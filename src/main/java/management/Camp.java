package management;

import activities.Activity;
import people.Participant;
import people.Instructor;
import equipment.Equipment;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Camp {
    private String campId;
    private String campName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;
    private String status;

    private CampSchedule schedule = new CampSchedule();
    private Repository<Registration> registrations = new Repository<>();
    private Repository<SafetyCheck> safetyChecks = new Repository<>();
    private Attendance attendance = new Attendance();
    
    // For easy management in CLI
    private Repository<Participant> participants = new Repository<>();
    private Repository<Instructor> instructors = new Repository<>();
    private Repository<Activity> activities = new Repository<>();
    private Repository<Equipment> equipmentList = new Repository<>();
    private Repository<Location> locations = new Repository<>();

    public Camp(String campId, String campName, LocalDate startDate, LocalDate endDate, int capacity) {
        this.campId = campId;
        this.campName = campName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.status = "Active";
    }

    public void registerParticipant(Participant p, Activity a) throws RegistrationException, CapacityExceededException {
        if(registrations.size() >= capacity) {
            throw new CapacityExceededException("Camp capacity reached.");
        }
        if(a.getParticipants().size() >= a.getMaxParticipants()) {
            throw new CapacityExceededException("Activity capacity reached.");
        }
        if(a.getParticipants().contains(p)) {
            throw new RegistrationException("Participant already registered for this activity.");
        }

        Registration r = new Registration("REG" + (registrations.size() + 1), p, a);
        registrations.add(r);
        p.addRegistration(r);
        a.addParticipant(p);
    }

    public void scheduleActivity(Activity a, LocalDateTime time) throws CampException {
        a.updateSchedule(time);
        ScheduleEntry entry = new ScheduleEntry("SCH" + (schedule.getEntries().size() + 1), a, time);
        schedule.addEntry(entry);
    }

    public void allocateEquipment(Equipment e, Activity a) throws EquipmentUnavailableException {
        if(!e.isAvailable()) {
            throw new EquipmentUnavailableException("Equipment " + e.getName() + " is currently unavailable.");
        }
        EquipmentAllocation alloc = new EquipmentAllocation("EA" + (a.getEquipmentAllocations().size() + 1), e, a);
        a.addEquipmentAllocation(alloc);
    }

    public void addSafetyCheck(SafetyCheck check) throws SafetyViolationException {
        safetyChecks.add(check);
        if(!check.isPassed()) {
            throw new SafetyViolationException("Safety check failed: " + check.getRemarks());
        }
    }
    
    public void generateReport() {
        System.out.println("=== CAMP REPORT ===");
        System.out.println("Camp Name: " + campName);
        System.out.println("Total Registrations: " + registrations.size());
        System.out.println("Total Activities: " + activities.size());
        System.out.println("Total Safety Checks: " + safetyChecks.size());
        System.out.println("===================");
    }

    // Getters for CLI Repositories
    public Repository<Participant> getParticipants() { return participants; }
    public Repository<Instructor> getInstructors() { return instructors; }
    public Repository<Activity> getActivities() { return activities; }
    public Repository<Equipment> getEquipmentList() { return equipmentList; }
    public Repository<Location> getLocations() { return locations; }
    public CampSchedule getSchedule() { return schedule; }
    public Attendance getAttendance() { return attendance; }
    public Repository<Registration> getRegistrations() { return registrations; }
}
