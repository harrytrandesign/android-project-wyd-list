package us.wetpaws.wydlist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.UserAge;

public class AgeVerifyConsent extends AppCompatActivity {

    private final DatabaseReference mFirebaseDatabase = FirebaseUtil.getBaseRef();
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private String usernamePath = "usernames";
    private String verifyPath = "verifyage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_verify_consent);

        mFirebaseAuth = FirebaseAuth.getInstance();

        final DatePicker datePicker = (DatePicker) findViewById(R.id.age_consent_date_picker);
        final CheckBox ageConsentBox = (CheckBox) findViewById(R.id.confirm_user_age);
        Button submitButton = (Button) findViewById(R.id.age_submit_button);

        final int monthNumber = datePicker.getMonth() + 1;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ageConsentBox.isChecked()) {

                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    String birthdayStatedDate = monthNumber + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                    Boolean verifyCoppaStatement = ageConsentBox.isChecked();

                    Toast.makeText(AgeVerifyConsent.this, "Status " + verifyCoppaStatement, Toast.LENGTH_SHORT).show();
                    Toast.makeText(AgeVerifyConsent.this, "Date " + birthdayStatedDate, Toast.LENGTH_SHORT).show();

                    UserAge userAge = new UserAge(birthdayStatedDate, verifyCoppaStatement);

                    mFirebaseDatabase.child(usernamePath).child(mFirebaseUser.getUid()).child(verifyPath).setValue(userAge).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Intent switchIntent = new Intent(AgeVerifyConsent.this, MainFeedActivity.class);
                            switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(switchIntent);

                        }
                    });

                } else {

                    Toast.makeText(AgeVerifyConsent.this, R.string.error_over_13_only, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
