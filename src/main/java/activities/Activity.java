package activities;

import management.Schedulable;
import management.Location;
import management.Feedback;
import management.EquipmentAllocation;
import people.Instructor;
import people.Participant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Activity implements Schedulable {
    private String activityId;
    private String title;
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
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.instructor = instructor;
        if(instructor != null) {
            instructor.assignActivity(this);
        }
    }

    public String getActivityId() { return activityId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getMaxParticipants() { return maxParticipants; }
    public Location getLocation() { return location; }
    public Instructor getInstructor() { return instructor; }
    public List<Participant> getParticipants() { return participants; }
    public List<Feedback> getFeedbackList() { return feedbackList; }
    public List<EquipmentAllocation> getEquipmentAllocations() { return equipmentAllocations; }

    public void addParticipant(Participant p) { 
        if (!participants.contains(p)) {
            participants.add(p); 
        }
    }
    public void removeParticipant(Participant p) { participants.remove(p); }
    public void addFeedback(Feedback f) { feedbackList.add(f); }
    public void addEquipmentAllocation(EquipmentAllocation ea) { equipmentAllocations.add(ea); }

    // --- Lambda & Stream Operations ---

    /** Calculates the average feedback rating for this activity using lambda expression. */
    public double getAverageRating() {
        return feedbackList.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    /** Checks whether a participant is enrolled in this activity using lambda predicate. */
    public boolean hasParticipant(String participantId) {
        return participants.stream()
                .anyMatch(p -> p.getId().equalsIgnoreCase(participantId));
    }

    /** Returns currently active equipment allocations using lambda filter. */
    public List<EquipmentAllocation> getActiveAllocations() {
        return equipmentAllocations.stream()
                .filter(ea -> "Allocated".equalsIgnoreCase(ea.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

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

    public abstract void conductActivity();
}

