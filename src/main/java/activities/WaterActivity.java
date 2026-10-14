package activities;
import management.Location;
import people.Instructor;

public class WaterActivity extends Activity {
    private boolean lifeguardRequired;
    private String waterBodyType;

    public WaterActivity(String id, String title, int duration, int max, Location loc, Instructor inst, boolean lifeguard, String waterBody) {
        super(id, title, duration, max, loc, inst);
        this.lifeguardRequired = lifeguard;
        this.waterBodyType = waterBody;
    }

    public WaterActivity(String id, String title, String desc, int duration, int max, Location loc, Instructor inst, boolean lifeguard, String waterBody) {
        super(id, title, desc, duration, max, loc, inst);
        this.lifeguardRequired = lifeguard;
        this.waterBodyType = waterBody;
    }

    // --- Normal Method Overriding (overriding concrete method from superclass Activity) ---

    @Override
    public String getActivityDetails() {
        return super.getActivityDetails() + String.format(" [Water: %s, Lifeguard Required: %s]", waterBodyType, lifeguardRequired);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void conductActivity() {
        System.out.println("Conducting Water Activity: " + getTitle() + " in " + waterBodyType);
    }

    public boolean isLifeguardRequired() { return lifeguardRequired; }
    public String getWaterBodyType() { return waterBodyType; }
}
