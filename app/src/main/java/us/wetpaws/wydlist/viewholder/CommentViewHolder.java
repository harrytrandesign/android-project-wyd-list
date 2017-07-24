package us.wetpaws.wydlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import us.wetpaws.wydlist.R;

/**
 * Created by HTDWPS on 7/24/17.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView comment_user_circleimage;
    private TextView comment_user_displayname;
    private TextView comment_user_displaytext;
    private TextView comment_user_timestamp;

    public CommentViewHolder(View itemView) {
        super(itemView);

        comment_user_circleimage = (CircleImageView) itemView.findViewById(R.id.comment_display_usericon);
        comment_user_displayname = (TextView) itemView.findViewById(R.id.comment_display_username);
        comment_user_displaytext = (TextView) itemView.findViewById(R.id.comment_display_message);
        comment_user_timestamp = (TextView) itemView.findViewById(R.id.comment_display_timestamp);
    }

    // TODO: Create the setter methods.

}
