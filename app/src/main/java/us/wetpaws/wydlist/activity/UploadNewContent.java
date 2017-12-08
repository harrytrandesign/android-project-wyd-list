package us.wetpaws.wydlist.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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

import java.io.File;
import java.util.regex.Pattern;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;

public class UploadNewContent extends AppCompatActivity implements View.OnClickListener {

    protected static final String LOG_TAG = UploadNewContent.class.getSimpleName();

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private final int MIN_TITLE_LENGTH = 5;
    private final int CHAR_LENGTH_LIMIT = 25;
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

    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

            for (int x = i; x < i1; ++x) {
                if (Pattern.compile("[ ]*").matcher(String.valueOf(charSequence.charAt(x))).matches()) {
                    return "";
                }
            }

            for (int x = i; x < i1; ++x) {
                if (Character.isWhitespace(charSequence.charAt(x))) {
                    return "";
                }
            }


            return null;
        }
    };


    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getSupportActionBar().setTitle(R.string.upload_new_content);
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

        wyd_title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() < MIN_TITLE_LENGTH) {
                    wyd_title_edit.setError(getString(R.string.length_title_short_msg));
                }
            }
        });
        wyd_tag_edit.setFilters(new InputFilter[] {inputFilter, new InputFilter.LengthFilter(CHAR_LENGTH_LIMIT)});
        wyd_camera_open_text.setOnClickListener(this);
        wyd_submit_data_button.setOnClickListener(this);
    }

    private void selectImageCameraGallery() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    private void uploadToFirebaseDatabase() {
        wyd_title_string = wyd_title_edit.getText().toString();
        wyd_tag_string = wyd_tag_edit.getText().toString();

        final String randomPostKey = mainFeedReference.push().getKey();

        BucketList bucketList = new BucketList(user, wyd_title_string, "http://www.redspottedhanky.com/images/215/original/colchester-zoo_monkey_colchester_46190563.jpg", ServerValue.TIMESTAMP);

        mainFeedReference.child(randomPostKey).setValue(bucketList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                wyd_title_edit.setText("");
                wyd_tag_edit.setText("");

//                userFeedReference = FirebaseUtil.getUserListRef(); // Repeat.
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);



//                img_logo.setImageBitmap(bitmap);
                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {
//                    txt_image_path.setText(selectedImagePath);
                }

                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);

//                img_logo.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wyd_image_add_camera_activity:

                selectImageCameraGallery();

                break;

            case R.id.wyd_submit_button_activity:

                uploadToFirebaseDatabase();

                break;
        }
    }
}
