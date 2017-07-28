package us.wetpaws.wydlist.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.fragment.CommentFragment;

public class CommentActivity extends AppCompatActivity {

    public static final String POST_KEY_EXTRA = "post_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        String postKey = getIntent().getStringExtra(POST_KEY_EXTRA);
        if (postKey == null) {
            finish();
        }

        getSupportActionBar().setTitle("" + postKey);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.comments_fragment, CommentFragment.newInstance(postKey)).commit();
    }
}
