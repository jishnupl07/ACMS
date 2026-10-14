package equipment;

public class SportsEquipment extends Equipment {
    private String sportType;

    public SportsEquipment(String id, String name, String condition, String sportType) {
        super(id, name, condition);
        this.sportType = sportType;
    }

    // --- Normal Method Overriding (overriding concrete method from superclass Equipment) ---

    @Override
    public String getStatusSummary() {
        return super.getStatusSummary() + String.format(" | Sport: %s", sportType);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void checkCondition() {
        System.out.println("Checking sports equipment " + getName() + " for sport: " + sportType);
    }

    public String getSportType() { return sportType; }
}
