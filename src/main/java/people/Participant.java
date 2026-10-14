package people;

import management.Registration;
import management.Notifiable;
import java.util.ArrayList;
import java.util.List;

public class Participant extends Person implements Notifiable {
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

    // --- Method Overloading: isRegisteredFor ---

    /** Checks if participant is registered for a given activity object using lambda expression. */
    public boolean isRegisteredFor(activities.Activity activity) {
        return registrations.stream()
                .anyMatch(r -> r.getActivity() != null && r.getActivity().equals(activity));
    }

    /** Overloaded isRegisteredFor: checks if participant is registered for a given activity ID string. */
    public boolean isRegisteredFor(String activityId) {
        return registrations.stream()
                .anyMatch(r -> r.getActivity() != null && r.getActivity().getActivityId().equalsIgnoreCase(activityId));
    }

    // --- Lambda & Stream Operations ---

    /** Returns all activities registered by this participant using lambda map and filter. */
    public List<activities.Activity> getRegisteredActivities() {
        return registrations.stream()
                .map(Registration::getActivity)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
    }

    // --- Normal Method Overriding (overriding concrete methods from superclass) ---

    /** Overrides concrete method getContactInfo() from Person. */
    @Override
    public String getContactInfo() {
        return super.getContactInfo() + " [Participant - Skill: " + skillLevel + ", Emergency: " + emergencyContact + "]";
    }

    /** Overrides concrete method getDetails() from Person / CampEntity. */
    @Override
    public String getDetails() {
        return String.format("[%s] ID: %s | Name: %s | Age: %d | Contact: %s | Skill: %s",
                getClass().getSimpleName(), getId(), getName(), getAge(), getContactNumber(), skillLevel);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void displayInfo() {
        System.out.println("Participant [" + getId() + "] " + getName() + " - Skill: " + skillLevel);
    }

    public String getEmergencyContact() { return emergencyContact; }
    public String getMedicalNotes() { return medicalNotes; }
    public String getSkillLevel() { return skillLevel; }

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification for Participant " + getName() + " (" + getId() + "): " + message);
    }
}
