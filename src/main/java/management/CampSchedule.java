package management;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampSchedule {
    private Repository<ScheduleEntry> entries = new Repository<>();

    public void addEntry(ScheduleEntry entry) {
        entries.add(entry);
    }
    
    public void removeEntry(ScheduleEntry entry) {
        entries.remove(entry);
    }
    
    public List<ScheduleEntry> getActivitiesForDate(LocalDate date) {
        List<ScheduleEntry> result = new ArrayList<>();
        for(ScheduleEntry e : entries.getAll()) {
            if(e.getDate().equals(date)) {
                result.add(e);
            }
        }
        return result;
    }
    
    public Repository<ScheduleEntry> getEntries() { return entries; }
}
