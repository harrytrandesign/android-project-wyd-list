package us.wetpaws.wydlist.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import us.wetpaws.wydlist.R;

public class AgeVerifyConsent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_verify_consent);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.age_consent_date_picker);
        final CheckBox ageConsentBox = (CheckBox) findViewById(R.id.confirm_user_age);
        Button submitButton = (Button) findViewById(R.id.age_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ageConsentBox.isChecked()) {
                    Toast.makeText(AgeVerifyConsent.this, "Status " + ageConsentBox.isChecked(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(AgeVerifyConsent.this, "Date " + datePicker.getDayOfMonth() + " " + datePicker.getMonth() + " " + datePicker.getYear(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
