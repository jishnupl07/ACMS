package management;
import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class AttendanceRecord {
    private Participant participant;
    private Activity activity;
    private LocalDate date;
    private boolean isPresent;

    public AttendanceRecord(Participant participant, Activity activity, LocalDate date, boolean isPresent) {
        this.participant = participant;
        this.activity = activity;
        this.date = date;
        this.isPresent = isPresent;
    }
    
    public Participant getParticipant() { return participant; }
    public Activity getActivity() { return activity; }
    public boolean isPresent() { return isPresent; }
}
