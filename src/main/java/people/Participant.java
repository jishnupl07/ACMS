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

    // --- Lambda & Stream Operations ---

    /** Checks if participant is registered for a given activity using lambda expression. */
    public boolean isRegisteredFor(activities.Activity activity) {
        return registrations.stream()
                .anyMatch(r -> r.getActivity() != null && r.getActivity().equals(activity));
    }

    /** Returns all activities registered by this participant using lambda map and filter. */
    public List<activities.Activity> getRegisteredActivities() {
        return registrations.stream()
                .map(Registration::getActivity)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void displayInfo() {
        System.out.println("Participant [" + getId() + "] " + getName() + " - Skill: " + skillLevel);
    }
    public String getEmergencyContact() { return emergencyContact; }
    public String getMedicalNotes() { return medicalNotes; }
    public String getSkillLevel() { return skillLevel; }
}

