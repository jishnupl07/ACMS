package management;

import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class AttendanceRecord extends CampEntity {
    private Participant participant;
    private Activity activity;
    private LocalDate date;
    private boolean isPresent;

    public AttendanceRecord(Participant participant, Activity activity, LocalDate date, boolean isPresent) {
        this("ATT-" + (participant != null ? participant.getId() : "P") + "-" +
             (activity != null ? activity.getActivityId() : "A") + "-" + date,
             participant, activity, date, isPresent);
    }

    public AttendanceRecord(String recordId, Participant participant, Activity activity, LocalDate date, boolean isPresent) {
        super(recordId, "Attendance-" + (participant != null ? participant.getName() : "Unknown"));
        this.participant = participant;
        this.activity = activity;
        this.date = date;
        this.isPresent = isPresent;
    }

    public String getRecordId() { return getId(); }
    public Participant getParticipant() { return participant; }
    public Activity getActivity() { return activity; }
    public LocalDate getDate() { return date; }
    public boolean isPresent() { return isPresent; }

    // --- Method Overloading: markAttendance ---

    public void markAttendance(boolean isPresent) {
        this.isPresent = isPresent;
    }

    /** Overloaded markAttendance: updates presence and attendance date simultaneously. */
    public void markAttendance(boolean isPresent, LocalDate date) {
        this.isPresent = isPresent;
        this.date = date;
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String pName = participant != null ? participant.getName() : "Unknown";
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        return String.format("[%s] ID: %s | Participant: %s | Activity: %s | Date: %s | Present: %s",
                getClass().getSimpleName(), getId(), pName, aTitle, date, isPresent);
    }
}
