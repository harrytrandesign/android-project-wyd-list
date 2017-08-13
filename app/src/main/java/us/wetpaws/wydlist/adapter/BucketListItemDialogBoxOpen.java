package us.wetpaws.wydlist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.model.BucketList;
import us.wetpaws.wydlist.model.User;

/**
 * Created by HTDWPS on 8/8/17.
 */

public class BucketListItemDialogBoxOpen extends LayoutInflater implements View.OnTouchListener, View.OnClickListener {

    private AlertDialog alertDialog;
    private DatabaseReference mainFeedReference;
    private DatabaseReference tagFeedReference;
    private DatabaseReference userFeedReference;
    private DatabaseReference missingPhotoReference;
    private final int MIN_TITLE_LENGTH = 4;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    String selectedImagePath;
    Bitmap bitmap;

    private EditText wyd_title_field;
    private TextView wyd_camera_open;
    private TextView wyd_gallery_open;
    private EditText wyd_tag_field;
    private String wyd_title_new;
    private String wyd_image_new;
    private String wyd_tag_new;
    private TextView wyd_title_label;
    private TextView wyd_image_label;
    private TextView wyd_tag_label;

    public BucketListItemDialogBoxOpen(Context context) {
        super(context);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public LayoutInflater cloneInContext(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.add_wyd_alert_box, null);

        mainFeedReference = FirebaseUtil.getMainListRef();
        tagFeedReference = FirebaseUtil.getTagRef();
        userFeedReference = FirebaseUtil.getUserListRef();
        missingPhotoReference = FirebaseUtil.getMissingPhotoRef();
        final User user = FirebaseUtil.getUser();

        wyd_title_field = (EditText) view.findViewById(R.id.wyd_title_add);
        wyd_camera_open = (TextView) view.findViewById(R.id.wyd_image_add_camera);
        wyd_gallery_open = (TextView) view.findViewById(R.id.wyd_image_add_gallery);
        wyd_tag_field = (EditText) view.findViewById(R.id.wyd_tag_add);
        wyd_title_label = (TextView) view.findViewById(R.id.wyd_title_label);
        wyd_image_label = (TextView) view.findViewById(R.id.wyd_image_label);
        wyd_tag_label = (TextView) view.findViewById(R.id.wyd_tag_label);

        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        wyd_title_new = wyd_title_field.getText().toString();
                        wyd_title_field.setText("");
                        wyd_tag_new = wyd_tag_field.getText().toString();
                        wyd_tag_field.setText("");

                        final String randomPostKey = mainFeedReference.push().getKey();

                        BucketList bucketList = new BucketList(user, wyd_title_new, "http://www.redspottedhanky.com/images/215/original/colchester-zoo_monkey_colchester_46190563.jpg", ServerValue.TIMESTAMP);

                        mainFeedReference.child(randomPostKey).setValue(bucketList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userFeedReference = FirebaseUtil.getUserListRef();
                                userFeedReference.child(user.getUserid()).child(randomPostKey).setValue(true);

//                                if (wyd_camera_open.getText().toString().equals("")) {
//                                    missingPhotoReference.child(user.getUserid()).child(randomPostKey).setValue(true);
//                                }

                                tagFeedReference.child(wyd_tag_new).setValue(randomPostKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "All completed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //TODO Uncomment this setEnabled in the live app.
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        alertDialog.show();

        wyd_camera_open.setOnClickListener(this);
        wyd_gallery_open.setOnClickListener(this);

        return layoutInflater;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.wyd_image_add_camera:

//                startDialog();

                break;

            case R.id.wyd_image_add_gallery:


                break;
        }

    }
}
