package cli;

import management.*;
import people.*;
import activities.*;
import equipment.*;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainCLI {
    private static Camp camp;
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_PATH = DataManager.DEFAULT_DB_PATH;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        // Load camp from JSON database
        camp = DataManager.loadCamp(DB_PATH);
        System.out.println("=================================================");
        System.out.println("   Welcome to Adventure Camp Management System   ");
        System.out.println("=================================================");
        System.out.println("Loaded database from: " + DB_PATH);

        boolean running = true;
        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. Dashboard / Summary Report");
            System.out.println("2. Manage Participants");
            System.out.println("3. Manage Instructors");
            System.out.println("4. Manage Locations");
            System.out.println("5. Manage Activities");
            System.out.println("6. Schedule Activity");
            System.out.println("7. Register Participant for Activity");
            System.out.println("8. Manage Equipment & Safety");
            System.out.println("9. Attendance Management");
            System.out.println("10. Feedback Management");
            System.out.println("0. Save & Exit");
            System.out.print("Select an option (0-10): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        camp.generateReport();
                        break;
                    case "2":
                        manageParticipants();
                        break;
                    case "3":
                        manageInstructors();
                        break;
                    case "4":
                        manageLocations();
                        break;
                    case "5":
                        manageActivities();
                        break;
                    case "6":
                        manageScheduling();
                        break;
                    case "7":
                        registerParticipant();
                        break;
                    case "8":
                        manageEquipment();
                        break;
                    case "9":
                        manageAttendance();
                        break;
                    case "10":
                        manageFeedback();
                        break;
                    case "0":
                        running = false;
                        saveDatabase();
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 0 and 10.");
                }
            } catch (CampException e) {
                System.out.println("\n[CAMP ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERROR] An unexpected error occurred: " + e.getMessage());
            }
        }
        System.out.println("\nThank you for using ACMS. Goodbye!");
    }

    private static void saveDatabase() {
        DataManager.saveCamp(camp, DB_PATH);
        System.out.println("All changes successfully saved to " + DB_PATH);
    }

    // --- 2. PARTICIPANTS ---
    private static void manageParticipants() {
        System.out.println("\n=== PARTICIPANTS LIST ===");
        if (camp.getParticipants().size() == 0) {
            System.out.println("No participants found.");
        } else {
            for (Participant p : camp.getParticipants().getAll()) {
                System.out.println(String.format("ID: %-4s | Name: %-12s | Age: %-2d | Contact: %-10s | Emergency: %-14s | Skill: %-12s | MedNotes: %s",
                        p.getId(), p.getName(), p.getAge(), p.getContactNumber(), p.getEmergencyContact(), p.getSkillLevel(), p.getMedicalNotes()));
            }
        }

        System.out.print("\nAdd new participant? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Contact Number: ");
                String contactNumber = scanner.nextLine().trim();
                System.out.print("Emergency Contact: ");
                String emergencyContact = scanner.nextLine().trim();
                System.out.print("Medical Notes: ");
                String medicalNotes = scanner.nextLine().trim();
                System.out.print("Skill Level (Beginner/Intermediate/Advanced): ");
                String skillLevel = scanner.nextLine().trim();

                String pId = "P" + (camp.getParticipants().size() + 1);
                Participant newP = new Participant(pId, name, age, contactNumber, emergencyContact, medicalNotes, skillLevel);
                camp.getParticipants().add(newP);
                saveDatabase();
                System.out.println("Participant added successfully with ID: " + pId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number for age. Participant creation aborted.");
            }
        }
    }

    // --- 3. INSTRUCTORS ---
    private static void manageInstructors() {
        System.out.println("\n=== INSTRUCTORS LIST ===");
        if (camp.getInstructors().size() == 0) {
            System.out.println("No instructors found.");
        } else {
            for (Instructor i : camp.getInstructors().getAll()) {
                System.out.println(String.format("ID: %-4s | Name: %-12s | Age: %-2d | Contact: %-10s | Specialization: %-16s | Certifications: %s",
                        i.getId(), i.getName(), i.getAge(), i.getContactNumber(), i.getSpecialization(), String.join(", ", i.getCertifications())));
            }
        }

        System.out.print("\nAdd new instructor? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Age: ");
                int age = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Contact Number: ");
                String contactNumber = scanner.nextLine().trim();
                System.out.print("Specialization: ");
                String spec = scanner.nextLine().trim();
                System.out.print("Certifications (comma separated): ");
                String certsInput = scanner.nextLine().trim();

                String iId = "I" + (camp.getInstructors().size() + 1);
                Instructor newI = new Instructor(iId, name, age, contactNumber, spec);
                if (!certsInput.isEmpty()) {
                    for (String c : certsInput.split(",")) {
                        newI.getCertifications().add(c.trim());
                    }
                }
                camp.getInstructors().add(newI);
                saveDatabase();
                System.out.println("Instructor added successfully with ID: " + iId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number for age. Instructor creation aborted.");
            }
        }
    }

    // --- 4. LOCATIONS ---
    private static void manageLocations() {
        System.out.println("\n=== LOCATIONS LIST ===");
        if (camp.getLocations().size() == 0) {
            System.out.println("No locations found.");
        } else {
            for (Location loc : camp.getLocations().getAll()) {
                System.out.println(String.format("ID: %-4s | Name: %-16s | Address: %-16s | Terrain: %-12s | Capacity: %d",
                        loc.getLocationId(), loc.getName(), loc.getAddress(), loc.getTerrainType(), loc.getCapacity()));
            }
        }

        System.out.print("\nAdd new location? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                System.out.print("Location Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Address/Zone: ");
                String address = scanner.nextLine().trim();
                System.out.print("Terrain Type (Water/Mountain/Grassland/Forest): ");
                String terrain = scanner.nextLine().trim();
                System.out.print("Capacity: ");
                int cap = Integer.parseInt(scanner.nextLine().trim());

                String lId = "L" + (camp.getLocations().size() + 1);
                Location loc = new Location(lId, name, address, terrain, cap);
                camp.getLocations().add(loc);
                saveDatabase();
                System.out.println("Location added successfully with ID: " + lId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number for capacity. Location creation aborted.");
            }
        }
    }

    // --- 5. ACTIVITIES ---
    private static void manageActivities() {
        System.out.println("\n=== ACTIVITIES LIST ===");
        if (camp.getActivities().size() == 0) {
            System.out.println("No activities found.");
        } else {
            for (Activity a : camp.getActivities().getAll()) {
                String locName = a.getLocation() != null ? a.getLocation().getName() : "None";
                String instName = a.getInstructor() != null ? a.getInstructor().getName() : "None";
                String type = a.getClass().getSimpleName();
                System.out.println(String.format("ID: %-4s | Type: %-20s | Title: %-20s | Duration: %3d min | Max: %2d | Loc: %-12s | Inst: %-10s | Enrolled: %d",
                        a.getActivityId(), type, a.getTitle(), a.getDurationMinutes(), a.getMaxParticipants(), locName, instName, a.getParticipants().size()));
            }
        }

        System.out.print("\nAdd new activity? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                System.out.println("Select Activity Type:");
                System.out.println("1. Adventure Activity (Rock Climbing, Trekking, etc.)");
                System.out.println("2. Water Activity (Kayaking, Rafting, Swimming)");
                System.out.println("3. Team Building Activity (Ropes, Puzzles, Relays)");
                System.out.print("Choice (1-3): ");
                String typeChoice = scanner.nextLine().trim();

                System.out.print("Title: ");
                String title = scanner.nextLine().trim();
                System.out.print("Description: ");
                String desc = scanner.nextLine().trim();
                System.out.print("Duration (minutes): ");
                int duration = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Max Participants: ");
                int maxPart = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter Location ID: ");
                String locId = scanner.nextLine().trim();
                Location loc = findLocation(locId);
                if (loc == null) {
                    System.out.println("Location ID not found. Activity creation aborted.");
                    return;
                }

                System.out.print("Enter Instructor ID: ");
                String instId = scanner.nextLine().trim();
                Instructor inst = findInstructor(instId);
                if (inst == null) {
                    System.out.println("Instructor ID not found. Activity creation aborted.");
                    return;
                }

                String aId = "A" + (camp.getActivities().size() + 1);
                Activity newActivity;

                if (typeChoice.equals("2")) {
                    System.out.print("Is Lifeguard Required? (true/false): ");
                    boolean lifeguard = Boolean.parseBoolean(scanner.nextLine().trim());
                    System.out.print("Water Body Type (River/Lake/Pool/Ocean): ");
                    String waterBody = scanner.nextLine().trim();
                    newActivity = new WaterActivity(aId, title, desc, duration, maxPart, loc, inst, lifeguard, waterBody);
                } else if (typeChoice.equals("3")) {
                    System.out.print("Team Size: ");
                    int teamSize = Integer.parseInt(scanner.nextLine().trim());
                    newActivity = new TeamBuildingActivity(aId, title, desc, duration, maxPart, loc, inst, teamSize);
                } else {
                    System.out.print("Terrain Type: ");
                    String terrain = scanner.nextLine().trim();
                    System.out.print("Risk Level (Low/Moderate/High): ");
                    String risk = scanner.nextLine().trim();
                    newActivity = new AdventureActivity(aId, title, desc, duration, maxPart, loc, inst, terrain, risk);
                }

                camp.getActivities().add(newActivity);
                saveDatabase();
                System.out.println("Activity created successfully with ID: " + aId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Activity creation aborted.");
            }
        }
    }

    // --- 6. SCHEDULING ---
    private static void manageScheduling() throws CampException {
        System.out.println("\n=== CAMP SCHEDULE ===");
        if (camp.getSchedule().getEntries().size() == 0) {
            System.out.println("No scheduled activities.");
        } else {
            for (ScheduleEntry se : camp.getSchedule().getEntries().getAll()) {
                String actTitle = se.getActivity() != null ? se.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("Entry ID: %-5s | Date: %-10s | Start: %-16s | Activity: %s",
                        se.getEntryId(), se.getDate(), se.getStartTime(), actTitle));
            }
        }

        System.out.print("\nSchedule an activity? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("Enter Activity ID: ");
            String aId = scanner.nextLine().trim();
            Activity act = findActivity(aId);
            if (act == null) {
                System.out.println("Activity not found.");
                return;
            }

            System.out.print("Enter Date and Start Time (format: YYYY-MM-DD HH:mm, e.g. 2026-10-05 14:00): ");
            String dtStr = scanner.nextLine().trim();
            try {
                LocalDateTime start = LocalDateTime.parse(dtStr, DATE_TIME_FORMATTER);
                camp.scheduleActivity(act, start);
                saveDatabase();
                System.out.println("Activity scheduled successfully for " + dtStr);
            } catch (Exception e) {
                System.out.println("Invalid date/time format. Please use 'YYYY-MM-DD HH:mm'.");
            }
        }
    }

    // --- 7. REGISTER PARTICIPANT ---
    private static void registerParticipant() throws CampException {
        System.out.println("\n=== ACTIVITY REGISTRATION ===");
        System.out.print("Enter Participant ID (e.g. P1): ");
        String pId = scanner.nextLine().trim();
        Participant part = findParticipant(pId);
        if (part == null) {
            System.out.println("Participant ID not found.");
            return;
        }

        System.out.print("Enter Activity ID (e.g. A1): ");
        String aId = scanner.nextLine().trim();
        Activity act = findActivity(aId);
        if (act == null) {
            System.out.println("Activity ID not found.");
            return;
        }

        camp.registerParticipant(part, act);
        saveDatabase();
        System.out.println("Participant " + part.getName() + " registered for " + act.getTitle() + " successfully!");
    }

    // --- 8. EQUIPMENT & SAFETY ---
    private static void manageEquipment() throws CampException {
        System.out.println("\n=== EQUIPMENT INVENTORY ===");
        if (camp.getEquipmentList().size() == 0) {
            System.out.println("No equipment found.");
        } else {
            for (Equipment eq : camp.getEquipmentList().getAll()) {
                String specific = "";
                if (eq instanceof SafetyEquipment) {
                    SafetyEquipment seq = (SafetyEquipment) eq;
                    specific = "Safety Rating: " + seq.getSafetyRating() + ", Last Insp: " + seq.getLastInspectionDate();
                } else if (eq instanceof SportsEquipment) {
                    SportsEquipment seq = (SportsEquipment) eq;
                    specific = "Sport: " + seq.getSportType();
                }
                System.out.println(String.format("ID: %-4s | Name: %-18s | Condition: %-6s | Available: %-5s | %s",
                        eq.getEquipmentId(), eq.getName(), eq.getCondition(), eq.isAvailable(), specific));
            }
        }

        System.out.println("\nActions:");
        System.out.println("1. Add New Equipment");
        System.out.println("2. Perform Safety Check");
        System.out.println("3. Allocate Equipment to Activity");
        System.out.println("4. Return Equipment");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String eqChoice = scanner.nextLine().trim();

        switch (eqChoice) {
            case "1":
                System.out.println("Equipment Type: 1. SafetyEquipment, 2. SportsEquipment");
                System.out.print("Choice: ");
                String tChoice = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Condition (Good/Fair/Needs Maintenance): ");
                String cond = scanner.nextLine().trim();
                String eId = "E" + (camp.getEquipmentList().size() + 1);

                Equipment newEq;
                if (tChoice.equals("1")) {
                    System.out.print("Safety Rating (A/B/C): ");
                    String rating = scanner.nextLine().trim();
                    newEq = new SafetyEquipment(eId, name, cond, LocalDate.now(), rating);
                } else {
                    System.out.print("Sport Type: ");
                    String sport = scanner.nextLine().trim();
                    newEq = new SportsEquipment(eId, name, cond, sport);
                }
                camp.getEquipmentList().add(newEq);
                saveDatabase();
                System.out.println("Equipment added successfully with ID: " + eId);
                break;

            case "2":
                System.out.print("Enter Equipment ID: ");
                String checkEqId = scanner.nextLine().trim();
                Equipment eqToCheck = findEquipment(checkEqId);
                if (eqToCheck == null) {
                    System.out.println("Equipment not found.");
                    return;
                }
                System.out.print("Did the safety check pass? (true/false): ");
                boolean passed = Boolean.parseBoolean(scanner.nextLine().trim());
                System.out.print("Remarks: ");
                String remarks = scanner.nextLine().trim();

                String scId = "SC" + (camp.getSafetyChecks().size() + 1);
                SafetyCheck check = new SafetyCheck(scId, passed, remarks, eqToCheck, null);
                camp.addSafetyCheck(check);
                saveDatabase();
                System.out.println("Safety check recorded. Status: " + (passed ? "PASSED" : "FAILED"));
                break;

            case "3":
                System.out.print("Enter Equipment ID: ");
                String allocEqId = scanner.nextLine().trim();
                Equipment allocEq = findEquipment(allocEqId);
                if (allocEq == null) {
                    System.out.println("Equipment not found.");
                    return;
                }
                System.out.print("Enter Activity ID: ");
                String allocActId = scanner.nextLine().trim();
                Activity allocAct = findActivity(allocActId);
                if (allocAct == null) {
                    System.out.println("Activity not found.");
                    return;
                }
                camp.allocateEquipment(allocEq, allocAct);
                saveDatabase();
                System.out.println("Equipment " + allocEq.getName() + " allocated to " + allocAct.getTitle() + " successfully!");
                break;

            case "4":
                System.out.print("Enter Equipment ID to return: ");
                String retEqId = scanner.nextLine().trim();
                Equipment retEq = findEquipment(retEqId);
                if (retEq == null) {
                    System.out.println("Equipment not found.");
                    return;
                }
                boolean foundAlloc = false;
                for (Activity a : camp.getActivities().getAll()) {
                    for (EquipmentAllocation ea : a.getEquipmentAllocations()) {
                        if (ea.getEquipment() != null && ea.getEquipment().getEquipmentId().equals(retEq.getEquipmentId()) && "Allocated".equalsIgnoreCase(ea.getStatus())) {
                            ea.returnEquipment();
                            foundAlloc = true;
                        }
                    }
                }
                retEq.setAvailable(true);
                saveDatabase();
                System.out.println("Equipment returned to inventory successfully.");
                break;

            default:
                break;
        }
    }

    // --- 9. ATTENDANCE ---
    private static void manageAttendance() {
        System.out.println("\n=== ATTENDANCE RECORDS ===");
        if (camp.getAttendance().getRecords().size() == 0) {
            System.out.println("No attendance records recorded yet.");
        } else {
            for (AttendanceRecord ar : camp.getAttendance().getRecords().getAll()) {
                String pName = ar.getParticipant() != null ? ar.getParticipant().getName() : "Unknown";
                String aTitle = ar.getActivity() != null ? ar.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("Participant: %-12s | Activity: %-20s | Date: %-10s | Present: %s",
                        pName, aTitle, ar.getDate(), ar.isPresent() ? "YES" : "NO"));
            }
        }

        System.out.print("\nMark attendance for an activity? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.print("Enter Activity ID: ");
            String aId = scanner.nextLine().trim();
            Activity act = findActivity(aId);
            if (act == null) {
                System.out.println("Activity not found.");
                return;
            }

            if (act.getParticipants().isEmpty()) {
                System.out.println("No registered participants for this activity.");
                return;
            }

            System.out.println("\nMarking attendance for " + act.getTitle() + ":");
            for (Participant p : act.getParticipants()) {
                System.out.print("Is " + p.getName() + " (" + p.getId() + ") present? (y/n): ");
                boolean present = scanner.nextLine().trim().equalsIgnoreCase("y");
                AttendanceRecord record = new AttendanceRecord(p, act, LocalDate.now(), present);
                camp.getAttendance().recordAttendance(record);
            }
            saveDatabase();
            System.out.println("Attendance successfully saved.");
        }
    }

    // --- 10. FEEDBACK ---
    private static void manageFeedback() {
        System.out.println("\n=== FEEDBACK & REVIEWS ===");
        if (camp.getFeedbacks().size() == 0) {
            System.out.println("No feedback submitted yet.");
        } else {
            for (Feedback fb : camp.getFeedbacks().getAll()) {
                String pName = fb.getParticipant() != null ? fb.getParticipant().getName() : "Unknown";
                String aTitle = fb.getActivity() != null ? fb.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("ID: %-4s | Rating: %d/5 | Participant: %-10s | Activity: %-16s | Date: %-10s | Comments: %s",
                        fb.getFeedbackId(), fb.getRating(), pName, aTitle, fb.getSubmittedDate(), fb.getComments()));
            }
        }

        System.out.print("\nSubmit new feedback? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            try {
                System.out.print("Enter Participant ID: ");
                String pId = scanner.nextLine().trim();
                Participant part = findParticipant(pId);
                if (part == null) {
                    System.out.println("Participant not found.");
                    return;
                }

                System.out.print("Enter Activity ID: ");
                String aId = scanner.nextLine().trim();
                Activity act = findActivity(aId);
                if (act == null) {
                    System.out.println("Activity not found.");
                    return;
                }

                System.out.print("Enter Rating (1-5): ");
                int rating = Integer.parseInt(scanner.nextLine().trim());
                if (rating < 1 || rating > 5) {
                    System.out.println("Rating must be between 1 and 5.");
                    return;
                }

                System.out.print("Comments: ");
                String comments = scanner.nextLine().trim();

                String fbId = "FB" + (camp.getFeedbacks().size() + 1);
                Feedback feedback = new Feedback(fbId, rating, comments, part, act);
                camp.addFeedback(feedback);
                saveDatabase();
                System.out.println("Thank you! Feedback submitted and saved.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating number.");
            }
        }
    }

    // --- HELPER LOOKUP METHODS ---
    private static Participant findParticipant(String id) {
        for (Participant p : camp.getParticipants().getAll()) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    private static Instructor findInstructor(String id) {
        for (Instructor i : camp.getInstructors().getAll()) {
            if (i.getId().equalsIgnoreCase(id)) return i;
        }
        return null;
    }

    private static Location findLocation(String id) {
        for (Location loc : camp.getLocations().getAll()) {
            if (loc.getLocationId().equalsIgnoreCase(id)) return loc;
        }
        return null;
    }

    private static Activity findActivity(String id) {
        for (Activity a : camp.getActivities().getAll()) {
            if (a.getActivityId().equalsIgnoreCase(id)) return a;
        }
        return null;
    }

    private static Equipment findEquipment(String id) {
        for (Equipment eq : camp.getEquipmentList().getAll()) {
            if (eq.getEquipmentId().equalsIgnoreCase(id)) return eq;
        }
        return null;
    }
}
