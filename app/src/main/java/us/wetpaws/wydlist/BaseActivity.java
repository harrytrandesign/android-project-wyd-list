package us.wetpaws.wydlist;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Wetpaws Studio on 4/11/17.
 */

public class BaseActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;

    // Set up a Progress Dialog.
    public void showProgressDialog() {

        String mMessage = getString(R.string.progress_dialog_loading);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(mMessage);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);

        }

        mProgressDialog.show();

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {

            mProgressDialog.hide();

        }
    }

}
