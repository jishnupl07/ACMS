package activities;
import management.Location;
import people.Instructor;

public class TeamBuildingActivity extends Activity {
    private int teamSize;

    public TeamBuildingActivity(String id, String title, int duration, int max, Location loc, Instructor inst, int teamSize) {
        super(id, title, duration, max, loc, inst);
        this.teamSize = teamSize;
    }

    public TeamBuildingActivity(String id, String title, String desc, int duration, int max, Location loc, Instructor inst, int teamSize) {
        super(id, title, desc, duration, max, loc, inst);
        this.teamSize = teamSize;
    }

    @Override
    public void conductActivity() {
        System.out.println("Conducting Team Building Activity: " + getTitle() + " with teams of " + teamSize);
    }
    public int getTeamSize() { return teamSize; }
}
