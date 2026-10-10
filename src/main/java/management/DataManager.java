package management;

import activities.*;
import equipment.*;
import people.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DataManager handles all persistence for the Adventure Camp Management System.
 *
 * Data is stored as CSV files in the configured data directory.
 * Each entity type has its own CSV file with a header row.
 *
 * CSV files:
 *   camp.csv                  - Single row with camp metadata
 *   locations.csv             - One row per location
 *   instructors.csv           - One row per instructor (certifications semicolon-separated)
 *   participants.csv          - One row per participant
 *   equipment.csv             - One row per equipment item (type-specific columns included)
 *   activities.csv            - One row per activity (type-specific columns included)
 *   schedules.csv             - One row per schedule entry
 *   registrations.csv         - One row per registration
 *   safety_checks.csv         - One row per safety check
 *   equipment_allocations.csv - One row per equipment allocation
 *   attendance.csv            - One row per attendance record
 *   feedbacks.csv             - One row per feedback submission
 */
public class DataManager {

    /** Directory where all CSV data files are stored. */
    public static final String DEFAULT_DB_PATH = "data";

    // -------------------------------------------------------------------------
    // PUBLIC API
    // -------------------------------------------------------------------------

    /**
     * Loads the entire camp state from CSV files in the given directory.
     * If the directory does not exist, default seed data is created and saved.
     *
     * @param dataDir path to the data directory (e.g. "data")
     * @return a fully populated Camp object
     */
    public static Camp loadCamp(String dataDir) {
        File dir = new File(dataDir);

        if (!dir.exists()) {
            Camp defaultCamp = createDefaultCamp();
            saveCamp(defaultCamp, dataDir);
            return defaultCamp;
        }

        try {
            // Load in dependency order so foreign-key lookups always find their targets
            Camp camp = loadCampMetadata(dataDir);

            Map<String, Location>    locationById    = loadLocations(dataDir, camp);
            Map<String, Instructor>  instructorById  = loadInstructors(dataDir, camp);
            Map<String, Participant> participantById = loadParticipants(dataDir, camp);
            Map<String, Equipment>   equipmentById   = loadEquipment(dataDir, camp);
            Map<String, Activity>    activityById    = loadActivities(dataDir, camp, locationById, instructorById);

            loadSchedules(dataDir, camp, activityById);
            loadRegistrations(dataDir, camp, participantById, activityById);
            loadSafetyChecks(dataDir, camp, equipmentById, activityById);
            loadEquipmentAllocations(dataDir, camp, equipmentById, activityById);
            loadAttendance(dataDir, camp, participantById, activityById);
            loadFeedbacks(dataDir, camp, participantById, activityById);

            return camp;

        } catch (Exception e) {
            System.err.println("Warning: Error loading CSV data: " + e.getMessage());
            Camp defaultCamp = createDefaultCamp();
            saveCamp(defaultCamp, dataDir);
            return defaultCamp;
        }
    }

    /**
     * Saves the entire camp state to CSV files in the given directory.
     * Each entity type gets its own file.
     *
     * @param camp    the Camp object to persist
     * @param dataDir path to the data directory (e.g. "data")
     */
    public static void saveCamp(Camp camp, String dataDir) {
        try {
            Files.createDirectories(Paths.get(dataDir));

            saveCampMetadata(camp, dataDir);
            saveLocations(camp, dataDir);
            saveInstructors(camp, dataDir);
            saveParticipants(camp, dataDir);
            saveEquipment(camp, dataDir);
            saveActivities(camp, dataDir);
            saveSchedules(camp, dataDir);
            saveRegistrations(camp, dataDir);
            saveSafetyChecks(camp, dataDir);
            saveEquipmentAllocations(camp, dataDir);
            saveAttendance(camp, dataDir);
            saveFeedbacks(camp, dataDir);

        } catch (IOException e) {
            System.err.println("Error saving CSV data: " + e.getMessage());
        }
    }

    // =========================================================================
    // LOAD METHODS (one per entity type)
    // =========================================================================

