package management;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import activities.Activity;

public class CampSchedule {
    private Repository<ScheduleEntry> entries = new Repository<>();

    public void addEntry(ScheduleEntry entry) {
        entries.add(entry);
    }
    
    public void removeEntry(ScheduleEntry entry) {
        entries.remove(entry);
    }
    
    /** Returns all schedule entries for a given date using a lambda predicate. */
    public List<ScheduleEntry> getActivitiesForDate(LocalDate date) {
        return entries.filter(e -> e.getDate() != null && e.getDate().equals(date));
    }

    /** Returns all schedule entries for a specific activity using a lambda predicate. */
    public List<ScheduleEntry> getEntriesForActivity(Activity activity) {
        return entries.filter(e -> e.getActivity() != null && e.getActivity().equals(activity));
    }

    /** Returns all schedule entries sorted chronologically using a lambda comparator. */
    public List<ScheduleEntry> getEntriesSortedByTime() {
        List<ScheduleEntry> sorted = entries.getAll();
        sorted.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
        return sorted;
    }

    /** Checks whether a new time slot conflicts with an existing schedule entry using a lambda predicate. */
    public boolean hasConflict(LocalDateTime start, LocalDateTime end) {
        return entries.anyMatch(e -> {
            LocalDateTime eStart = e.getStartTime();
            LocalDateTime eEnd = e.getActivity() != null && e.getActivity().getEndTime() != null
                    ? e.getActivity().getEndTime()
                    : eStart.plusHours(1);
            return start.isBefore(eEnd) && end.isAfter(eStart);
        });
    }
    
    public Repository<ScheduleEntry> getEntries() { return entries; }
}

