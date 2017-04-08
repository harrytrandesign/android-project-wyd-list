package us.wetpaws.wydlist.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import us.wetpaws.wydlist.R;

public class MainFeedActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private ImageView imageNavHeaderBackground, imageProfile;
    private TextView textName, textWebsite;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    // Static image for testing the background
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    public static int navItemIndex = 0;

    // Tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MYLIST = "my_list";
    private static final String TAG_ADVENTURE = "adventure";
    public static String CURRENT_TAG = TAG_HOME;
    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress =  true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
    }
}
