import os

files = {}

# MANAGEMENT ENTITIES
files["src/main/java/management/ScheduleEntry.java"] = """
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
"""

files["src/main/java/management/CampSchedule.java"] = """
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
"""

files["src/main/java/management/Registration.java"] = """
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
"""

files["src/main/java/management/AttendanceRecord.java"] = """
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
"""

files["src/main/java/management/Attendance.java"] = """
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
"""

files["src/main/java/management/EquipmentAllocation.java"] = """
package management;
import equipment.Equipment;
import activities.Activity;
import java.time.LocalDate;

public class EquipmentAllocation {
    private String allocationId;
    private LocalDate allocationDate;
    private LocalDate returnDate;
    private String status;
    private Equipment equipment;
    private Activity activity;

    public EquipmentAllocation(String allocationId, Equipment equipment, Activity activity) {
        this.allocationId = allocationId;
        this.equipment = equipment;
        this.activity = activity;
        this.allocationDate = LocalDate.now();
        this.status = "Allocated";
        equipment.setAvailable(false);
    }
    
    public void returnEquipment() {
        this.status = "Returned";
        this.returnDate = LocalDate.now();
        this.equipment.setAvailable(true);
    }
    
    public Equipment getEquipment() { return equipment; }
    public Activity getActivity() { return activity; }
}
"""

files["src/main/java/management/SafetyCheck.java"] = """
package management;
import java.time.LocalDate;
import equipment.Equipment;
import activities.Activity;

public class SafetyCheck {
    private String checkId;
    private LocalDate checkDate;
    private boolean passed;
    private String remarks;
    private Equipment equipment;
    private Activity activity;

    public SafetyCheck(String checkId, boolean passed, String remarks, Equipment equipment, Activity activity) {
        this.checkId = checkId;
        this.passed = passed;
        this.remarks = remarks;
        this.equipment = equipment;
        this.activity = activity;
        this.checkDate = LocalDate.now();
    }
    
    public boolean isPassed() { return passed; }
    public String getRemarks() { return remarks; }
}
"""

files["src/main/java/management/Feedback.java"] = """
package management;
import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class Feedback {
    private String feedbackId;
    private int rating;
    private String comments;
    private LocalDate submittedDate;
    private Participant participant;
    private Activity activity;

    public Feedback(String feedbackId, int rating, String comments, Participant participant, Activity activity) {
        this.feedbackId = feedbackId;
        this.rating = rating;
        this.comments = comments;
        this.participant = participant;
        this.activity = activity;
        this.submittedDate = LocalDate.now();
    }
    
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public Participant getParticipant() { return participant; }
}
"""

files["src/main/java/management/Camp.java"] = """
package management;

import activities.Activity;
import people.Participant;
import people.Instructor;
import equipment.Equipment;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Camp {
    private String campId;
    private String campName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;
    private String status;

    private CampSchedule schedule = new CampSchedule();
    private Repository<Registration> registrations = new Repository<>();
    private Repository<SafetyCheck> safetyChecks = new Repository<>();
    private Attendance attendance = new Attendance();
    
    // For easy management in CLI
    private Repository<Participant> participants = new Repository<>();
    private Repository<Instructor> instructors = new Repository<>();
    private Repository<Activity> activities = new Repository<>();
    private Repository<Equipment> equipmentList = new Repository<>();
    private Repository<Location> locations = new Repository<>();

    public Camp(String campId, String campName, LocalDate startDate, LocalDate endDate, int capacity) {
        this.campId = campId;
        this.campName = campName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.status = "Active";
    }

    public void registerParticipant(Participant p, Activity a) throws RegistrationException, CapacityExceededException {
        if(registrations.size() >= capacity) {
            throw new CapacityExceededException("Camp capacity reached.");
        }
        if(a.getParticipants().size() >= a.getMaxParticipants()) {
            throw new CapacityExceededException("Activity capacity reached.");
        }
        if(a.getParticipants().contains(p)) {
            throw new RegistrationException("Participant already registered for this activity.");
        }

        Registration r = new Registration("REG" + (registrations.size() + 1), p, a);
        registrations.add(r);
        p.addRegistration(r);
        a.addParticipant(p);
    }

    public void scheduleActivity(Activity a, LocalDateTime time) throws CampException {
        a.updateSchedule(time);
        ScheduleEntry entry = new ScheduleEntry("SCH" + (schedule.getEntries().size() + 1), a, time);
        schedule.addEntry(entry);
    }

    public void allocateEquipment(Equipment e, Activity a) throws EquipmentUnavailableException {
        if(!e.isAvailable()) {
            throw new EquipmentUnavailableException("Equipment " + e.getName() + " is currently unavailable.");
        }
        EquipmentAllocation alloc = new EquipmentAllocation("EA" + (a.getEquipmentAllocations().size() + 1), e, a);
        a.addEquipmentAllocation(alloc);
    }

    public void addSafetyCheck(SafetyCheck check) throws SafetyViolationException {
        safetyChecks.add(check);
        if(!check.isPassed()) {
            throw new SafetyViolationException("Safety check failed: " + check.getRemarks());
        }
    }
    
    public void generateReport() {
        System.out.println("=== CAMP REPORT ===");
        System.out.println("Camp Name: " + campName);
        System.out.println("Total Registrations: " + registrations.size());
        System.out.println("Total Activities: " + activities.size());
        System.out.println("Total Safety Checks: " + safetyChecks.size());
        System.out.println("===================");
    }

    // Getters for CLI Repositories
    public Repository<Participant> getParticipants() { return participants; }
    public Repository<Instructor> getInstructors() { return instructors; }
    public Repository<Activity> getActivities() { return activities; }
    public Repository<Equipment> getEquipmentList() { return equipmentList; }
    public Repository<Location> getLocations() { return locations; }
    public CampSchedule getSchedule() { return schedule; }
    public Attendance getAttendance() { return attendance; }
    public Repository<Registration> getRegistrations() { return registrations; }
}
"""

for path, content in files.items():
    d = os.path.dirname(path)
    if d:
        os.makedirs(d, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content.strip() + "\n")
print("Phase 4 files generated.")
