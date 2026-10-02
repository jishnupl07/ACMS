import os

files = {}

files["src/main/java/cli/MainCLI.java"] = """
package cli;

import management.*;
import people.*;
import activities.*;
import equipment.*;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class MainCLI {
    private static Camp camp;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initCamp();
        boolean running = true;
        while (running) {
            System.out.println("\\n--- Adventure Camp Management System ---");
            System.out.println("1. Dashboard / Report");
            System.out.println("2. Manage Participants");
            System.out.println("3. Manage Activities");
            System.out.println("4. Schedule Activity");
            System.out.println("5. Register Participant");
            System.out.println("6. Manage Equipment & Safety");
            System.out.println("7. Mark Attendance");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine();
            
            try {
                switch (choice) {
                    case "1": camp.generateReport(); break;
                    case "2": manageParticipants(); break;
                    case "3": manageActivities(); break;
                    case "4": scheduleActivity(); break;
                    case "5": registerParticipant(); break;
                    case "6": manageEquipment(); break;
                    case "7": markAttendance(); break;
                    case "0": running = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (CampException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        System.out.println("Exiting ACMS...");
    }

    private static void initCamp() {
        camp = new Camp("C1", "Summer Adventure Camp", LocalDate.now(), LocalDate.now().plusDays(7), 100);
        
        // Sample Data
        Location loc1 = new Location("L1", "River Zone", "North Woods", "Water", 20);
        Location loc2 = new Location("L2", "Climbing Wall", "Base Camp", "Mountain", 10);
        camp.getLocations().add(loc1);
        camp.getLocations().add(loc2);

        Instructor i1 = new Instructor("I1", "Alice", 30, "555-0101", "Water Sports");
        camp.getInstructors().add(i1);

        Participant p1 = new Participant("P1", "Bob", 20, "555-0201", "Mom: 555-0000", "None", "Beginner");
        Participant p2 = new Participant("P2", "Charlie", 22, "555-0202", "Dad: 555-0001", "None", "Intermediate");
        camp.getParticipants().add(p1);
        camp.getParticipants().add(p2);

        Activity a1 = new WaterActivity("A1", "Kayaking Basics", 120, 10, loc1, i1, true, "River");
        camp.getActivities().add(a1);

        Equipment eq1 = new SafetyEquipment("E1", "Life Jacket", "Good", LocalDate.now(), "A");
        camp.getEquipmentList().add(eq1);
    }

    private static void manageParticipants() {
        System.out.println("\\n--- Participants ---");
        for (Participant p : camp.getParticipants().getAll()) {
            p.displayInfo();
        }
        System.out.print("Add new participant? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Name: ");
            String name = scanner.nextLine();
            Participant newP = new Participant("P" + (camp.getParticipants().size() + 1), name, 20, "N/A", "N/A", "None", "Beginner");
            camp.getParticipants().add(newP);
            System.out.println("Participant added.");
        }
    }

    private static void manageActivities() {
        System.out.println("\\n--- Activities ---");
        for (Activity a : camp.getActivities().getAll()) {
            System.out.println(a.getActivityId() + " - " + a.getTitle());
        }
    }

    private static void scheduleActivity() throws CampException {
        System.out.print("Enter Activity ID to schedule: ");
        String aId = scanner.nextLine();
        Activity activity = null;
        for (Activity a : camp.getActivities().getAll()) {
            if (a.getActivityId().equals(aId)) activity = a;
        }
        if (activity != null) {
            camp.scheduleActivity(activity, LocalDateTime.now().plusDays(1));
            System.out.println("Activity scheduled for tomorrow.");
        } else {
            System.out.println("Activity not found.");
        }
    }

    private static void registerParticipant() throws CampException {
        System.out.print("Enter Participant ID: ");
        String pId = scanner.nextLine();
        System.out.print("Enter Activity ID: ");
        String aId = scanner.nextLine();
        
        Participant part = null;
        for (Participant p : camp.getParticipants().getAll()) {
            if (p.getId().equals(pId)) part = p;
        }
        
        Activity act = null;
        for (Activity a : camp.getActivities().getAll()) {
            if (a.getActivityId().equals(aId)) act = a;
        }

        if (part != null && act != null) {
            camp.registerParticipant(part, act);
            System.out.println("Registration successful.");
        } else {
            System.out.println("Invalid Participant or Activity.");
        }
    }

    private static void manageEquipment() throws CampException {
        System.out.println("\\n--- Equipment ---");
        for (Equipment e : camp.getEquipmentList().getAll()) {
            System.out.println(e.getEquipmentId() + " - " + e.getName() + " (Available: " + e.isAvailable() + ")");
        }
        System.out.print("Enter Equipment ID to perform safety check: ");
        String eId = scanner.nextLine();
        Equipment eq = null;
        for (Equipment e : camp.getEquipmentList().getAll()) {
            if (e.getEquipmentId().equals(eId)) eq = e;
        }
        if (eq != null) {
            SafetyCheck check = new SafetyCheck("SC1", true, "Looks good", eq, null);
            camp.addSafetyCheck(check);
            System.out.println("Safety check passed.");
        }
    }

    private static void markAttendance() {
        System.out.println("Attendance tracking is available for scheduled activities. (Placeholder)");
    }
}
"""

for path, content in files.items():
    d = os.path.dirname(path)
    if d:
        os.makedirs(d, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content.strip() + "\n")
print("Phase 5 CLI generated.")
