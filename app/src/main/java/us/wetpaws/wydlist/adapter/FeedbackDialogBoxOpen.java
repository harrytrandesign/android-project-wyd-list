package us.wetpaws.wydlist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.model.Feedback;
import us.wetpaws.wydlist.model.User;

/**
 * Created by HTDWPS on 8/9/17.
 */

public class FeedbackDialogBoxOpen extends LayoutInflater implements View.OnTouchListener {

    private AlertDialog alertDialog;
    private DatabaseReference feedbackReference;

    private EditText feedback_text_field;
    private String user_feedback_text;

    public FeedbackDialogBoxOpen(Context context) {
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
        View view = layoutInflater.inflate(R.layout.add_wyd_feedback_box, null);

        feedbackReference = FirebaseUtil.getFeedbackRef();
        final User user = FirebaseUtil.getUser();

        feedback_text_field = (EditText) view.findViewById(R.id.wyd_feedback_add);

        alertDialogBuilder
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user_feedback_text = feedback_text_field.getText().toString();
                        feedback_text_field.setText("");

                        Feedback user_feedback = new Feedback(user, user_feedback_text, ServerValue.TIMESTAMP, false);

                        feedbackReference.push().setValue(user_feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Your feedback has been submitted.", Toast.LENGTH_SHORT).show();
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

        alertDialog.show();

        return layoutInflater;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
