package us.wetpaws.wydlist.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.GlideUtil;

/**
 * Created by HTDWPS on 7/24/17.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView comment_user_circleimage;
    private TextView comment_user_displayname;
    private TextView comment_user_displaytext;
    private TextView comment_user_timestamp;
    private String altProfileUrl = "https://firebasestorage.googleapis.com/v0/b/wetpawslolpets.appspot.com/o/profile_image.png?alt=media&token=aa79fc5f-9fa2-4576-bbc3-e6438ffba952";

    public CommentViewHolder(View itemView) {
        super(itemView);

        comment_user_circleimage = (CircleImageView) itemView.findViewById(R.id.comment_display_usericon);
        comment_user_displayname = (TextView) itemView.findViewById(R.id.comment_display_username);
        comment_user_displaytext = (TextView) itemView.findViewById(R.id.comment_display_message);
        comment_user_timestamp = (TextView) itemView.findViewById(R.id.comment_display_timestamp);
    }

    public void setProfileIcon(String url) {
        if (url != null) {
            GlideUtil.loadProfileIcon(url, comment_user_circleimage);
        } else {
            GlideUtil.loadProfileIcon(altProfileUrl, comment_user_circleimage);
        }
    }

    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            author = itemView.getResources().getString(R.string.user_info_no_name);
        }
        comment_user_displayname.setText(author);
    }

    public void setCommentText(final String text) {
        comment_user_displaytext.setText(text);
    }

    public void setCommentTimestamp(final String timestamp) {
        comment_user_timestamp.setText(timestamp);
    }

}
