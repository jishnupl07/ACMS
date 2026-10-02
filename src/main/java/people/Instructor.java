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
