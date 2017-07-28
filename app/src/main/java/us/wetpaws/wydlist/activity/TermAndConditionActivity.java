package us.wetpaws.wydlist.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import us.wetpaws.wydlist.R;

public class TermAndConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        getSupportActionBar().setTitle(R.string.term_condition);
    }
}
