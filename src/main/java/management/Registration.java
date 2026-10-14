package management;

import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class Registration extends CampEntity {
    private LocalDate registrationDate;
    private String status;
    private Participant participant;
    private Activity activity;

    public Registration(String registrationId, Participant participant, Activity activity) {
        super(registrationId, "Registration-" + registrationId);
        this.participant = participant;
        this.activity = activity;
        this.registrationDate = LocalDate.now();
        this.status = "Confirmed";
    }

    public Registration(String registrationId, Participant participant, Activity activity, LocalDate registrationDate, String status) {
        super(registrationId, "Registration-" + registrationId);
        this.participant = participant;
        this.activity = activity;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getRegistrationId() { return getId(); }
    public Activity getActivity() { return activity; }
    public Participant getParticipant() { return participant; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getStatus() { return status; }

    // --- Method Overloading: displayInfo ---

    public void displayInfo() {
        displayInfo("");
    }

    /** Overloaded displayInfo: prints registration info with a customizable prefix tag. */
    public void displayInfo(String prefix) {
        String pName = participant != null ? participant.getName() : "Unknown";
        String pId = participant != null ? participant.getId() : "Unknown";
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        String aId = activity != null ? activity.getActivityId() : "Unknown";
        String tag = (prefix != null && !prefix.isEmpty()) ? "[" + prefix + "] " : "";
        System.out.println(String.format("%sReg ID: %-5s | Participant: %-12s (%-4s) | Activity: %-22s (%-4s) | Date: %-10s | Status: %s",
                tag, getId(), pName, pId, aTitle, aId, registrationDate, status));
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String pName = participant != null ? participant.getName() : "Unknown";
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        return String.format("[%s] ID: %s | Participant: %s | Activity: %s | Status: %s",
                getClass().getSimpleName(), getId(), pName, aTitle, status);
    }
}
