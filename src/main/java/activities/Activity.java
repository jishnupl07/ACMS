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
        this.activityId = activityId;
        this.title = title;
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
    public int getMaxParticipants() { return maxParticipants; }
    public Location getLocation() { return location; }
    public Instructor getInstructor() { return instructor; }
    public List<Participant> getParticipants() { return participants; }
    public List<EquipmentAllocation> getEquipmentAllocations() { return equipmentAllocations; }

    public void addParticipant(Participant p) { participants.add(p); }
    public void removeParticipant(Participant p) { participants.remove(p); }
    public void addFeedback(Feedback f) { feedbackList.add(f); }
    public void addEquipmentAllocation(EquipmentAllocation ea) { equipmentAllocations.add(ea); }

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
