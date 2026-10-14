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

    // --- Method Overloading: assignActivity ---

    public void assignActivity(Activity a) { 
        assignedActivities.add(a); 
    }

    /** Overloaded assignActivity: assigns multiple activities at once. */
    public void assignActivity(List<Activity> activities) { 
        assignedActivities.addAll(activities); 
    }

    public void removeActivity(Activity a) { assignedActivities.remove(a); }
    public List<Activity> getAssignedActivities() { return assignedActivities; }
    public boolean isAvailable() { return isAvailable; }

    // --- Method Overloading: hasCertification ---

    /** Checks if the instructor has a certification (case-insensitive by default). */
    public boolean hasCertification(String certName) {
        return hasCertification(certName, false);
    }

    /** Overloaded hasCertification: allows configuring case-sensitivity. */
    public boolean hasCertification(String certName, boolean caseSensitive) {
        return certifications.stream()
                .anyMatch(c -> caseSensitive ? c.equals(certName) : c.equalsIgnoreCase(certName));
    }

    // --- Lambda & Stream Operations ---

    /** Returns titles of all activities assigned to this instructor using lambda mapping. */
    public List<String> getAssignedActivityTitles() {
        return assignedActivities.stream()
                .map(Activity::getTitle)
                .collect(java.util.stream.Collectors.toList());
    }

    // --- Normal Method Overriding (overriding concrete methods from superclass) ---

    /** Overrides concrete method getContactInfo() from Person. */
    @Override
    public String getContactInfo() {
        return super.getContactInfo() + " [Instructor - Specialization: " + specialization + "]";
    }

    /** Overrides concrete method getDetails() from Person / CampEntity. */
    @Override
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s | Age: %d | Specialization: %s",
                getClass().getSimpleName(), getId(), getName(), getAge(), specialization);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void displayInfo() {
        System.out.println("Instructor [" + getId() + "] " + getName() + " - Specialization: " + specialization);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Instructor " + getName() + ": " + message);
    }

    public java.util.List<String> getCertifications() { return certifications; }
}
