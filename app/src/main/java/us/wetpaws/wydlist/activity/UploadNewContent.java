package us.wetpaws.wydlist.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.SignInPageActivity;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;

public class UploadNewContent extends AppCompatActivity implements View.OnClickListener {

    protected static final String LOG_TAG = UploadNewContent.class.getSimpleName();

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private final int MIN_TITLE_LENGTH = 4;
    private Intent pictureActionIntent = null;
    String selectedImagePath;
    Bitmap bitmap;

    private DatabaseReference mainFeedReference;
    private DatabaseReference tagFeedReference;
    private DatabaseReference userFeedReference;
    private DatabaseReference missingPhotoReference;
    User user;

    EditText wyd_title_edit;
    EditText wyd_tag_edit;
    TextView wyd_camera_open_text;
    Button wyd_submit_data_button;
    String wyd_title_string;
    String wyd_tag_string;

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_content);

        mainFeedReference = FirebaseUtil.getMainListRef();
        tagFeedReference = FirebaseUtil.getTagRef();
        userFeedReference = FirebaseUtil.getUserListRef();
        missingPhotoReference = FirebaseUtil.getMissingPhotoRef();
        user = FirebaseUtil.getUser();

        wyd_title_edit = (EditText) findViewById(R.id.wyd_title_add_activity);
        wyd_tag_edit = (EditText) findViewById(R.id.wyd_tag_add_activity);
        wyd_camera_open_text = (TextView) findViewById(R.id.wyd_image_add_camera_activity);
        wyd_submit_data_button = (Button) findViewById(R.id.wyd_submit_button_activity);

        wyd_submit_data_button.setOnClickListener(this);
    }

    private void uploadToFirebaseDatabase() {
        wyd_title_string = wyd_title_edit.getText().toString();
        wyd_tag_string = wyd_tag_edit.getText().toString();
        wyd_title_edit.setText("");
        wyd_tag_edit.setText("");

        final String randomPostKey = mainFeedReference.push().getKey();

        BucketList bucketList = new BucketList(user, wyd_title_string, "http://www.redspottedhanky.com/images/215/original/colchester-zoo_monkey_colchester_46190563.jpg", ServerValue.TIMESTAMP);

        mainFeedReference.child(randomPostKey).setValue(bucketList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userFeedReference = FirebaseUtil.getUserListRef();
                userFeedReference.child(user.getUserid()).child(randomPostKey).setValue(true);

                tagFeedReference.child(wyd_tag_string).setValue(randomPostKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "All completed.", Toast.LENGTH_SHORT).show();

                        Intent switchIntent = new Intent(UploadNewContent.this, MainFeedActivity.class);
                        switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(switchIntent);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wyd_image_add_camera_activity:

                break;

            case R.id.wyd_submit_button_activity:

                uploadToFirebaseDatabase();

                break;
        }
    }
}
