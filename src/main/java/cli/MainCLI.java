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
        // Load camp state from CSV files in the data/ directory
        camp = DataManager.loadCamp(DB_PATH);
        System.out.println("=================================================");
        System.out.println("   Welcome to Adventure Camp Management System   ");
        System.out.println("=================================================");
        System.out.println("Data directory: " + DB_PATH);

        boolean running = true;
        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. Dashboard / Summary Report");
            System.out.println("2. Manage Participants");
            System.out.println("3. Manage Instructors");
            System.out.println("4. Manage Locations");
            System.out.println("5. Manage Activities");
            System.out.println("6. Schedule Activity");
            System.out.println("7. Manage Registrations");
            System.out.println("8. Manage Equipment & Safety");
            System.out.println("9. Attendance Management");
            System.out.println("10. Feedback Management");
            System.out.println("11. Send Notifications");
            System.out.println("0. Save & Exit");
            System.out.print("Select an option (0-11): ");

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
                        manageRegistrations();
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
                    case "11":
                        manageNotifications();
                        break;
                    case "0":
                        running = false;
                        saveDatabase();
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 0 and 11.");
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
        System.out.println("All changes saved to CSV files in: " + DB_PATH + "/");
    }

    // --- 2. PARTICIPANTS ---
    private static void manageParticipants() {
        System.out.println("\n=== PARTICIPANTS LIST ===");
        if (camp.getParticipants().size() == 0) {
            System.out.println("No participants found.");
        } else {
            camp.getParticipants().forEach(p ->
                System.out.println(String.format("ID: %-4s | Name: %-12s | Age: %-2d | Contact: %-10s | Emergency: %-14s | Skill: %-12s | MedNotes: %s",
                        p.getId(), p.getName(), p.getAge(), p.getContactNumber(), p.getEmergencyContact(), p.getSkillLevel(), p.getMedicalNotes()))
            );
        }

        System.out.println("\nActions:");
        System.out.println("1. Add New Participant");
        System.out.println("2. Remove Participant");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String pChoice = scanner.nextLine().trim();

        if (pChoice.equals("1") || pChoice.equalsIgnoreCase("y")) {
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
        } else if (pChoice.equals("2")) {
            System.out.print("Enter Participant ID to remove (e.g. P1): ");
            String remId = scanner.nextLine().trim();
            Participant p = findParticipant(remId);
            if (p == null) {
                System.out.println("Participant not found.");
                return;
            }
            System.out.print("Are you sure you want to remove " + p.getName() + " (" + p.getId() + ")? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                if (camp.removeParticipant(remId)) {
                    saveDatabase();
                    System.out.println("Participant " + p.getName() + " (" + remId + ") and all associated registrations were removed successfully.");
                } else {
                    System.out.println("Failed to remove participant.");
                }
            }
        }
    }

    // --- 3. INSTRUCTORS ---
    private static void manageInstructors() {
        System.out.println("\n=== INSTRUCTORS LIST ===");
        if (camp.getInstructors().size() == 0) {
            System.out.println("No instructors found.");
        } else {
            camp.getInstructors().forEach(i ->
                System.out.println(String.format("ID: %-4s | Name: %-12s | Age: %-2d | Contact: %-10s | Specialization: %-16s | Certifications: %s",
                        i.getId(), i.getName(), i.getAge(), i.getContactNumber(), i.getSpecialization(), String.join(", ", i.getCertifications())))
            );
        }

        System.out.println("\nActions:");
        System.out.println("1. Add New Instructor");
        System.out.println("2. Remove Instructor");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String iChoice = scanner.nextLine().trim();

        if (iChoice.equals("1") || iChoice.equalsIgnoreCase("y")) {
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
                    java.util.Arrays.stream(certsInput.split(","))
                            .map(String::trim)
                            .filter(c -> !c.isEmpty())
                            .forEach(newI.getCertifications()::add);
                }
                camp.getInstructors().add(newI);
                saveDatabase();
                System.out.println("Instructor added successfully with ID: " + iId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number for age. Instructor creation aborted.");
            }
        } else if (iChoice.equals("2")) {
            System.out.print("Enter Instructor ID to remove (e.g. I1): ");
            String remId = scanner.nextLine().trim();
            Instructor inst = findInstructor(remId);
            if (inst == null) {
                System.out.println("Instructor not found.");
                return;
            }
            System.out.print("Are you sure you want to remove " + inst.getName() + " (" + inst.getId() + ")? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                if (camp.removeInstructor(remId)) {
                    saveDatabase();
                    System.out.println("Instructor " + inst.getName() + " (" + remId + ") removed successfully.");
                } else {
                    System.out.println("Failed to remove instructor.");
                }
            }
        }
    }

    // --- 4. LOCATIONS ---
    private static void manageLocations() {
        System.out.println("\n=== LOCATIONS LIST ===");
        if (camp.getLocations().size() == 0) {
            System.out.println("No locations found.");
        } else {
            camp.getLocations().forEach(loc ->
                System.out.println(String.format("ID: %-4s | Name: %-16s | Address: %-16s | Terrain: %-12s | Capacity: %d",
                        loc.getLocationId(), loc.getName(), loc.getAddress(), loc.getTerrainType(), loc.getCapacity()))
            );
        }

        System.out.println("\nActions:");
        System.out.println("1. Add New Location");
        System.out.println("2. Remove Location");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String lChoice = scanner.nextLine().trim();

        if (lChoice.equals("1") || lChoice.equalsIgnoreCase("y")) {
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
        } else if (lChoice.equals("2")) {
            System.out.print("Enter Location ID to remove (e.g. L1): ");
            String remId = scanner.nextLine().trim();
            Location loc = findLocation(remId);
            if (loc == null) {
                System.out.println("Location not found.");
                return;
            }
            System.out.print("Are you sure you want to remove " + loc.getName() + " (" + loc.getLocationId() + ")? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                if (camp.removeLocation(remId)) {
                    saveDatabase();
                    System.out.println("Location " + loc.getName() + " (" + remId + ") removed successfully.");
                } else {
                    System.out.println("Failed to remove location.");
                }
            }
        }
    }

    // --- 5. ACTIVITIES ---
    private static void manageActivities() {
        System.out.println("\n=== ACTIVITIES LIST ===");
        if (camp.getActivities().size() == 0) {
            System.out.println("No activities found.");
        } else {
            camp.getActivities().forEach(a -> {
                String locName = a.getLocation() != null ? a.getLocation().getName() : "None";
                String instName = a.getInstructor() != null ? a.getInstructor().getName() : "None";
                String type = a.getClass().getSimpleName();
                System.out.println(String.format("ID: %-4s | Type: %-20s | Title: %-20s | Duration: %3d min | Max: %2d | Loc: %-12s | Inst: %-10s | Enrolled: %d",
                        a.getActivityId(), type, a.getTitle(), a.getDurationMinutes(), a.getMaxParticipants(), locName, instName, a.getParticipants().size()));
            });
        }


        System.out.println("\nActions:");
        System.out.println("1. Add New Activity");
        System.out.println("2. Remove Activity");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String aChoice = scanner.nextLine().trim();

        if (aChoice.equals("1") || aChoice.equalsIgnoreCase("y")) {
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
        } else if (aChoice.equals("2")) {
            System.out.print("Enter Activity ID to remove (e.g. A1): ");
            String remId = scanner.nextLine().trim();
            Activity act = findActivity(remId);
            if (act == null) {
                System.out.println("Activity not found.");
                return;
            }
            System.out.print("Are you sure you want to remove '" + act.getTitle() + "' (" + act.getActivityId() + ")? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                if (camp.removeActivity(remId)) {
                    saveDatabase();
                    System.out.println("Activity '" + act.getTitle() + "' (" + remId + ") and all its registrations/schedule entries were removed successfully.");
                } else {
                    System.out.println("Failed to remove activity.");
                }
            }
        }
    }

    // --- 6. SCHEDULING ---
    private static void manageScheduling() throws CampException {
        System.out.println("\n=== CAMP SCHEDULE ===");
        if (camp.getSchedule().getEntries().size() == 0) {
            System.out.println("No scheduled activities.");
        } else {
            camp.getSchedule().getEntries().forEach(se -> {
                String actTitle = se.getActivity() != null ? se.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("Entry ID: %-5s | Date: %-10s | Start: %-16s | Activity: %s",
                        se.getEntryId(), se.getDate(), se.getStartTime(), actTitle));
            });
        }

        System.out.println("\nActions:");
        System.out.println("1. Schedule an Activity");
        System.out.println("2. Remove Schedule Entry");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String sChoice = scanner.nextLine().trim();

        if (sChoice.equals("1") || sChoice.equalsIgnoreCase("y")) {
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
        } else if (sChoice.equals("2")) {
            System.out.print("Enter Schedule Entry ID to remove (e.g. SCH1): ");
            String remId = scanner.nextLine().trim();
            java.util.Optional<ScheduleEntry> opt = camp.getSchedule().getEntries().findFirst(se -> se.getEntryId().equalsIgnoreCase(remId));
            if (!opt.isPresent()) {
                System.out.println("Schedule entry not found.");
                return;
            }
            camp.getSchedule().removeEntry(opt.get());
            saveDatabase();
            System.out.println("Schedule entry " + remId + " removed successfully.");
        }
    }

    // --- 7. REGISTRATIONS ---
    private static void manageRegistrations() throws CampException {
        displayRegistrations();

        System.out.println("\nActions:");
        System.out.println("1. Register Participant for Activity");
        System.out.println("2. View Registrations by Participant");
        System.out.println("3. View Registrations by Activity");
        System.out.println("4. Cancel / Remove Registration");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String regChoice = scanner.nextLine().trim();

        if (regChoice.equals("1") || regChoice.equalsIgnoreCase("y")) {
            registerParticipant();
        } else if (regChoice.equals("2")) {
            viewRegistrationsByParticipant();
        } else if (regChoice.equals("3")) {
            viewRegistrationsByActivity();
        } else if (regChoice.equals("4")) {
            System.out.print("Enter Registration ID to cancel (e.g. REG1): ");
            String remId = scanner.nextLine().trim();
            if (camp.cancelRegistration(remId)) {
                saveDatabase();
                System.out.println("Registration " + remId + " cancelled and removed successfully.");
            } else {
                System.out.println("Registration ID not found.");
            }
        }
    }

    private static void displayRegistrations() {
        System.out.println("\n=== ACTIVITY REGISTRATIONS ===");
        if (camp.getRegistrations().size() == 0) {
            System.out.println("No registrations found.");
        } else {
            camp.getRegistrations().forEach(Registration::displayInfo);
        }
    }

    private static void viewRegistrationsByParticipant() {
        System.out.print("Enter Participant ID (e.g. P1): ");
        String pId = scanner.nextLine().trim();
        Participant part = findParticipant(pId);
        if (part == null) {
            System.out.println("Participant ID not found.");
            return;
        }
        System.out.println("\n=== REGISTRATIONS FOR " + part.getName() + " (" + part.getId() + ") ===");
        java.util.List<Registration> list = camp.getRegistrationsForParticipant(part.getId());
        if (list.isEmpty()) {
            System.out.println("No registrations found for this participant.");
        } else {
            list.forEach(r -> {
                String aTitle = r.getActivity() != null ? r.getActivity().getTitle() : "Unknown";
                String aId = r.getActivity() != null ? r.getActivity().getActivityId() : "Unknown";
                System.out.println(String.format("Reg ID: %-5s | Activity: %-22s (%-4s) | Date: %-10s | Status: %s",
                        r.getRegistrationId(), aTitle, aId, r.getRegistrationDate(), r.getStatus()));
            });
        }
    }

    private static void viewRegistrationsByActivity() {
        System.out.print("Enter Activity ID (e.g. A1): ");
        String aId = scanner.nextLine().trim();
        Activity act = findActivity(aId);
        if (act == null) {
            System.out.println("Activity ID not found.");
            return;
        }
        System.out.println("\n=== REGISTRATIONS FOR " + act.getTitle() + " (" + act.getActivityId() + ") ===");
        java.util.List<Registration> list = camp.getRegistrationsForActivity(act.getActivityId());
        if (list.isEmpty()) {
            System.out.println("No participants registered for this activity.");
        } else {
            list.forEach(r -> {
                String pName = r.getParticipant() != null ? r.getParticipant().getName() : "Unknown";
                String pId = r.getParticipant() != null ? r.getParticipant().getId() : "Unknown";
                System.out.println(String.format("Reg ID: %-5s | Participant: %-15s (%-4s) | Date: %-10s | Status: %s",
                        r.getRegistrationId(), pName, pId, r.getRegistrationDate(), r.getStatus()));
            });
        }
    }

    private static void registerParticipant() throws CampException {
        System.out.println("\n--- Register Participant ---");
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
            camp.getEquipmentList().forEach(eq -> {
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
            });
        }


        System.out.println("\nActions:");
        System.out.println("1. Add New Equipment");
        System.out.println("2. Perform Safety Check");
        System.out.println("3. Allocate Equipment to Activity");
        System.out.println("4. Return Equipment");
        System.out.println("5. Remove Equipment from Inventory");
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
                try {
                    camp.addSafetyCheck(check);
                    saveDatabase();
                    System.out.println("Safety check recorded. Status: PASSED");
                    System.out.println("Equipment " + eqToCheck.getName() + " (" + eqToCheck.getEquipmentId() + ") marked as AVAILABLE (Condition: Good).");
                } catch (SafetyViolationException e) {
                    saveDatabase();
                    System.out.println("\n[CAMP ERROR] " + e.getMessage());
                    System.out.println("Equipment " + eqToCheck.getName() + " (" + eqToCheck.getEquipmentId() + ") has failed inspection and is now marked UNAVAILABLE (Condition: Needs Maintenance).");
                }
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
                camp.getActivities().forEach(a ->
                    a.getEquipmentAllocations().stream()
                        .filter(ea -> ea.getEquipment() != null &&
                                ea.getEquipment().getEquipmentId().equalsIgnoreCase(retEq.getEquipmentId()) &&
                                "Allocated".equalsIgnoreCase(ea.getStatus()))
                        .forEach(EquipmentAllocation::returnEquipment)
                );
                retEq.setAvailable(true);
                saveDatabase();
                System.out.println("Equipment returned to inventory successfully.");
                break;

            case "5":
                System.out.print("Enter Equipment ID to remove: ");
                String remEqId = scanner.nextLine().trim();
                Equipment remEq = findEquipment(remEqId);
                if (remEq == null) {
                    System.out.println("Equipment not found.");
                    return;
                }
                System.out.print("Are you sure you want to remove " + remEq.getName() + " (" + remEq.getEquipmentId() + ")? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    if (camp.removeEquipment(remEqId)) {
                        saveDatabase();
                        System.out.println("Equipment " + remEq.getName() + " (" + remEqId + ") removed from inventory.");
                    } else {
                        System.out.println("Failed to remove equipment.");
                    }
                }
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
            camp.getAttendance().getRecords().forEach(ar -> {
                String pName = ar.getParticipant() != null ? ar.getParticipant().getName() : "Unknown";
                String aTitle = ar.getActivity() != null ? ar.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("Participant: %-12s | Activity: %-20s | Date: %-10s | Present: %s",
                        pName, aTitle, ar.getDate(), ar.isPresent() ? "YES" : "NO"));
            });
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
            act.getParticipants().forEach(p -> {
                System.out.print("Is " + p.getName() + " (" + p.getId() + ") present? (y/n): ");
                boolean present = scanner.nextLine().trim().equalsIgnoreCase("y");
                AttendanceRecord record = new AttendanceRecord(p, act, LocalDate.now(), present);
                camp.getAttendance().recordAttendance(record);
            });
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
            camp.getFeedbacks().forEach(fb -> {
                String pName = fb.getParticipant() != null ? fb.getParticipant().getName() : "Unknown";
                String aTitle = fb.getActivity() != null ? fb.getActivity().getTitle() : "Unknown";
                System.out.println(String.format("ID: %-4s | Rating: %d/5 | Participant: %-10s | Activity: %-16s | Date: %-10s | Comments: %s",
                        fb.getFeedbackId(), fb.getRating(), pName, aTitle, fb.getSubmittedDate(), fb.getComments()));
            });
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

    // --- 11. NOTIFICATIONS ---
    private static void manageNotifications() {
        System.out.println("\n=== SEND NOTIFICATIONS ===");
        System.out.println("1. Notify All Participants (" + camp.getParticipants().size() + " registered)");
        System.out.println("2. Notify All Instructors (" + camp.getInstructors().size() + " registered)");
        System.out.println("3. Notify Everyone (All Participants & Instructors)");
        System.out.println("4. Notify a Specific Participant");
        System.out.println("5. Notify a Specific Instructor");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choice: ");
        String nChoice = scanner.nextLine().trim();

        switch (nChoice) {
            case "1":
                if (camp.getParticipants().size() == 0) {
                    System.out.println("No participants to notify.");
                    return;
                }
                System.out.print("Enter message for all participants: ");
                String pMsg = scanner.nextLine().trim();
                if (pMsg.isEmpty()) { System.out.println("Empty message. Notification cancelled."); return; }
                System.out.println("\n--- Sending to all participants ---");
                camp.broadcastToParticipants(pMsg);
                System.out.println("Notification sent to " + camp.getParticipants().size() + " participant(s).");
                break;

            case "2":
                if (camp.getInstructors().size() == 0) {
                    System.out.println("No instructors to notify.");
                    return;
                }
                System.out.print("Enter message for all instructors: ");
                String iMsg = scanner.nextLine().trim();
                if (iMsg.isEmpty()) { System.out.println("Empty message. Notification cancelled."); return; }
                System.out.println("\n--- Sending to all instructors ---");
                camp.broadcastToInstructors(iMsg);
                System.out.println("Notification sent to " + camp.getInstructors().size() + " instructor(s).");
                break;

            case "3":
                int total = camp.getParticipants().size() + camp.getInstructors().size();
                if (total == 0) {
                    System.out.println("No participants or instructors to notify.");
                    return;
                }
                System.out.print("Enter message for everyone: ");
                String allMsg = scanner.nextLine().trim();
                if (allMsg.isEmpty()) { System.out.println("Empty message. Notification cancelled."); return; }
                System.out.println("\n--- Sending to all participants & instructors ---");
                camp.broadcastToAll(allMsg);
                System.out.println("Notification sent to " + total + " person(s).");
                break;

            case "4":
                System.out.print("Enter Participant ID (e.g. P1): ");
                String spId = scanner.nextLine().trim();
                Participant sp = findParticipant(spId);
                if (sp == null) {
                    System.out.println("Participant ID not found.");
                    return;
                }
                System.out.print("Enter message for " + sp.getName() + ": ");
                String spMsg = scanner.nextLine().trim();
                if (spMsg.isEmpty()) { System.out.println("Empty message. Notification cancelled."); return; }
                camp.notifyStaff(spMsg, sp);
                System.out.println("Notification sent to " + sp.getName() + " (" + sp.getId() + ").");
                break;

            case "5":
                System.out.print("Enter Instructor ID (e.g. I1): ");
                String siId = scanner.nextLine().trim();
                Instructor si = findInstructor(siId);
                if (si == null) {
                    System.out.println("Instructor ID not found.");
                    return;
                }
                System.out.print("Enter message for " + si.getName() + ": ");
                String siMsg = scanner.nextLine().trim();
                if (siMsg.isEmpty()) { System.out.println("Empty message. Notification cancelled."); return; }
                camp.notifyStaff(siMsg, si);
                System.out.println("Notification sent to " + si.getName() + " (" + si.getId() + ").");
                break;

            default:
                break;
        }
    }

    // --- HELPER LOOKUP METHODS (Using Lambdas) ---
    private static Participant findParticipant(String id) {
        return camp.findParticipant(id).orElse(null);
    }

    private static Instructor findInstructor(String id) {
        return camp.findInstructor(id).orElse(null);
    }

    private static Location findLocation(String id) {
        return camp.findLocation(id).orElse(null);
    }

    private static Activity findActivity(String id) {
        return camp.findActivity(id).orElse(null);
    }

    private static Equipment findEquipment(String id) {
        return camp.findEquipment(id).orElse(null);
    }
}

