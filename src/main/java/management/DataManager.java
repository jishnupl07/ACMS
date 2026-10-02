package management;

import activities.*;
import equipment.*;
import people.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DataManager {
    public static final String DEFAULT_DB_PATH = "data/database.json";

    public static Camp loadCamp(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Camp defaultCamp = createDefaultCamp();
            saveCamp(defaultCamp, filePath);
            return defaultCamp;
        }

        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
            Object rootObj = SimpleJsonParser.parse(jsonStr);
            if (!(rootObj instanceof Map)) {
                Camp defaultCamp = createDefaultCamp();
                saveCamp(defaultCamp, filePath);
                return defaultCamp;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> root = (Map<String, Object>) rootObj;

            // 1. Camp basic details
            @SuppressWarnings("unchecked")
            Map<String, Object> campMap = (Map<String, Object>) root.get("camp");
            String campId = campMap != null ? (String) campMap.getOrDefault("campId", "C1") : "C1";
            String campName = campMap != null ? (String) campMap.getOrDefault("campName", "Adventure Camp") : "Adventure Camp";
            LocalDate startDate = campMap != null && campMap.get("startDate") != null ? LocalDate.parse((String) campMap.get("startDate")) : LocalDate.now();
            LocalDate endDate = campMap != null && campMap.get("endDate") != null ? LocalDate.parse((String) campMap.get("endDate")) : LocalDate.now().plusDays(7);
            int capacity = campMap != null && campMap.get("capacity") != null ? ((Number) campMap.get("capacity")).intValue() : 100;

            Camp camp = new Camp(campId, campName, startDate, endDate, capacity);

            // 2. Locations
            @SuppressWarnings("unchecked")
            List<Object> locList = (List<Object>) root.getOrDefault("locations", Collections.emptyList());
            Map<String, Location> locationMap = new HashMap<>();
            for (Object item : locList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String lId = (String) m.get("locationId");
                    String name = (String) m.get("name");
                    String address = (String) m.getOrDefault("address", "");
                    String terrain = (String) m.getOrDefault("terrainType", "");
                    int cap = m.get("capacity") != null ? ((Number) m.get("capacity")).intValue() : 50;
                    Location loc = new Location(lId, name, address, terrain, cap);
                    camp.getLocations().add(loc);
                    locationMap.put(lId, loc);
                }
            }

            // 3. Instructors
            @SuppressWarnings("unchecked")
            List<Object> instList = (List<Object>) root.getOrDefault("instructors", Collections.emptyList());
            Map<String, Instructor> instructorMap = new HashMap<>();
            for (Object item : instList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String id = (String) m.get("id");
                    String name = (String) m.get("name");
                    int age = m.get("age") != null ? ((Number) m.get("age")).intValue() : 30;
                    String contact = (String) m.getOrDefault("contactNumber", "");
                    String spec = (String) m.getOrDefault("specialization", "General");
                    Instructor inst = new Instructor(id, name, age, contact, spec);
                    if (m.get("certifications") instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Object> certs = (List<Object>) m.get("certifications");
                        for (Object c : certs) {
                            if (c != null) inst.getCertifications().add(c.toString());
                        }
                    }
                    camp.getInstructors().add(inst);
                    instructorMap.put(id, inst);
                }
            }

            // 4. Participants
            @SuppressWarnings("unchecked")
            List<Object> partList = (List<Object>) root.getOrDefault("participants", Collections.emptyList());
            Map<String, Participant> participantMap = new HashMap<>();
            for (Object item : partList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String id = (String) m.get("id");
                    String name = (String) m.get("name");
                    int age = m.get("age") != null ? ((Number) m.get("age")).intValue() : 18;
                    String contact = (String) m.getOrDefault("contactNumber", "");
                    String emergency = (String) m.getOrDefault("emergencyContact", "");
                    String medical = (String) m.getOrDefault("medicalNotes", "None");
                    String skill = (String) m.getOrDefault("skillLevel", "Beginner");
                    Participant part = new Participant(id, name, age, contact, emergency, medical, skill);
                    camp.getParticipants().add(part);
                    participantMap.put(id, part);
                }
            }

            // 5. Equipment
            @SuppressWarnings("unchecked")
            List<Object> eqList = (List<Object>) root.getOrDefault("equipment", Collections.emptyList());
            Map<String, Equipment> equipmentMap = new HashMap<>();
            for (Object item : eqList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String id = (String) m.get("equipmentId");
                    String name = (String) m.get("name");
                    String condition = (String) m.getOrDefault("condition", "Good");
                    String type = (String) m.getOrDefault("type", "SafetyEquipment");
                    boolean isAvail = m.get("isAvailable") != null ? (Boolean) m.get("isAvailable") : true;

                    Equipment eq;
                    if ("SafetyEquipment".equalsIgnoreCase(type)) {
                        LocalDate lastInsp = m.get("lastInspectionDate") != null ? LocalDate.parse((String) m.get("lastInspectionDate")) : LocalDate.now();
                        String rating = (String) m.getOrDefault("safetyRating", "A");
                        eq = new SafetyEquipment(id, name, condition, lastInsp, rating);
                    } else {
                        String sportType = (String) m.getOrDefault("sportType", "General");
                        eq = new SportsEquipment(id, name, condition, sportType);
                    }
                    eq.setAvailable(isAvail);
                    camp.getEquipmentList().add(eq);
                    equipmentMap.put(id, eq);
                }
            }

            // 6. Activities
            @SuppressWarnings("unchecked")
            List<Object> actList = (List<Object>) root.getOrDefault("activities", Collections.emptyList());
            Map<String, Activity> activityMap = new HashMap<>();
            for (Object item : actList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String id = (String) m.get("activityId");
                    String title = (String) m.get("title");
                    String desc = (String) m.getOrDefault("description", "");
                    int duration = m.get("durationMinutes") != null ? ((Number) m.get("durationMinutes")).intValue() : 60;
                    int maxPart = m.get("maxParticipants") != null ? ((Number) m.get("maxParticipants")).intValue() : 20;
                    String locId = (String) m.get("locationId");
                    String instId = (String) m.get("instructorId");
                    String type = (String) m.getOrDefault("type", "AdventureActivity");

                    Location loc = locationMap.get(locId);
                    Instructor inst = instructorMap.get(instId);

                    Activity act;
                    if ("WaterActivity".equalsIgnoreCase(type)) {
                        boolean lifeguard = m.get("lifeguardRequired") != null ? (Boolean) m.get("lifeguardRequired") : false;
                        String waterBody = (String) m.getOrDefault("waterBodyType", "Lake");
                        act = new WaterActivity(id, title, desc, duration, maxPart, loc, inst, lifeguard, waterBody);
                    } else if ("TeamBuildingActivity".equalsIgnoreCase(type)) {
                        int teamSize = m.get("teamSize") != null ? ((Number) m.get("teamSize")).intValue() : 4;
                        act = new TeamBuildingActivity(id, title, desc, duration, maxPart, loc, inst, teamSize);
                    } else {
                        String terrain = (String) m.getOrDefault("terrainType", "General");
                        String risk = (String) m.getOrDefault("riskLevel", "Moderate");
                        act = new AdventureActivity(id, title, desc, duration, maxPart, loc, inst, terrain, risk);
                    }
                    camp.getActivities().add(act);
                    activityMap.put(id, act);
                }
            }

            // 7. Schedules
            @SuppressWarnings("unchecked")
            List<Object> schList = (List<Object>) root.getOrDefault("schedules", Collections.emptyList());
            for (Object item : schList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String entryId = (String) m.get("entryId");
                    String actId = (String) m.get("activityId");
                    String startStr = (String) m.get("startTime");
                    Activity act = activityMap.get(actId);
                    if (act != null && startStr != null) {
                        LocalDateTime startTime = LocalDateTime.parse(startStr);
                        act.updateSchedule(startTime);
                        ScheduleEntry entry = new ScheduleEntry(entryId, act, startTime);
                        camp.getSchedule().addEntry(entry);
                    }
                }
            }

            // 8. Registrations
            @SuppressWarnings("unchecked")
            List<Object> regList = (List<Object>) root.getOrDefault("registrations", Collections.emptyList());
            for (Object item : regList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String regId = (String) m.get("registrationId");
                    String pId = (String) m.get("participantId");
                    String aId = (String) m.get("activityId");
                    String regDateStr = (String) m.get("registrationDate");
                    String status = (String) m.getOrDefault("status", "Confirmed");

                    Participant part = participantMap.get(pId);
                    Activity act = activityMap.get(aId);
                    if (part != null && act != null) {
                        LocalDate regDate = regDateStr != null ? LocalDate.parse(regDateStr) : LocalDate.now();
                        Registration reg = new Registration(regId, part, act, regDate, status);
                        camp.getRegistrations().add(reg);
                        part.addRegistration(reg);
                        act.addParticipant(part);
                    }
                }
            }

            // 9. Safety Checks
            @SuppressWarnings("unchecked")
            List<Object> scList = (List<Object>) root.getOrDefault("safetyChecks", Collections.emptyList());
            for (Object item : scList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String checkId = (String) m.get("checkId");
                    String dateStr = (String) m.get("checkDate");
                    boolean passed = m.get("passed") != null ? (Boolean) m.get("passed") : true;
                    String remarks = (String) m.getOrDefault("remarks", "");
                    String eqId = (String) m.get("equipmentId");
                    String actId = (String) m.get("activityId");

                    Equipment eq = equipmentMap.get(eqId);
                    Activity act = activityMap.get(actId);
                    LocalDate checkDate = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
                    SafetyCheck check = new SafetyCheck(checkId, passed, remarks, eq, act, checkDate);
                    camp.getSafetyChecks().add(check);
                }
            }

            // 10. Equipment Allocations
            @SuppressWarnings("unchecked")
            List<Object> allocList = (List<Object>) root.getOrDefault("equipmentAllocations", Collections.emptyList());
            for (Object item : allocList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String allocId = (String) m.get("allocationId");
                    String eqId = (String) m.get("equipmentId");
                    String actId = (String) m.get("activityId");
                    String allocDateStr = (String) m.get("allocationDate");
                    String retDateStr = (String) m.get("returnDate");
                    String status = (String) m.getOrDefault("status", "Allocated");

                    Equipment eq = equipmentMap.get(eqId);
                    Activity act = activityMap.get(actId);
                    if (eq != null && act != null) {
                        LocalDate allocDate = allocDateStr != null ? LocalDate.parse(allocDateStr) : LocalDate.now();
                        LocalDate retDate = retDateStr != null ? LocalDate.parse(retDateStr) : null;
                        EquipmentAllocation alloc = new EquipmentAllocation(allocId, eq, act, allocDate, retDate, status);
                        act.addEquipmentAllocation(alloc);
                        if ("Allocated".equalsIgnoreCase(status)) {
                            eq.setAvailable(false);
                        }
                    }
                }
            }

            // 11. Attendance Records
            @SuppressWarnings("unchecked")
            List<Object> attList = (List<Object>) root.getOrDefault("attendanceRecords", Collections.emptyList());
            for (Object item : attList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String pId = (String) m.get("participantId");
                    String aId = (String) m.get("activityId");
                    String dateStr = (String) m.get("date");
                    boolean isPresent = m.get("isPresent") != null ? (Boolean) m.get("isPresent") : true;

                    Participant part = participantMap.get(pId);
                    Activity act = activityMap.get(aId);
                    if (part != null && act != null) {
                        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
                        AttendanceRecord rec = new AttendanceRecord(part, act, date, isPresent);
                        camp.getAttendance().recordAttendance(rec);
                    }
                }
            }

            // 12. Feedbacks
            @SuppressWarnings("unchecked")
            List<Object> fbList = (List<Object>) root.getOrDefault("feedbacks", Collections.emptyList());
            for (Object item : fbList) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    String fbId = (String) m.get("feedbackId");
                    String pId = (String) m.get("participantId");
                    String aId = (String) m.get("activityId");
                    int rating = m.get("rating") != null ? ((Number) m.get("rating")).intValue() : 5;
                    String comments = (String) m.getOrDefault("comments", "");
                    String subDateStr = (String) m.get("submittedDate");

                    Participant part = participantMap.get(pId);
                    Activity act = activityMap.get(aId);
                    if (part != null && act != null) {
                        LocalDate subDate = subDateStr != null ? LocalDate.parse(subDateStr) : LocalDate.now();
                        Feedback fb = new Feedback(fbId, rating, comments, part, act, subDate);
                        camp.addFeedback(fb);
                    }
                }
            }

            return camp;
        } catch (Exception e) {
            System.err.println("Warning: Error loading database.json: " + e.getMessage());
            Camp defaultCamp = createDefaultCamp();
            saveCamp(defaultCamp, filePath);
            return defaultCamp;
        }
    }

    public static void saveCamp(Camp camp, String filePath) {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            Map<String, Object> root = new LinkedHashMap<>();

            // 1. Camp
            Map<String, Object> campMap = new LinkedHashMap<>();
            campMap.put("campId", camp.getCampId());
            campMap.put("campName", camp.getCampName());
            campMap.put("startDate", camp.getStartDate().toString());
            campMap.put("endDate", camp.getEndDate().toString());
            campMap.put("capacity", camp.getCapacity());
            campMap.put("status", camp.getStatus());
            root.put("camp", campMap);

            // 2. Locations
            List<Map<String, Object>> locationsList = new ArrayList<>();
            for (Location loc : camp.getLocations().getAll()) {
                Map<String, Object> lm = new LinkedHashMap<>();
                lm.put("locationId", loc.getLocationId());
                lm.put("name", loc.getName());
                lm.put("address", loc.getAddress());
                lm.put("terrainType", loc.getTerrainType());
                lm.put("capacity", loc.getCapacity());
                locationsList.add(lm);
            }
            root.put("locations", locationsList);

            // 3. Instructors
            List<Map<String, Object>> instructorsList = new ArrayList<>();
            for (Instructor inst : camp.getInstructors().getAll()) {
                Map<String, Object> im = new LinkedHashMap<>();
                im.put("id", inst.getId());
                im.put("name", inst.getName());
                im.put("age", inst.getAge());
                im.put("contactNumber", inst.getContactNumber());
                im.put("specialization", inst.getSpecialization());
                im.put("certifications", inst.getCertifications());
                instructorsList.add(im);
            }
            root.put("instructors", instructorsList);

            // 4. Participants
            List<Map<String, Object>> participantsList = new ArrayList<>();
            for (Participant p : camp.getParticipants().getAll()) {
                Map<String, Object> pm = new LinkedHashMap<>();
                pm.put("id", p.getId());
                pm.put("name", p.getName());
                pm.put("age", p.getAge());
                pm.put("contactNumber", p.getContactNumber());
                pm.put("emergencyContact", p.getEmergencyContact());
                pm.put("medicalNotes", p.getMedicalNotes());
                pm.put("skillLevel", p.getSkillLevel());
                participantsList.add(pm);
            }
            root.put("participants", participantsList);

            // 5. Equipment
            List<Map<String, Object>> eqList = new ArrayList<>();
            for (Equipment eq : camp.getEquipmentList().getAll()) {
                Map<String, Object> em = new LinkedHashMap<>();
                em.put("equipmentId", eq.getEquipmentId());
                em.put("name", eq.getName());
                em.put("condition", eq.getCondition());
                em.put("isAvailable", eq.isAvailable());
                if (eq instanceof SafetyEquipment) {
                    SafetyEquipment seq = (SafetyEquipment) eq;
                    em.put("type", "SafetyEquipment");
                    em.put("lastInspectionDate", seq.getLastInspectionDate() != null ? seq.getLastInspectionDate().toString() : LocalDate.now().toString());
                    em.put("safetyRating", seq.getSafetyRating());
                } else if (eq instanceof SportsEquipment) {
                    SportsEquipment seq = (SportsEquipment) eq;
                    em.put("type", "SportsEquipment");
                    em.put("sportType", seq.getSportType());
                }
                eqList.add(em);
            }
            root.put("equipment", eqList);

            // 6. Activities
            List<Map<String, Object>> actList = new ArrayList<>();
            for (Activity act : camp.getActivities().getAll()) {
                Map<String, Object> am = new LinkedHashMap<>();
                am.put("activityId", act.getActivityId());
                am.put("title", act.getTitle());
                am.put("description", act.getDescription());
                am.put("durationMinutes", act.getDurationMinutes());
                am.put("maxParticipants", act.getMaxParticipants());
                am.put("locationId", act.getLocation() != null ? act.getLocation().getLocationId() : null);
                am.put("instructorId", act.getInstructor() != null ? act.getInstructor().getId() : null);

                if (act instanceof WaterActivity) {
                    WaterActivity wact = (WaterActivity) act;
                    am.put("type", "WaterActivity");
                    am.put("lifeguardRequired", wact.isLifeguardRequired());
                    am.put("waterBodyType", wact.getWaterBodyType());
                } else if (act instanceof TeamBuildingActivity) {
                    TeamBuildingActivity tact = (TeamBuildingActivity) act;
                    am.put("type", "TeamBuildingActivity");
                    am.put("teamSize", tact.getTeamSize());
                } else if (act instanceof AdventureActivity) {
                    AdventureActivity aact = (AdventureActivity) act;
                    am.put("type", "AdventureActivity");
                    am.put("terrainType", aact.getTerrainType());
                    am.put("riskLevel", aact.getRiskLevel());
                }
                actList.add(am);
            }
            root.put("activities", actList);

            // 7. Schedules
            List<Map<String, Object>> schList = new ArrayList<>();
            for (ScheduleEntry se : camp.getSchedule().getEntries().getAll()) {
                Map<String, Object> sm = new LinkedHashMap<>();
                sm.put("entryId", se.getEntryId());
                sm.put("activityId", se.getActivity() != null ? se.getActivity().getActivityId() : null);
                sm.put("startTime", se.getStartTime() != null ? se.getStartTime().toString() : null);
                schList.add(sm);
            }
            root.put("schedules", schList);

            // 8. Registrations
            List<Map<String, Object>> regList = new ArrayList<>();
            for (Registration reg : camp.getRegistrations().getAll()) {
                Map<String, Object> rm = new LinkedHashMap<>();
                rm.put("registrationId", reg.getRegistrationId());
                rm.put("participantId", reg.getParticipant() != null ? reg.getParticipant().getId() : null);
                rm.put("activityId", reg.getActivity() != null ? reg.getActivity().getActivityId() : null);
                rm.put("registrationDate", reg.getRegistrationDate() != null ? reg.getRegistrationDate().toString() : null);
                rm.put("status", reg.getStatus());
                regList.add(rm);
            }
            root.put("registrations", regList);

            // 9. Safety Checks
            List<Map<String, Object>> scList = new ArrayList<>();
            for (SafetyCheck sc : camp.getSafetyChecks().getAll()) {
                Map<String, Object> scm = new LinkedHashMap<>();
                scm.put("checkId", sc.getCheckId());
                scm.put("checkDate", sc.getCheckDate() != null ? sc.getCheckDate().toString() : null);
                scm.put("passed", sc.isPassed());
                scm.put("remarks", sc.getRemarks());
                scm.put("equipmentId", sc.getEquipment() != null ? sc.getEquipment().getEquipmentId() : null);
                scm.put("activityId", sc.getActivity() != null ? sc.getActivity().getActivityId() : null);
                scList.add(scm);
            }
            root.put("safetyChecks", scList);

            // 10. Equipment Allocations
            List<Map<String, Object>> allocList = new ArrayList<>();
            for (Activity act : camp.getActivities().getAll()) {
                for (EquipmentAllocation ea : act.getEquipmentAllocations()) {
                    Map<String, Object> eam = new LinkedHashMap<>();
                    eam.put("allocationId", ea.getAllocationId());
                    eam.put("equipmentId", ea.getEquipment() != null ? ea.getEquipment().getEquipmentId() : null);
                    eam.put("activityId", ea.getActivity() != null ? ea.getActivity().getActivityId() : null);
                    eam.put("allocationDate", ea.getAllocationDate() != null ? ea.getAllocationDate().toString() : null);
                    eam.put("returnDate", ea.getReturnDate() != null ? ea.getReturnDate().toString() : null);
                    eam.put("status", ea.getStatus());
                    allocList.add(eam);
                }
            }
            root.put("equipmentAllocations", allocList);

            // 11. Attendance Records
            List<Map<String, Object>> attList = new ArrayList<>();
            for (AttendanceRecord ar : camp.getAttendance().getRecords().getAll()) {
                Map<String, Object> arm = new LinkedHashMap<>();
                arm.put("participantId", ar.getParticipant() != null ? ar.getParticipant().getId() : null);
                arm.put("activityId", ar.getActivity() != null ? ar.getActivity().getActivityId() : null);
                arm.put("date", ar.isPresent() ? LocalDate.now().toString() : null);
                arm.put("isPresent", ar.isPresent());
                attList.add(arm);
            }
            root.put("attendanceRecords", attList);

            // 12. Feedbacks
            List<Map<String, Object>> fbList = new ArrayList<>();
            for (Feedback fb : camp.getFeedbacks().getAll()) {
                Map<String, Object> fbm = new LinkedHashMap<>();
                fbm.put("feedbackId", fb.getFeedbackId());
                fbm.put("participantId", fb.getParticipant() != null ? fb.getParticipant().getId() : null);
                fbm.put("activityId", fb.getActivity() != null ? fb.getActivity().getActivityId() : null);
                fbm.put("rating", fb.getRating());
                fbm.put("comments", fb.getComments());
                fbm.put("submittedDate", fb.getSubmittedDate() != null ? fb.getSubmittedDate().toString() : null);
                fbList.add(fbm);
            }
            root.put("feedbacks", fbList);

            String formattedJson = SimpleJsonSerializer.serialize(root);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(formattedJson);
            }
        } catch (IOException e) {
            System.err.println("Error saving database.json: " + e.getMessage());
        }
    }

    private static Camp createDefaultCamp() {
        Camp camp = new Camp("C1", "Summer Adventure Camp", LocalDate.now(), LocalDate.now().plusDays(7), 100);
        Location loc1 = new Location("L1", "River Zone", "North Woods", "Water", 20);
        Location loc2 = new Location("L2", "Climbing Wall", "Base Camp", "Mountain", 10);
        Location loc3 = new Location("L3", "Camp Ground", "Central Field", "Grassland", 50);
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

        Participant p1 = new Participant("P1", "Bob", 20, "555-0201", "Mom: 555-0000", "None", "Beginner");
        Participant p2 = new Participant("P2", "Charlie", 22, "555-0202", "Dad: 555-0001", "None", "Intermediate");
        Participant p3 = new Participant("P3", "Diana", 19, "555-0203", "Sister: 555-0002", "Asthma", "Advanced");
        camp.getParticipants().add(p1);
        camp.getParticipants().add(p2);
        camp.getParticipants().add(p3);

        Activity a1 = new WaterActivity("A1", "Kayaking Basics", "Learn fundamentals of kayaking", 120, 10, loc1, i1, true, "River");
        Activity a2 = new AdventureActivity("A2", "Rock Climbing", "Introductory rock climbing wall", 90, 8, loc2, i2, "Rock Face", "Moderate");
        Activity a3 = new TeamBuildingActivity("A3", "Rope Course Challenge", "Obstacle course navigation", 60, 15, loc3, i1, 5);
        camp.getActivities().add(a1);
        camp.getActivities().add(a2);
        camp.getActivities().add(a3);

        Equipment eq1 = new SafetyEquipment("E1", "Life Jacket", "Good", LocalDate.now(), "A");
        Equipment eq2 = new SafetyEquipment("E2", "Climbing Helmet", "Good", LocalDate.now(), "A");
        Equipment eq3 = new SportsEquipment("E3", "Kayak Paddle", "Good", "Kayaking");
        Equipment eq4 = new SportsEquipment("E4", "Climbing Harness", "Good", "Climbing");
        camp.getEquipmentList().add(eq1);
        camp.getEquipmentList().add(eq2);
        camp.getEquipmentList().add(eq3);
        camp.getEquipmentList().add(eq4);

        return camp;
    }

    // --- Lightweight Recursive Descent JSON Parser & Formatted Serializer ---
    public static class SimpleJsonParser {
        private final String src;
        private int pos = 0;

        public SimpleJsonParser(String src) {
            this.src = src != null ? src : "";
        }

        public static Object parse(String json) {
            return new SimpleJsonParser(json).parseValue();
        }

        private Object parseValue() {
            skipWhitespace();
            if (pos >= src.length()) return null;
            char c = src.charAt(pos);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (c == 't' || c == 'f') return parseBoolean();
            if (c == 'n') return parseNull();
            if (c == '-' || Character.isDigit(c)) return parseNumber();
            throw new RuntimeException("Unexpected character at " + pos + ": " + c);
        }

        private void skipWhitespace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            pos++; // skip '{'
            skipWhitespace();
            if (pos < src.length() && src.charAt(pos) == '}') {
                pos++;
                return map;
            }
            while (pos < src.length()) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                if (pos < src.length() && src.charAt(pos) == ':') pos++;
                skipWhitespace();
                Object val = parseValue();
                map.put(key, val);
                skipWhitespace();
                if (pos < src.length() && src.charAt(pos) == ',') {
                    pos++;
                } else if (pos < src.length() && src.charAt(pos) == '}') {
                    pos++;
                    break;
                }
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++; // skip '['
            skipWhitespace();
            if (pos < src.length() && src.charAt(pos) == ']') {
                pos++;
                return list;
            }
            while (pos < src.length()) {
                skipWhitespace();
                Object val = parseValue();
                list.add(val);
                skipWhitespace();
                if (pos < src.length() && src.charAt(pos) == ',') {
                    pos++;
                } else if (pos < src.length() && src.charAt(pos) == ']') {
                    pos++;
                    break;
                }
            }
            return list;
        }

        private String parseString() {
            pos++; // skip '"'
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') return sb.toString();
                if (c == '\\' && pos < src.length()) {
                    char next = src.charAt(pos++);
                    if (next == 'n') sb.append('\n');
                    else if (next == 'r') sb.append('\r');
                    else if (next == 't') sb.append('\t');
                    else if (next == '"') sb.append('"');
                    else if (next == '\\') sb.append('\\');
                    else sb.append(next);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private Boolean parseBoolean() {
            if (src.startsWith("true", pos)) {
                pos += 4;
                return true;
            } else if (src.startsWith("false", pos)) {
                pos += 5;
                return false;
            }
            throw new RuntimeException("Invalid boolean at " + pos);
        }

        private Object parseNull() {
            if (src.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw new RuntimeException("Invalid null at " + pos);
        }

        private Number parseNumber() {
            int start = pos;
            if (src.charAt(pos) == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E' || src.charAt(pos) == '+' || src.charAt(pos) == '-')) {
                pos++;
            }
            String numStr = src.substring(start, pos);
            if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
                return Double.parseDouble(numStr);
            } else {
                long val = Long.parseLong(numStr);
                if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                    return (int) val;
                }
                return val;
            }
        }
    }

    public static class SimpleJsonSerializer {
        public static String serialize(Object obj) {
            StringBuilder sb = new StringBuilder();
            serializeValue(obj, sb, 0);
            return sb.toString();
        }

        @SuppressWarnings("unchecked")
        private static void serializeValue(Object obj, StringBuilder sb, int indent) {
            if (obj == null) {
                sb.append("null");
            } else if (obj instanceof String) {
                sb.append('"').append(escapeString((String) obj)).append('"');
            } else if (obj instanceof Boolean || obj instanceof Number) {
                sb.append(obj.toString());
            } else if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                if (map.isEmpty()) {
                    sb.append("{}");
                    return;
                }
                sb.append("{\n");
                int size = map.size();
                int idx = 0;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    indent(sb, indent + 2);
                    sb.append('"').append(escapeString(entry.getKey())).append("\": ");
                    serializeValue(entry.getValue(), sb, indent + 2);
                    if (++idx < size) sb.append(",");
                    sb.append("\n");
                }
                indent(sb, indent);
                sb.append("}");
            } else if (obj instanceof List) {
                List<?> list = (List<?>) obj;
                if (list.isEmpty()) {
                    sb.append("[]");
                    return;
                }
                sb.append("[\n");
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    indent(sb, indent + 2);
                    serializeValue(list.get(i), sb, indent + 2);
                    if (i < size - 1) sb.append(",");
                    sb.append("\n");
                }
                indent(sb, indent);
                sb.append("]");
            } else {
                sb.append('"').append(escapeString(obj.toString())).append('"');
            }
        }

        private static void indent(StringBuilder sb, int spaces) {
            for (int i = 0; i < spaces; i++) sb.append(' ');
        }

        private static String escapeString(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }
    }
}
