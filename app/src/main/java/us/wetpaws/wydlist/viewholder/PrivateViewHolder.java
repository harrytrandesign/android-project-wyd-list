package us.wetpaws.wydlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import us.wetpaws.wydlist.R;

/**
 * Created by HTDWPS on 7/24/17.
 */

public class PrivateViewHolder extends RecyclerView.ViewHolder {

    private TextView personal_bucketlist_title;

    public PrivateViewHolder(View itemView) {
        super(itemView);

        personal_bucketlist_title = (TextView) itemView.findViewById(R.id.profile_bucket_title);
    }

    public void setPersonal_bucketlist_title(String text) {
        personal_bucketlist_title.setText(text);
    }

}
