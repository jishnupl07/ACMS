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

    public String getRegistrationId() { return registrationId; }
    public Activity getActivity() { return activity; }
    public Participant getParticipant() { return participant; }
    public String getStatus() { return status; }
}
