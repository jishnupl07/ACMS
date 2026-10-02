package equipment;

public class SportsEquipment extends Equipment {
    private String sportType;

    public SportsEquipment(String id, String name, String condition, String sportType) {
        super(id, name, condition);
        this.sportType = sportType;
    }

    @Override
    public void checkCondition() {
        System.out.println("Checking sports equipment " + getName() + " for sport: " + sportType);
    }
}
