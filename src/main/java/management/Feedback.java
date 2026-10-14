package management;

import people.Participant;
import activities.Activity;
import java.time.LocalDate;

public class Feedback extends CampEntity {
    private int rating;
    private String comments;
    private LocalDate submittedDate;
    private Participant participant;
    private Activity activity;

    public Feedback(String feedbackId, int rating, String comments, Participant participant, Activity activity) {
        super(feedbackId, "Feedback-" + feedbackId);
        this.rating = rating;
        this.comments = comments;
        this.participant = participant;
        this.activity = activity;
        this.submittedDate = LocalDate.now();
    }

    public Feedback(String feedbackId, int rating, String comments, Participant participant, Activity activity, LocalDate submittedDate) {
        super(feedbackId, "Feedback-" + feedbackId);
        this.rating = rating;
        this.comments = comments;
        this.participant = participant;
        this.activity = activity;
        this.submittedDate = submittedDate;
    }

    public String getFeedbackId() { return getId(); }
    public int getRating() { return rating; }
    public String getComments() { return comments; }
    public Participant getParticipant() { return participant; }
    public Activity getActivity() { return activity; }
    public LocalDate getSubmittedDate() { return submittedDate; }

    // --- Method Overloading: updateFeedback ---

    public void updateFeedback(int rating) {
        this.rating = rating;
    }

    /** Overloaded updateFeedback: updates both rating and comments together. */
    public void updateFeedback(int rating, String comments) {
        this.rating = rating;
        this.comments = comments;
    }

    // --- Normal Method Overriding ---

    @Override
    public String getDetails() {
        String pName = participant != null ? participant.getName() : "Unknown";
        String aTitle = activity != null ? activity.getTitle() : "Unknown";
        return String.format("[%s] ID: %s | Participant: %s | Activity: %s | Rating: %d/5",
                getClass().getSimpleName(), getId(), pName, aTitle, rating);
    }
}
