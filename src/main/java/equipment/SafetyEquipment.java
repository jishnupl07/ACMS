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

    // --- Normal Method Overriding (overriding concrete method from superclass Equipment) ---

    @Override
    public String getStatusSummary() {
        return super.getStatusSummary() + String.format(" | Rating: %s | Last Inspected: %s", safetyRating, lastInspectionDate);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void checkCondition() {
        System.out.println("Checking safety equipment " + getName() + ". Last inspection: " + lastInspectionDate);
    }

    public LocalDate getLastInspectionDate() { return lastInspectionDate; }
    public void setLastInspectionDate(LocalDate lastInspectionDate) { this.lastInspectionDate = lastInspectionDate; }
    public String getSafetyRating() { return safetyRating; }
    public void setSafetyRating(String safetyRating) { this.safetyRating = safetyRating; }
}