    /** Reads camp.csv: campId, campName, startDate, endDate, capacity, status */
    private static Camp loadCampMetadata(String dataDir) throws IOException {
        List<String[]> rows = readCsv(dataDir + "/camp.csv");
        if (rows.size() < 2) {
            return new Camp("C1", "Summer Adventure Camp",
                    LocalDate.now(), LocalDate.now().plusDays(7), 100);
        }
        String[] r = rows.get(1);
        return new Camp(r[0], r[1], LocalDate.parse(r[2]), LocalDate.parse(r[3]), Integer.parseInt(r[4]));
    }

    /**
     * Reads locations.csv: locationId, name, address, terrainType, capacity
     * Returns a locationId -> Location map for use when loading activities.
     */
    private static Map<String, Location> loadLocations(String dataDir, Camp camp) throws IOException {
        Map<String, Location> map = new LinkedHashMap<>();
        for (String[] r : dataRows(readCsv(dataDir + "/locations.csv"))) {
            Location loc = new Location(r[0], r[1], r[2], r[3], Integer.parseInt(r[4]));
            camp.getLocations().add(loc);
            map.put(r[0], loc);
        }
        return map;
    }

    /**
     * Reads instructors.csv: id, name, age, contactNumber, specialization, certifications
     * Certifications are stored as one semicolon-separated value in column 6.
     * Returns an instructorId -> Instructor map for use when loading activities.
     */
    private static Map<String, Instructor> loadInstructors(String dataDir, Camp camp) throws IOException {
        Map<String, Instructor> map = new LinkedHashMap<>();
        for (String[] r : dataRows(readCsv(dataDir + "/instructors.csv"))) {
            Instructor inst = new Instructor(r[0], r[1], Integer.parseInt(r[2]), r[3], r[4]);
            if (r.length > 5 && !r[5].isEmpty()) {
                for (String cert : r[5].split(";")) {
                    inst.getCertifications().add(cert.trim());
                }
            }
            camp.getInstructors().add(inst);
            map.put(r[0], inst);
        }
        return map;
    }

    /**
     * Reads participants.csv: id, name, age, contactNumber, emergencyContact, medicalNotes, skillLevel
     * Returns a participantId -> Participant map for use when loading registrations.
     */
    private static Map<String, Participant> loadParticipants(String dataDir, Camp camp) throws IOException {
        Map<String, Participant> map = new LinkedHashMap<>();
        for (String[] r : dataRows(readCsv(dataDir + "/participants.csv"))) {
            Participant p = new Participant(r[0], r[1], Integer.parseInt(r[2]), r[3], r[4], r[5], r[6]);
            camp.getParticipants().add(p);
            map.put(r[0], p);
        }
        return map;
    }

    /**
     * Reads equipment.csv.
     * Columns: equipmentId, name, condition, isAvailable, type,
     *          lastInspectionDate (col 5), safetyRating (col 6),  -- SafetyEquipment
     *          sportType (col 7)                                   -- SportsEquipment
     * The 'type' column drives which subclass to instantiate.
     * Returns an equipmentId -> Equipment map.
     */
    private static Map<String, Equipment> loadEquipment(String dataDir, Camp camp) throws IOException {
        Map<String, Equipment> map = new LinkedHashMap<>();
        for (String[] r : dataRows(readCsv(dataDir + "/equipment.csv"))) {
            String id = r[0], name = r[1], condition = r[2];
            boolean available = Boolean.parseBoolean(r[3]);
            String type = r[4];

            Equipment eq;
            if ("SafetyEquipment".equalsIgnoreCase(type)) {
                LocalDate lastInsp = (r.length > 5 && !r[5].isEmpty()) ? LocalDate.parse(r[5]) : LocalDate.now();
                String rating = r.length > 6 ? r[6] : "A";
                eq = new SafetyEquipment(id, name, condition, lastInsp, rating);
            } else {
                eq = new SportsEquipment(id, name, condition, r.length > 7 ? r[7] : "General");
            }
            eq.setAvailable(available);
            camp.getEquipmentList().add(eq);
            map.put(id, eq);
        }
        return map;
    }

