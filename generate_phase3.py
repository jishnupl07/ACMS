import os

files = {}

# PEOPLE
files["src/main/java/people/Person.java"] = """
package people;

public abstract class Person {
    private String id;
    private String name;
    private int age;
    private String contactNumber;

    public Person(String id, String name, int age, String contactNumber) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public abstract void displayInfo();
}
"""
files["src/main/java/people/Participant.java"] = """
package people;

import management.Registration;
import java.util.ArrayList;
import java.util.List;

public class Participant extends Person {
    private String emergencyContact;
    private String medicalNotes;
    private String skillLevel;
    private List<Registration> registrations = new ArrayList<>();

    public Participant(String id, String name, int age, String contactNumber, String emergencyContact, String medicalNotes, String skillLevel) {
        super(id, name, age, contactNumber);
        this.emergencyContact = emergencyContact;
        this.medicalNotes = medicalNotes;
        this.skillLevel = skillLevel;
    }

    public List<Registration> getRegistrations() { return registrations; }
    public void addRegistration(Registration r) { registrations.add(r); }
    public void removeRegistration(Registration r) { registrations.remove(r); }

    @Override
    public void displayInfo() {
        System.out.println("Participant [" + getId() + "] " + getName() + " - Skill: " + skillLevel);
    }
}
"""
files["src/main/java/people/Instructor.java"] = """
package people;

import management.Notifiable;
import activities.Activity;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person implements Notifiable {
    private String specialization;
    private List<String> certifications;
    private boolean isAvailable;
    private List<Activity> assignedActivities = new ArrayList<>();

    public Instructor(String id, String name, int age, String contactNumber, String specialization) {
        super(id, name, age, contactNumber);
        this.specialization = specialization;
        this.certifications = new ArrayList<>();
        this.isAvailable = true;
    }

    public String getSpecialization() { return specialization; }
    public void assignActivity(Activity a) { assignedActivities.add(a); }
    public void removeActivity(Activity a) { assignedActivities.remove(a); }
    public List<Activity> getAssignedActivities() { return assignedActivities; }
    public boolean isAvailable() { return isAvailable; }

    @Override
    public void displayInfo() {
        System.out.println("Instructor [" + getId() + "] " + getName() + " - Specialization: " + specialization);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Instructor " + getName() + ": " + message);
    }
}
"""
files["src/main/java/people/AdminStaff.java"] = """
package people;

import management.Notifiable;

public class AdminStaff extends Person implements Notifiable {
    private String role;

    public AdminStaff(String id, String name, int age, String contactNumber, String role) {
        super(id, name, age, contactNumber);
        this.role = role;
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin [" + getId() + "] " + getName() + " - Role: " + role);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Admin " + getName() + ": " + message);
    }
}
"""

# LOCATION
files["src/main/java/management/Location.java"] = """
package management;

public class Location {
    private String locationId;
    private String name;
    private String address;
    private String terrainType;
    private int capacity;

    public Location(String locationId, String name, String address, String terrainType, int capacity) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.terrainType = terrainType;
        this.capacity = capacity;
    }

    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    
    public void displayDetails() {
        System.out.println("Location: " + name + " (" + capacity + " max)");
    }
}
"""

# ACTIVITIES
files["src/main/java/activities/Activity.java"] = """
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
"""
files["src/main/java/activities/AdventureActivity.java"] = """
package activities;
import management.Location;
import people.Instructor;

public class AdventureActivity extends Activity {
    private String terrainType;
    private String riskLevel;

    public AdventureActivity(String id, String title, int duration, int max, Location loc, Instructor inst, String terrain, String risk) {
        super(id, title, duration, max, loc, inst);
        this.terrainType = terrain;
        this.riskLevel = risk;
    }

    @Override
    public void conductActivity() {
        System.out.println("Conducting Adventure Activity: " + getTitle() + " at Risk Level: " + riskLevel);
    }
}
"""
files["src/main/java/activities/WaterActivity.java"] = """
package activities;
import management.Location;
import people.Instructor;

public class WaterActivity extends Activity {
    private boolean lifeguardRequired;
    private String waterBodyType;

    public WaterActivity(String id, String title, int duration, int max, Location loc, Instructor inst, boolean lifeguard, String waterBody) {
        super(id, title, duration, max, loc, inst);
        this.lifeguardRequired = lifeguard;
        this.waterBodyType = waterBody;
    }

    @Override
    public void conductActivity() {
        System.out.println("Conducting Water Activity: " + getTitle() + " in " + waterBodyType);
    }
}
"""
files["src/main/java/activities/TeamBuildingActivity.java"] = """
package activities;
import management.Location;
import people.Instructor;

public class TeamBuildingActivity extends Activity {
    private int teamSize;

    public TeamBuildingActivity(String id, String title, int duration, int max, Location loc, Instructor inst, int teamSize) {
        super(id, title, duration, max, loc, inst);
        this.teamSize = teamSize;
    }

    @Override
    public void conductActivity() {
        System.out.println("Conducting Team Building Activity: " + getTitle() + " with teams of " + teamSize);
    }
}
"""

# EQUIPMENT
files["src/main/java/equipment/Equipment.java"] = """
package equipment;

public abstract class Equipment {
    private String equipmentId;
    private String name;
    private boolean isAvailable;
    private String condition;

    public Equipment(String equipmentId, String name, String condition) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.condition = condition;
        this.isAvailable = true;
    }

    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public abstract void checkCondition();
}
"""
files["src/main/java/equipment/SafetyEquipment.java"] = """
package equipment;
import java.time.LocalDate;

public class SafetyEquipment extends Equipment {
    private LocalDate lastInspectionDate;
    private String safetyRating;

    public SafetyEquipment(String id, String name, String condition, LocalDate lastInspectionDate, String safetyRating) {
        super(id, name, condition);
        this.lastInspectionDate = lastInspectionDate;
        this.safetyRating = safetyRating;
    }

    @Override
    public void checkCondition() {
        System.out.println("Checking safety equipment " + getName() + ". Last inspection: " + lastInspectionDate);
    }
}
"""
files["src/main/java/equipment/SportsEquipment.java"] = """
package equipment;

public class SportsEquipment extends Equipment {
    private String sportType;

    public SportsEquipment(String id, String name, String condition, String sportType) {
        super(id, name, condition);
        this.sportType = sportType;
    }

    @Override
    public void checkCondition() {
        System.out.println("Checking sports equipment " + getName() + " for sport: " + sportType);
    }
}
"""

for path, content in files.items():
    d = os.path.dirname(path)
    if d:
        os.makedirs(d, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content.strip() + "\n")
print("Phase 3 files generated.")
