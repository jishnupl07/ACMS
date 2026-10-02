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
    public String getEmergencyContact() { return emergencyContact; }
    public String getMedicalNotes() { return medicalNotes; }
    public String getSkillLevel() { return skillLevel; }
}
