package activities;
import management.Location;
import people.Instructor;

public class AdventureActivity extends Activity {
    private String terrainType;
    private String riskLevel;

    public AdventureActivity(String id, String title, int duration, int max, Location loc, Instructor inst, String terrain, String risk) {
        super(id, title, duration, max, loc, inst);
        this.terrainType = terrain;
        this.riskLevel = risk;
    }

    public AdventureActivity(String id, String title, String desc, int duration, int max, Location loc, Instructor inst, String terrain, String risk) {
        super(id, title, desc, duration, max, loc, inst);
        this.terrainType = terrain;
        this.riskLevel = risk;
    }

    // --- Normal Method Overriding (overriding concrete method from superclass Activity) ---

    @Override
    public String getActivityDetails() {
        return super.getActivityDetails() + String.format(" [Terrain: %s, Risk: %s]", terrainType, riskLevel);
    }

    // --- Abstract Method Implementation ---

    @Override
    public void conductActivity() {
        System.out.println("Conducting Adventure Activity: " + getTitle() + " at Risk Level: " + riskLevel);
    }

    public String getTerrainType() { return terrainType; }
    public String getRiskLevel() { return riskLevel; }
}
