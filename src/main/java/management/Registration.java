package management;
import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class Registration {
    private String registrationId;
    private LocalDate registrationDate;
    private String status;
    private Participant participant;
    private Activity activity;

    public Registration(String registrationId, Participant participant, Activity activity) {
        this.registrationId = registrationId;
        this.participant = participant;
        this.activity = activity;
        this.registrationDate = LocalDate.now();
        this.status = "Confirmed";
    }

    public Registration(String registrationId, Participant participant, Activity activity, LocalDate registrationDate, String status) {
        this.registrationId = registrationId;
        this.participant = participant;
        this.activity = activity;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getRegistrationId() { return registrationId; }
    public Activity getActivity() { return activity; }
    public Participant getParticipant() { return participant; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getStatus() { return status; }

    public void displayInfo() {
        String pName = participant != null ? participant.getName() : "Unknown";
        String pId = participant != null ? participant.getId() : "Unknown";
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        String aId = activity != null ? activity.getActivityId() : "Unknown";
        System.out.println(String.format("Reg ID: %-5s | Participant: %-12s (%-4s) | Activity: %-22s (%-4s) | Date: %-10s | Status: %s",
                registrationId, pName, pId, aTitle, aId, registrationDate, status));
    }
}