    /**
     * Reads activities.csv.
     * Common columns: activityId, title, description, durationMinutes, maxParticipants,
     *                 locationId, instructorId, type
     * Type-specific columns (leave empty if unused):
     *   col 8:  lifeguardRequired  (WaterActivity)
     *   col 9:  waterBodyType      (WaterActivity)
     *   col 10: teamSize           (TeamBuildingActivity)
     *   col 11: terrainType        (AdventureActivity)
     *   col 12: riskLevel          (AdventureActivity)
     * Returns an activityId -> Activity map.
     */
    private static Map<String, Activity> loadActivities(String dataDir, Camp camp,
            Map<String, Location> locationById, Map<String, Instructor> instructorById) throws IOException {
        Map<String, Activity> map = new LinkedHashMap<>();
        for (String[] r : dataRows(readCsv(dataDir + "/activities.csv"))) {
            String id = r[0], title = r[1], desc = r[2];
            int duration = Integer.parseInt(r[3]);
            int maxPart  = Integer.parseInt(r[4]);
            Location   loc  = locationById.get(r[5]);
            Instructor inst = instructorById.get(r[6]);
            String type = r[7];

            Activity act;
            if ("WaterActivity".equalsIgnoreCase(type)) {
                boolean lifeguard = r.length > 8 && Boolean.parseBoolean(r[8]);
                String waterBody  = r.length > 9 ? r[9] : "Lake";
                act = new WaterActivity(id, title, desc, duration, maxPart, loc, inst, lifeguard, waterBody);
            } else if ("TeamBuildingActivity".equalsIgnoreCase(type)) {
                int teamSize = r.length > 10 ? Integer.parseInt(r[10]) : 4;
                act = new TeamBuildingActivity(id, title, desc, duration, maxPart, loc, inst, teamSize);
            } else {
                String terrain = r.length > 11 ? r[11] : "General";
                String risk    = r.length > 12 ? r[12] : "Moderate";
                act = new AdventureActivity(id, title, desc, duration, maxPart, loc, inst, terrain, risk);
            }
            camp.getActivities().add(act);
            map.put(id, act);
        }
        return map;
    }

