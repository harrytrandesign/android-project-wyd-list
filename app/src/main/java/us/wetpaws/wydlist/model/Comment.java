package us.wetpaws.wydlist.model;

/**
 * Created by HTDWPS on 7/19/17.
 */

public class Comment {

    private User user;
    private String commentText;
    private Object timestamp;

    public Comment() {
    }

    public Comment(User user, String commentText, Object timestamp) {
        this.user = user;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
