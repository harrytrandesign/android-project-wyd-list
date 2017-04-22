package us.wetpaws.wydlist.model;

/**
 * Created by Wetpaws Studio on 4/21/17.
 */

public class User {

    private String userid;
    private String userDisplayName;

    public User() {
    }

    public User(String userid, String userDisplayName) {
        this.userid = userid;
        this.userDisplayName = userDisplayName;
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
}
