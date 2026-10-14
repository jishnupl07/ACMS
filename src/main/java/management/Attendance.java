package management;

import java.time.LocalDate;
import java.util.List;
import activities.Activity;
import people.Participant;

public class Attendance {
    private Repository<AttendanceRecord> records = new Repository<>();

    // --- Method Overloading: recordAttendance ---

    public void recordAttendance(AttendanceRecord record) {
        records.add(record);
    }

    /** Overloaded recordAttendance: creates and adds an AttendanceRecord from participant and activity with specific date. */
    public void recordAttendance(Participant participant, Activity activity, LocalDate date, boolean isPresent) {
        records.add(new AttendanceRecord(participant, activity, date, isPresent));
    }

    /** Overloaded recordAttendance: records attendance defaulting to current date. */
    public void recordAttendance(Participant participant, Activity activity, boolean isPresent) {
        recordAttendance(participant, activity, LocalDate.now(), isPresent);
    }
    
    /** Returns all attendance records for a given activity using a lambda predicate. */
    public List<AttendanceRecord> getRecordsForActivity(Activity activity) {
        return records.filter(r -> r.getActivity() != null && r.getActivity().equals(activity));
    }

    /** Returns all attendance records for a specific participant using a lambda predicate. */
    public List<AttendanceRecord> getRecordsForParticipant(Participant participant) {
        return records.filter(r -> r.getParticipant() != null && r.getParticipant().equals(participant));
    }

    /** Returns the count of participants present for an activity using a lambda predicate. */
    public long getPresentCount(Activity activity) {
        return records.count(r -> r.getActivity() != null && r.getActivity().equals(activity) && r.isPresent());
    }

    /** Returns the count of participants absent for an activity using a lambda predicate. */
    public long getAbsentCount(Activity activity) {
        return records.count(r -> r.getActivity() != null && r.getActivity().equals(activity) && !r.isPresent());
    }

    public Repository<AttendanceRecord> getRecords() {
        return records;
    }
}
