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
    private Repository<Feedback> feedbacks = new Repository<>();
    private Attendance attendance = new Attendance();
    
    // Repositories for system entities
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

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        if (feedback.getActivity() != null) {
            feedback.getActivity().addFeedback(feedback);
        }
    }
    
    public void generateReport() {
        System.out.println("\n==========================================");
        System.out.println("              CAMP REPORT                 ");
        System.out.println("==========================================");
        System.out.println("Camp ID: " + campId + " | Name: " + campName);
        System.out.println("Dates: " + startDate + " to " + endDate);
        System.out.println("Capacity: " + capacity + " | Status: " + status);
        System.out.println("------------------------------------------");
        System.out.println("Total Locations: " + locations.size());
        System.out.println("Total Instructors: " + instructors.size());
        System.out.println("Total Participants: " + participants.size());
        System.out.println("Total Equipment: " + equipmentList.size());
        System.out.println("Total Activities: " + activities.size());
        System.out.println("Scheduled Entries: " + schedule.getEntries().size());
        System.out.println("Total Registrations: " + registrations.size());
        System.out.println("Total Safety Checks: " + safetyChecks.size());
        System.out.println("Attendance Records: " + attendance.getRecords().size());
        System.out.println("Total Feedbacks: " + feedbacks.size());
        System.out.println("==========================================\n");
    }

    // Getters and Setters
    public String getCampId() { return campId; }
    public String getCampName() { return campName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getCapacity() { return capacity; }
    public String getStatus() { return status; }
    
    public Repository<Participant> getParticipants() { return participants; }
    public Repository<Instructor> getInstructors() { return instructors; }
    public Repository<Activity> getActivities() { return activities; }
    public Repository<Equipment> getEquipmentList() { return equipmentList; }
    public Repository<Location> getLocations() { return locations; }
    public CampSchedule getSchedule() { return schedule; }
    public Attendance getAttendance() { return attendance; }
    public Repository<Registration> getRegistrations() { return registrations; }
    public Repository<SafetyCheck> getSafetyChecks() { return safetyChecks; }
    public Repository<Feedback> getFeedbacks() { return feedbacks; }
}
