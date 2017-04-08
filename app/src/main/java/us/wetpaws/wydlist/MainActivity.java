package us.wetpaws.wydlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import us.wetpaws.wydlist.activity.MainFeedActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent AuthIntent = new Intent(this, MainFeedActivity.class);
        AuthIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(AuthIntent);
        finish();

    }

}
