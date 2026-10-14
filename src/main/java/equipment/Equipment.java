package equipment;

import management.CampEntity;

public abstract class Equipment extends CampEntity {
    private boolean isAvailable;
    private String condition;

    public Equipment(String equipmentId, String name, String condition) {
        super(equipmentId, name);
        this.condition = condition;
        this.isAvailable = true;
    }

    public String getEquipmentId() { return getId(); }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public String getCondition() { return condition; }

    // --- Method Overloading: setCondition ---

    public void setCondition(String condition) { 
        this.condition = condition; 
    }

    /** Overloaded setCondition: updates both condition and availability at once. */
    public void setCondition(String condition, boolean available) {
        this.condition = condition;
        this.isAvailable = available;
    }

    // --- Normal Method Overriding (overriding concrete methods from CampEntity) ---

    /**
     * Concrete method providing equipment status summary.
     * Overridden by SafetyEquipment and SportsEquipment.
     */
    public String getStatusSummary() {
        return String.format("Equipment: %s [%s] | Condition: %s | Available: %s",
                getName(), getEquipmentId(), condition, isAvailable);
    }

    @Override
    public String getDetails() {
        return getStatusSummary();
    }

    // --- Abstract Method ---

    public abstract void checkCondition();
}
