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

    @Override
    public void conductActivity() {
        System.out.println("Conducting Water Activity: " + getTitle() + " in " + waterBodyType);
    }
}
