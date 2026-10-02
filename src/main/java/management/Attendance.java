package management;
import java.util.List;
import java.util.ArrayList;
import activities.Activity;

public class Attendance {
    private Repository<AttendanceRecord> records = new Repository<>();

    public void recordAttendance(AttendanceRecord record) {
        records.add(record);
    }
    
    public List<AttendanceRecord> getRecordsForActivity(Activity activity) {
        List<AttendanceRecord> result = new ArrayList<>();
        for(AttendanceRecord r : records.getAll()) {
            if(r.getActivity().equals(activity)) {
                result.add(r);
            }
        }
        return result;
    }
}
