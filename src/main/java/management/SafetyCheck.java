package management;
import java.time.LocalDate;
import equipment.Equipment;
import activities.Activity;

public class SafetyCheck extends CampEntity {
    private LocalDate checkDate;
    private boolean passed;
    private String remarks;
    private Equipment equipment;
    private Activity activity;

    public SafetyCheck(String checkId, boolean passed, String remarks, Equipment equipment, Activity activity) {
        super(checkId, "SafetyCheck-" + checkId);
        this.passed = passed;
        this.remarks = remarks;
        this.equipment = equipment;
        this.activity = activity;
        this.checkDate = LocalDate.now();
    }

    public SafetyCheck(String checkId, boolean passed, String remarks, Equipment equipment, Activity activity, LocalDate checkDate) {
        super(checkId, "SafetyCheck-" + checkId);
        this.passed = passed;
        this.remarks = remarks;
        this.equipment = equipment;
        this.activity = activity;
        this.checkDate = checkDate;
    }

    public String getCheckId() { return getId(); }
    public LocalDate getCheckDate() { return checkDate; }
    public boolean isPassed() { return passed; }
    public String getRemarks() { return remarks; }
    public Equipment getEquipment() { return equipment; }
    public Activity getActivity() { return activity; }

    // --- Method Overloading: setRemarks ---

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /** Overloaded setRemarks: optionally appends to existing remarks. */
    public void setRemarks(String remarks, boolean append) {
        if (append && this.remarks != null && !this.remarks.isEmpty()) {
            this.remarks = this.remarks + "; " + remarks;
        } else {
            this.remarks = remarks;
        }
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String eqName = equipment != null ? equipment.getName() : "None";
        return String.format("[%s] ID: %s | Equipment: %s | Passed: %s | Remarks: %s",
                getClass().getSimpleName(), getId(), eqName, passed, remarks);
    }
}
