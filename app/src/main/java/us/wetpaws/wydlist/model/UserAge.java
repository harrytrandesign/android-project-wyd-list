package us.wetpaws.wydlist.model;

/**
 * Created by HTDWPS on 7/17/17.
 */

public class UserAge {

    private String birthdate;
    private Boolean overThirteen;

    public UserAge() {
    }

    public UserAge(String birthdate, Boolean overthirteen) {
        this.birthdate = birthdate;
        this.overThirteen = overthirteen;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getOverThirteen() {
        return overThirteen;
    }

    public void setOverThirteen(Boolean overThirteen) {
        this.overThirteen = overThirteen;
    }
}
