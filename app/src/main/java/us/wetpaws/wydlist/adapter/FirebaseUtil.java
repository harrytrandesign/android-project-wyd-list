package us.wetpaws.wydlist.adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import us.wetpaws.wydlist.model.User;

/**
 * Created by Wetpaws Studio on 4/8/17.
 */

public class FirebaseUtil {

    // Format code shortcut keys: option, command, l

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

    public static User getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return null;

        return new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
    }

    public static DatabaseReference getUsernameRef() {
        // User and photo and details
        return getBaseRef().child("usernames");
    }

    public static String getUsernamePath() {
        return "usernames/";
    }


    public static DatabaseReference getVerifiedAgeRef() {
        // User Age Verification
        return getBaseRef().child("verifyage");
    }

    public static String getVerifyPath() {
        return "verifyage/";
    }


    public static DatabaseReference getTopicsRef() {
        // Where new wyd bucket list items go
        return getBaseRef().child("topic");
    }

    public static DatabaseReference getCommentRef() {
        // Comments for each topic
        return getBaseRef().child("comment");
    }

    public static DatabaseReference getMainListRef() {
        // All in main bucketlist
        return getBaseRef().child("mainwydlist");
    }

    public static DatabaseReference getUserListRef() {
        // User's own list of items they created
        return getBaseRef().child("userwydlist");
    }

    public static DatabaseReference getFollowRef() {
        // Following list of wyd items user subscribed to
        return getBaseRef().child("userfollowlist");
    }

}
