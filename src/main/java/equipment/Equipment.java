package equipment;

public abstract class Equipment {
    private String equipmentId;
    private String name;
    private boolean isAvailable;
    private String condition;

    public Equipment(String equipmentId, String name, String condition) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.condition = condition;
        this.isAvailable = true;
    }

    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public abstract void checkCondition();
}
