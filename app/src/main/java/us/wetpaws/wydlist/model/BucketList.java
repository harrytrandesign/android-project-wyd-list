package us.wetpaws.wydlist.model;

/**
 * Created by HTDWPS on 7/19/17.
 */

public class BucketList {

    private User user;
    private String title;
    private String imageurl;
    private Object timestamp;

    public BucketList() {
    }

    public BucketList(User user, String title, String imageurl, Object timestamp) {
        this.user = user;
        this.title = title;
        this.imageurl = imageurl;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
