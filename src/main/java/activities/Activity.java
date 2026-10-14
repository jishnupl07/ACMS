package activities;

import management.CampEntity;
import management.Schedulable;
import management.Location;
import management.Feedback;
import management.EquipmentAllocation;
import people.Instructor;
import people.Participant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Activity extends CampEntity implements Schedulable {
    private String description;
    private int durationMinutes;
    private int maxParticipants;
    private LocalDateTime startTime;
    private Location location;
    private Instructor instructor;
    
    private List<Participant> participants = new ArrayList<>();
    private List<Feedback> feedbackList = new ArrayList<>();
    private List<EquipmentAllocation> equipmentAllocations = new ArrayList<>();

    public Activity(String activityId, String title, int durationMinutes, int maxParticipants, Location location, Instructor instructor) {
        this(activityId, title, "", durationMinutes, maxParticipants, location, instructor);
    }

    public Activity(String activityId, String title, String description, int durationMinutes, int maxParticipants, Location location, Instructor instructor) {
        super(activityId, title);
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.instructor = instructor;
        if(instructor != null) {
            instructor.assignActivity(this);
        }
    }

    public String getActivityId() { return getId(); }
    public String getTitle() { return getName(); }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getMaxParticipants() { return maxParticipants; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public List<Participant> getParticipants() { return participants; }
    public List<Feedback> getFeedbackList() { return feedbackList; }
    public List<EquipmentAllocation> getEquipmentAllocations() { return equipmentAllocations; }

    // --- Method Overloading: addParticipant ---

    public void addParticipant(Participant p) { 
        if (!participants.contains(p)) {
            participants.add(p); 
        }
    }

    /** Overloaded addParticipant: adds a list of participants at once. */
    public void addParticipant(List<Participant> newParticipants) {
        for (Participant p : newParticipants) {
            addParticipant(p);
        }
    }

    public void removeParticipant(Participant p) { participants.remove(p); }
    public void addFeedback(Feedback f) { feedbackList.add(f); }
    public void addEquipmentAllocation(EquipmentAllocation ea) { equipmentAllocations.add(ea); }

    // --- Method Overloading: hasParticipant ---

    /** Checks whether a participant is enrolled in this activity by their ID string. */
    public boolean hasParticipant(String participantId) {
        return participants.stream()
                .anyMatch(p -> p.getId().equalsIgnoreCase(participantId));
    }

    /** Overloaded hasParticipant: checks whether a participant is enrolled given a Participant object. */
    public boolean hasParticipant(Participant participant) {
        return participant != null && hasParticipant(participant.getId());
    }

    // --- Lambda & Stream Operations ---

    /** Calculates the average feedback rating for this activity using lambda expression. */
    public double getAverageRating() {
        return feedbackList.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    /** Returns currently active equipment allocations using lambda filter. */
    public List<EquipmentAllocation> getActiveAllocations() {
        return equipmentAllocations.stream()
                .filter(ea -> "Allocated".equalsIgnoreCase(ea.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    // --- Normal Method Overriding (overriding concrete methods from CampEntity) ---

    /**
     * Concrete method providing activity details.
     * Overridden by subclasses (AdventureActivity, WaterActivity, TeamBuildingActivity)
     * to demonstrate normal method overriding of non-abstract methods.
     */
    public String getActivityDetails() {
        return String.format("Activity [%s] %s (%d mins, Max: %d)", getId(), getTitle(), durationMinutes, maxParticipants);
    }

    @Override
    public String getDetails() {
        return getActivityDetails();
    }

    // --- Interface Schedulable Implementation ---

    @Override
    public LocalDateTime getStartTime() { return startTime; }
    
    @Override
    public LocalDateTime getEndTime() { 
        return startTime != null ? startTime.plusMinutes(durationMinutes) : null; 
    }
    
    @Override
    public void updateSchedule(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // --- Abstract Method ---

    public abstract void conductActivity();
}
