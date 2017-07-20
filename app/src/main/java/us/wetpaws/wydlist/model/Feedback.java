package us.wetpaws.wydlist.model;

/**
 * Created by HTDWPS on 7/19/17.
 */

public class Feedback {

    User user;
    String feedback;
    Object timestamp;

    public Feedback() {
    }

    public Feedback(User user, String feedback, Object timestamp) {
        this.user = user;
        this.feedback = feedback;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