    /** Reads schedules.csv: entryId, activityId, startTime */
    private static void loadSchedules(String dataDir, Camp camp,
            Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/schedules.csv"))) {
            Activity act = activityById.get(r[1]);
            if (act != null && !r[2].isEmpty()) {
                LocalDateTime startTime = LocalDateTime.parse(r[2]);
                act.updateSchedule(startTime);
                camp.getSchedule().addEntry(new ScheduleEntry(r[0], act, startTime));
            }
        }
    }

    /** Reads registrations.csv: registrationId, participantId, activityId, registrationDate, status */
    private static void loadRegistrations(String dataDir, Camp camp,
            Map<String, Participant> participantById, Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/registrations.csv"))) {
            Participant part = participantById.get(r[1]);
            Activity act     = activityById.get(r[2]);
            if (part != null && act != null) {
                Registration reg = new Registration(r[0], part, act, LocalDate.parse(r[3]), r[4]);
                camp.getRegistrations().add(reg);
                part.addRegistration(reg);
                act.addParticipant(part);
            }
        }
    }

    /** Reads safety_checks.csv: checkId, passed, remarks, equipmentId, activityId, checkDate */
    private static void loadSafetyChecks(String dataDir, Camp camp,
            Map<String, Equipment> equipmentById, Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/safety_checks.csv"))) {
            Equipment eq = equipmentById.get(r[3]);
            Activity act = activityById.get(r[4].isEmpty() ? null : r[4]);
            camp.getSafetyChecks().add(
                    new SafetyCheck(r[0], Boolean.parseBoolean(r[1]), r[2], eq, act, LocalDate.parse(r[5])));
        }
    }

    /**
     * Reads equipment_allocations.csv:
     * allocationId, equipmentId, activityId, allocationDate, returnDate, status
     */
    private static void loadEquipmentAllocations(String dataDir, Camp camp,
            Map<String, Equipment> equipmentById, Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/equipment_allocations.csv"))) {
            Equipment eq = equipmentById.get(r[1]);
            Activity act = activityById.get(r[2]);
            if (eq != null && act != null) {
                LocalDate retDate = r[4].isEmpty() ? null : LocalDate.parse(r[4]);
                EquipmentAllocation alloc = new EquipmentAllocation(
                        r[0], eq, act, LocalDate.parse(r[3]), retDate, r[5]);
                act.addEquipmentAllocation(alloc);
                if ("Allocated".equalsIgnoreCase(r[5])) eq.setAvailable(false);
            }
        }
    }

    /** Reads attendance.csv: participantId, activityId, date, isPresent */
    private static void loadAttendance(String dataDir, Camp camp,
            Map<String, Participant> participantById, Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/attendance.csv"))) {
            Participant part = participantById.get(r[0]);
            Activity act     = activityById.get(r[1]);
            if (part != null && act != null) {
                camp.getAttendance().recordAttendance(
                        new AttendanceRecord(part, act, LocalDate.parse(r[2]), Boolean.parseBoolean(r[3])));
            }
        }
    }

    /** Reads feedbacks.csv: feedbackId, rating, comments, participantId, activityId, submittedDate */
    private static void loadFeedbacks(String dataDir, Camp camp,
            Map<String, Participant> participantById, Map<String, Activity> activityById) throws IOException {
        for (String[] r : dataRows(readCsv(dataDir + "/feedbacks.csv"))) {
            Participant part = participantById.get(r[3]);
            Activity act     = activityById.get(r[4]);
            if (part != null && act != null) {
                camp.addFeedback(
                        new Feedback(r[0], Integer.parseInt(r[1]), r[2], part, act, LocalDate.parse(r[5])));
            }
        }
    }

    // =========================================================================
    // SAVE METHODS (one per entity type)
    // =========================================================================

    /** Writes camp.csv: campId, campName, startDate, endDate, capacity, status */
    private static void saveCampMetadata(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/camp.csv")) {
            w.println("campId,campName,startDate,endDate,capacity,status");
            w.println(String.join(",",
                    camp.getCampId(), escapeCsv(camp.getCampName()),
                    camp.getStartDate().toString(), camp.getEndDate().toString(),
                    String.valueOf(camp.getCapacity()), camp.getStatus()));
        }
    }

    /** Writes locations.csv: locationId, name, address, terrainType, capacity */
    private static void saveLocations(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/locations.csv")) {
            w.println("locationId,name,address,terrainType,capacity");
            for (Location loc : camp.getLocations().getAll()) {
                w.println(String.join(",",
                        loc.getLocationId(), escapeCsv(loc.getName()),
                        escapeCsv(loc.getAddress()), escapeCsv(loc.getTerrainType()),
                        String.valueOf(loc.getCapacity())));
            }
        }
    }

    /** Writes instructors.csv: id, name, age, contactNumber, specialization, certifications */
    private static void saveInstructors(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/instructors.csv")) {
            w.println("id,name,age,contactNumber,specialization,certifications");
            for (Instructor inst : camp.getInstructors().getAll()) {
                w.println(String.join(",",
                        inst.getId(), escapeCsv(inst.getName()), String.valueOf(inst.getAge()),
                        escapeCsv(inst.getContactNumber()), escapeCsv(inst.getSpecialization()),
                        escapeCsv(String.join(";", inst.getCertifications()))));
            }
        }
    }

    /** Writes participants.csv: id, name, age, contactNumber, emergencyContact, medicalNotes, skillLevel */
    private static void saveParticipants(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/participants.csv")) {
            w.println("id,name,age,contactNumber,emergencyContact,medicalNotes,skillLevel");
            for (Participant p : camp.getParticipants().getAll()) {
                w.println(String.join(",",
                        p.getId(), escapeCsv(p.getName()), String.valueOf(p.getAge()),
                        escapeCsv(p.getContactNumber()), escapeCsv(p.getEmergencyContact()),
                        escapeCsv(p.getMedicalNotes()), escapeCsv(p.getSkillLevel())));
            }
        }
    }

    /**
     * Writes equipment.csv.
     * Columns: equipmentId, name, condition, isAvailable, type,
     *          lastInspectionDate, safetyRating, sportType
     * Columns not applicable to a subtype are written as empty strings.
     */
    private static void saveEquipment(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/equipment.csv")) {
            w.println("equipmentId,name,condition,isAvailable,type,lastInspectionDate,safetyRating,sportType");
            for (Equipment eq : camp.getEquipmentList().getAll()) {
                String type, lastInsp = "", rating = "", sport = "";
                if (eq instanceof SafetyEquipment) {
                    SafetyEquipment seq = (SafetyEquipment) eq;
                    type    = "SafetyEquipment";
                    lastInsp = seq.getLastInspectionDate() != null
                            ? seq.getLastInspectionDate().toString() : LocalDate.now().toString();
                    rating  = seq.getSafetyRating();
                } else {
                    type  = "SportsEquipment";
                    sport = ((SportsEquipment) eq).getSportType();
                }
                w.println(String.join(",",
                        eq.getEquipmentId(), escapeCsv(eq.getName()), escapeCsv(eq.getCondition()),
                        String.valueOf(eq.isAvailable()), type, lastInsp, rating, escapeCsv(sport)));
            }
        }
    }

    /**
     * Writes activities.csv.
     * Common columns: activityId, title, description, durationMinutes, maxParticipants,
     *                 locationId, instructorId, type
     * Type-specific columns are left empty when not applicable:
     *   lifeguardRequired, waterBodyType (WaterActivity)
     *   teamSize                         (TeamBuildingActivity)
     *   terrainType, riskLevel           (AdventureActivity)
     */
    private static void saveActivities(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/activities.csv")) {
            w.println("activityId,title,description,durationMinutes,maxParticipants,"
                    + "locationId,instructorId,type,"
                    + "lifeguardRequired,waterBodyType,teamSize,terrainType,riskLevel");
            for (Activity act : camp.getActivities().getAll()) {
                String locId  = act.getLocation()   != null ? act.getLocation().getLocationId() : "";
                String instId = act.getInstructor() != null ? act.getInstructor().getId()       : "";
                String type, lifeguard = "", waterBody = "", teamSize = "", terrain = "", risk = "";
                if (act instanceof WaterActivity) {
                    WaterActivity wa = (WaterActivity) act;
                    type      = "WaterActivity";
                    lifeguard = String.valueOf(wa.isLifeguardRequired());
                    waterBody = wa.getWaterBodyType();
                } else if (act instanceof TeamBuildingActivity) {
                    type     = "TeamBuildingActivity";
                    teamSize = String.valueOf(((TeamBuildingActivity) act).getTeamSize());
                } else {
                    AdventureActivity aa = (AdventureActivity) act;
                    type    = "AdventureActivity";
                    terrain = aa.getTerrainType();
                    risk    = aa.getRiskLevel();
                }
                w.println(String.join(",",
                        act.getActivityId(), escapeCsv(act.getTitle()), escapeCsv(act.getDescription()),
                        String.valueOf(act.getDurationMinutes()), String.valueOf(act.getMaxParticipants()),
                        locId, instId, type,
                        lifeguard, escapeCsv(waterBody), teamSize, escapeCsv(terrain), escapeCsv(risk)));
            }
        }
    }

    /** Writes schedules.csv: entryId, activityId, startTime */
    private static void saveSchedules(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/schedules.csv")) {
            w.println("entryId,activityId,startTime");
            for (ScheduleEntry entry : camp.getSchedule().getEntries().getAll()) {
                String actId = entry.getActivity()  != null ? entry.getActivity().getActivityId() : "";
                String time  = entry.getStartTime() != null ? entry.getStartTime().toString()     : "";
                w.println(String.join(",", entry.getEntryId(), actId, time));
            }
        }
    }

    /** Writes registrations.csv: registrationId, participantId, activityId, registrationDate, status */
    private static void saveRegistrations(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/registrations.csv")) {
            w.println("registrationId,participantId,activityId,registrationDate,status");
            for (Registration reg : camp.getRegistrations().getAll()) {
                String partId  = reg.getParticipant()      != null ? reg.getParticipant().getId()          : "";
                String actId   = reg.getActivity()         != null ? reg.getActivity().getActivityId()     : "";
                String regDate = reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString()  : "";
                w.println(String.join(",", reg.getRegistrationId(), partId, actId, regDate, reg.getStatus()));
            }
        }
    }

    /** Writes safety_checks.csv: checkId, passed, remarks, equipmentId, activityId, checkDate */
    private static void saveSafetyChecks(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/safety_checks.csv")) {
            w.println("checkId,passed,remarks,equipmentId,activityId,checkDate");
            for (SafetyCheck sc : camp.getSafetyChecks().getAll()) {
                String eqId  = sc.getEquipment() != null ? sc.getEquipment().getEquipmentId() : "";
                String actId = sc.getActivity()  != null ? sc.getActivity().getActivityId()   : "";
                String date  = sc.getCheckDate() != null ? sc.getCheckDate().toString()       : "";
                w.println(String.join(",",
                        sc.getCheckId(), String.valueOf(sc.isPassed()),
                        escapeCsv(sc.getRemarks()), eqId, actId, date));
            }
        }
    }

    /**
     * Writes equipment_allocations.csv:
     * allocationId, equipmentId, activityId, allocationDate, returnDate, status
     */
    private static void saveEquipmentAllocations(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/equipment_allocations.csv")) {
            w.println("allocationId,equipmentId,activityId,allocationDate,returnDate,status");
            for (Activity act : camp.getActivities().getAll()) {
                for (EquipmentAllocation ea : act.getEquipmentAllocations()) {
                    String eqId    = ea.getEquipment()    != null ? ea.getEquipment().getEquipmentId() : "";
                    String actId   = ea.getActivity()     != null ? ea.getActivity().getActivityId()   : "";
                    String alloc   = ea.getAllocationDate()!= null ? ea.getAllocationDate().toString()  : "";
                    String ret     = ea.getReturnDate()   != null ? ea.getReturnDate().toString()      : "";
                    w.println(String.join(",", ea.getAllocationId(), eqId, actId, alloc, ret, ea.getStatus()));
                }
            }
        }
    }

    /** Writes attendance.csv: participantId, activityId, date, isPresent */
    private static void saveAttendance(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/attendance.csv")) {
            w.println("participantId,activityId,date,isPresent");
            for (AttendanceRecord ar : camp.getAttendance().getRecords().getAll()) {
                String partId = ar.getParticipant() != null ? ar.getParticipant().getId()      : "";
                String actId  = ar.getActivity()    != null ? ar.getActivity().getActivityId() : "";
                String date   = ar.getDate()        != null ? ar.getDate().toString()          : "";
                w.println(String.join(",", partId, actId, date, String.valueOf(ar.isPresent())));
            }
        }
    }

    /** Writes feedbacks.csv: feedbackId, rating, comments, participantId, activityId, submittedDate */
    private static void saveFeedbacks(Camp camp, String dataDir) throws IOException {
        try (PrintWriter w = openCsvWriter(dataDir + "/feedbacks.csv")) {
            w.println("feedbackId,rating,comments,participantId,activityId,submittedDate");
            for (Feedback fb : camp.getFeedbacks().getAll()) {
                String partId  = fb.getParticipant()  != null ? fb.getParticipant().getId()        : "";
                String actId   = fb.getActivity()     != null ? fb.getActivity().getActivityId()   : "";
                String subDate = fb.getSubmittedDate()!= null ? fb.getSubmittedDate().toString()   : "";
                w.println(String.join(",",
                        fb.getFeedbackId(), String.valueOf(fb.getRating()),
                        escapeCsv(fb.getComments()), partId, actId, subDate));
            }
        }
    }

    // =========================================================================
    // DEFAULT SEED DATA
    // =========================================================================

    /**
     * Creates a default Camp with sample locations, instructors, participants,
     * activities, and equipment. Used when the data directory does not yet exist.
     */
    private static Camp createDefaultCamp() {
        Camp camp = new Camp("C1", "Summer Adventure Camp",
                LocalDate.now(), LocalDate.now().plusDays(7), 100);

        Location loc1 = new Location("L1", "River Zone",    "North Woods",   "Water",     20);
        Location loc2 = new Location("L2", "Climbing Wall", "Base Camp",     "Mountain",  10);
        Location loc3 = new Location("L3", "Camp Ground",   "Central Field", "Grassland", 50);
        camp.getLocations().add(loc1);
        camp.getLocations().add(loc2);
        camp.getLocations().add(loc3);

        Instructor i1 = new Instructor("I1", "Alice", 30, "555-0101", "Water Sports");
        i1.getCertifications().add("Lifeguard");
        i1.getCertifications().add("Kayak Instructor");
        Instructor i2 = new Instructor("I2", "Dave", 35, "555-0102", "Climbing & Ropes");
        i2.getCertifications().add("Lead Climbing");
        i2.getCertifications().add("Wilderness First Aid");
        camp.getInstructors().add(i1);
        camp.getInstructors().add(i2);

        camp.getParticipants().add(new Participant("P1", "Bob",     20, "555-0201", "Mom: 555-0000",    "None",   "Beginner"));
        camp.getParticipants().add(new Participant("P2", "Charlie", 22, "555-0202", "Dad: 555-0001",    "None",   "Intermediate"));
        camp.getParticipants().add(new Participant("P3", "Diana",   19, "555-0203", "Sister: 555-0002", "Asthma", "Advanced"));

        camp.getActivities().add(new WaterActivity("A1", "Kayaking Basics", "Learn fundamentals of kayaking", 120, 10, loc1, i1, true, "River"));
        camp.getActivities().add(new AdventureActivity("A2", "Rock Climbing", "Introductory rock climbing wall", 90, 8, loc2, i2, "Rock Face", "Moderate"));
        camp.getActivities().add(new TeamBuildingActivity("A3", "Rope Course Challenge", "Obstacle course navigation", 60, 15, loc3, i1, 5));

        camp.getEquipmentList().add(new SafetyEquipment("E1", "Life Jacket",      "Good", LocalDate.now(), "A"));
        camp.getEquipmentList().add(new SafetyEquipment("E2", "Climbing Helmet",  "Good", LocalDate.now(), "A"));
        camp.getEquipmentList().add(new SportsEquipment("E3", "Kayak Paddle",     "Good", "Kayaking"));
        camp.getEquipmentList().add(new SportsEquipment("E4", "Climbing Harness", "Good", "Climbing"));

        return camp;
    }

    // =========================================================================
    // CSV UTILITY HELPERS
    // =========================================================================

    /**
     * Reads all rows from a CSV file as a List of String arrays.
     * Index 0 is the header row; data rows start at index 1.
     * Returns an empty list if the file does not exist.
     */
    private static List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return rows;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(parseCsvLine(line));
            }
        }
        return rows;
    }

    /**
     * Returns only the data rows (skipping the header at index 0) from a list of CSV rows.
     * Convenience helper to avoid repeated "for (int i = 1; i < rows.size(); i++)" loops.
     */
    private static List<String[]> dataRows(List<String[]> rows) {
        return rows.size() > 1 ? rows.subList(1, rows.size()) : Collections.emptyList();
    }

    /**
     * Parses one CSV line into an array of field strings.
     * Respects double-quoted fields: fields may contain commas if wrapped in "".
     * A literal double-quote inside a quoted field is represented as "".
     *
     * Example: 'foo,"bar,baz","say ""hi"""'  =>  ["foo", "bar,baz", "say \"hi\""]
     */
    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"'); // escaped quote ("") inside a quoted field
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString()); // add the last field
        return fields.toArray(new String[0]);
    }

    /**
     * Wraps a value in double-quotes if it contains a comma, double-quote, or newline.
     * Any existing double-quotes within the value are escaped by doubling them ("").
     *
     * @param value the raw string to escape
     * @return a CSV-safe string
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /** Opens a UTF-8 PrintWriter, creating or overwriting the target file. */
    private static PrintWriter openCsvWriter(String filePath) throws IOException {
        return new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), java.nio.charset.StandardCharsets.UTF_8));
    }
}
