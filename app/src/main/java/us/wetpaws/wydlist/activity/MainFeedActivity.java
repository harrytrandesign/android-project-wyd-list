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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.GlideUtil;

import static us.wetpaws.wydlist.R.id.fab;

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

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        floatingActionButton = (FloatingActionButton) findViewById(fab);

        // Navigation Drawer View's Header Section
        navHeader = navigationView.getHeaderView(0);
        textName = (TextView) navHeader.findViewById(R.id.user_display_name);
        textWebsite = (TextView) navHeader.findViewById(R.id.website);
        imageNavHeaderBackground = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imageProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // Load toolbar titles from the string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainFeedActivity.this, "Do something later.", Toast.LENGTH_SHORT).show();
            }
        });

        // Load the navigation menu header
        loadNavHeader();

        // Initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    // Load the navigation menu header, such as header background image, profile name, etc
    private void loadNavHeader() {
        textName.setText("Harrison Test File");
        textWebsite.setText("wetpaws.us");

        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageNavHeaderBackground);

        GlideUtil.loadProfileIcon(urlProfileImg, imageProfile);

        // TODO: Remove later - I wanna see what this does.
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();







    }


}
