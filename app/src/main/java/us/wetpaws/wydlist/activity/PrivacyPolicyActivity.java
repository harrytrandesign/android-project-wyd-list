package us.wetpaws.wydlist.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import us.wetpaws.wydlist.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().setTitle(R.string.privacy_policy);
    }
}
