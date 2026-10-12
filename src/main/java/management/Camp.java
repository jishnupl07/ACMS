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
        // Using lambda predicate to verify if participant is already registered
        if(a.getParticipants().stream().anyMatch(participant -> participant.getId().equalsIgnoreCase(p.getId()))) {
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
        if (check.getEquipment() != null) {
            if (check.getEquipment() instanceof equipment.SafetyEquipment) {
                ((equipment.SafetyEquipment) check.getEquipment()).setLastInspectionDate(check.getCheckDate());
            }
            if (!check.isPassed()) {
                check.getEquipment().setAvailable(false);
                check.getEquipment().setCondition("Needs Maintenance");
            }
        }
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

    // --- Lambda & Functional Lookups and Queries ---

    public java.util.Optional<Participant> findParticipant(String id) {
        return participants.findFirst(p -> p.getId().equalsIgnoreCase(id));
    }

    public java.util.Optional<Instructor> findInstructor(String id) {
        return instructors.findFirst(i -> i.getId().equalsIgnoreCase(id));
    }

    public java.util.Optional<Activity> findActivity(String id) {
        return activities.findFirst(a -> a.getActivityId().equalsIgnoreCase(id));
    }

    public java.util.Optional<Location> findLocation(String id) {
        return locations.findFirst(l -> l.getLocationId().equalsIgnoreCase(id));
    }

    public java.util.Optional<Equipment> findEquipment(String id) {
        return equipmentList.findFirst(e -> e.getEquipmentId().equalsIgnoreCase(id));
    }

    /** Returns all available equipment using a lambda method reference predicate. */
    public java.util.List<Equipment> getAvailableEquipment() {
        return equipmentList.filter(Equipment::isAvailable);
    }

    /** Returns all passed safety checks using a lambda method reference predicate. */
    public java.util.List<SafetyCheck> getPassedSafetyChecks() {
        return safetyChecks.filter(SafetyCheck::isPassed);
    }

    /** Returns all failed safety checks using a lambda predicate. */
    public java.util.List<SafetyCheck> getFailedSafetyChecks() {
        return safetyChecks.filter(sc -> !sc.isPassed());
    }

    /** Returns all registrations for a specific participant using a lambda predicate. */
    public java.util.List<Registration> getRegistrationsForParticipant(String participantId) {
        return registrations.filter(r -> r.getParticipant() != null && r.getParticipant().getId().equalsIgnoreCase(participantId));
    }

    /** Returns all registrations for a specific activity using a lambda predicate. */
    public java.util.List<Registration> getRegistrationsForActivity(String activityId) {
        return registrations.filter(r -> r.getActivity() != null && r.getActivity().getActivityId().equalsIgnoreCase(activityId));
    }

    /** Calculates average feedback rating using stream and lambda expression. */
    public double getAverageFeedbackRating() {
        return feedbacks.getAll().stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    /** Sends a notification using the functional interface Notifiable with a lambda. */
    public void notifyStaff(String message, Notifiable notifier) {
        notifier.sendNotification(message);
    }

    /** Broadcasts message to all instructors using a lambda consumer. */
    public void broadcastToInstructors(String message) {
        instructors.forEach(i -> i.sendNotification(message));
    }
    
    public void generateReport() {
        long availableEquip = equipmentList.count(Equipment::isAvailable);
        long passedChecks = safetyChecks.count(SafetyCheck::isPassed);
        double avgRating = getAverageFeedbackRating();

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
        System.out.println("Total Equipment: " + equipmentList.size() + " (" + availableEquip + " available)");
        System.out.println("Total Activities: " + activities.size());
        System.out.println("Scheduled Entries: " + schedule.getEntries().size());
        System.out.println("Total Registrations: " + registrations.size());
        System.out.println("Total Safety Checks: " + safetyChecks.size() + " (" + passedChecks + " passed)");
        System.out.println("Attendance Records: " + attendance.getRecords().size());
        System.out.println("Total Feedbacks: " + feedbacks.size() + String.format(" (Avg Rating: %.1f/5)", avgRating));
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
