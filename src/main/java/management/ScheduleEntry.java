package management;
import activities.Activity;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleEntry {
    private String entryId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Activity activity;

    public ScheduleEntry(String entryId, Activity activity, LocalDateTime startTime) {
        this.entryId = entryId;
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = activity.getEndTime();
        this.date = startTime.toLocalDate();
    }
    
    public String getEntryId() { return entryId; }
    public Activity getActivity() { return activity; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDate getDate() { return date; }
}
