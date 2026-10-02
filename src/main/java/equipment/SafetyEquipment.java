package equipment;
import java.time.LocalDate;

public class SafetyEquipment extends Equipment {
    private LocalDate lastInspectionDate;
    private String safetyRating;

    public SafetyEquipment(String id, String name, String condition, LocalDate lastInspectionDate, String safetyRating) {
        super(id, name, condition);
        this.lastInspectionDate = lastInspectionDate;
        this.safetyRating = safetyRating;
    }

    @Override
    public void checkCondition() {
        System.out.println("Checking safety equipment " + getName() + ". Last inspection: " + lastInspectionDate);
    }

    public LocalDate getLastInspectionDate() { return lastInspectionDate; }
    public String getSafetyRating() { return safetyRating; }
}
