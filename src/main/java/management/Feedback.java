package management;
import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class Feedback {
    private String feedbackId;
    private int rating;
    private String comments;
    private LocalDate submittedDate;
    private Participant participant;
    private Activity activity;

    public Feedback(String feedbackId, int rating, String comments, Participant participant, Activity activity) {
        this.feedbackId = feedbackId;
        this.rating = rating;
        this.comments = comments;
        this.participant = participant;
        this.activity = activity;
        this.submittedDate = LocalDate.now();
    }
    
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public Participant getParticipant() { return participant; }
}
