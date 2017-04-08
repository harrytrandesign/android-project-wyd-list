package us.wetpaws.wydlist.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import us.wetpaws.wydlist.R;

/**
 * Created by Wetpaws Studio on 4/8/17.
 */

public class GlideUtil {

    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary));
        Glide.with(context)
                .load(url)
                .placeholder(colorDrawable)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadProfileIcon(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.user_profile_image)
                .dontAnimate()
                .fitCenter()
                .into(imageView);
    }

}
