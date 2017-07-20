package us.wetpaws.wydlist.model;

/**
 * Created by HTDWPS on 7/17/17.
 */

public class UserAge {

    private String dateofbirth;
    private Boolean overThirteen;

    public UserAge() {
    }

    public UserAge(String dateofbirth, Boolean overthirteen) {
        this.dateofbirth = dateofbirth;
        this.overThirteen = overthirteen;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public Boolean getOverThirteen() {
        return overThirteen;
    }

    public void setOverThirteen(Boolean overThirteen) {
        this.overThirteen = overThirteen;
    }
}
