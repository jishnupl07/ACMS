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
