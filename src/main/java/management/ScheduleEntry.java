package management;

import activities.Activity;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleEntry extends CampEntity {
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Activity activity;

    public ScheduleEntry(String entryId, Activity activity, LocalDateTime startTime) {
        super(entryId, "ScheduleEntry-" + entryId);
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = activity != null ? activity.getEndTime() : null;
        this.date = startTime != null ? startTime.toLocalDate() : null;
    }

    public String getEntryId() { return getId(); }
    public Activity getActivity() { return activity; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public LocalDate getDate() { return date; }

    // --- Method Overloading: reschedule ---

    public void reschedule(LocalDateTime newStartTime) {
        this.startTime = newStartTime;
        this.date = newStartTime != null ? newStartTime.toLocalDate() : null;
        if (activity != null) {
            activity.updateSchedule(newStartTime);
            this.endTime = activity.getEndTime();
        }
    }

    /** Overloaded reschedule: reschedules using separate date and time components. */
    public void reschedule(LocalDate newDate, int hour, int minute) {
        reschedule(LocalDateTime.of(newDate, java.time.LocalTime.of(hour, minute)));
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        return String.format("[%s] ID: %s | Activity: %s | Start: %s",
                getClass().getSimpleName(), getId(), aTitle, startTime);
    }
}
