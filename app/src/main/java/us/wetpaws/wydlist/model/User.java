package us.wetpaws.wydlist.model;

/**
 * Created by Wetpaws Studio on 4/21/17.
 */

public class User {

    private String userid;
    private String userDisplayName;
    private String userEmail;
    private String userPhoto;

    public User() {
    }

    public User(String userid, String userDisplayName, String userEmail, String userPhoto) {
        this.userid = userid;
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
