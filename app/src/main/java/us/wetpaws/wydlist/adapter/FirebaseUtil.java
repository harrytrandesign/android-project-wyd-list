package us.wetpaws.wydlist.adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Wetpaws Studio on 4/8/17.
 */

public class FirebaseUtil {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("people").child(getCurrentUserId());
        }
        return null;
    }

    public static DatabaseReference getUsernameRef() {
        return getBaseRef().child("username");
    }

    public static String getUsernamePath() {
        return "username/";
    }

    public static DatabaseReference getCommentsRef() {
        return getBaseRef().child("comments");
    }

    public static String getCommentPath() {
        return "comments/";
    }

    public static DatabaseReference getFollowingRef() {
        return getBaseRef().child("followed");
    }

    public static String getFollowingPath() {
        return "followed/";
    }

}
