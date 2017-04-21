package us.wetpaws.wydlist.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.adapter.GlideUtil;
import us.wetpaws.wydlist.fragment.AdventureFragment;
import us.wetpaws.wydlist.fragment.DestinationFragment;
import us.wetpaws.wydlist.fragment.EventFragment;
import us.wetpaws.wydlist.fragment.FoodFragment;
import us.wetpaws.wydlist.fragment.HikingFragment;
import us.wetpaws.wydlist.fragment.HomeFragment;
import us.wetpaws.wydlist.fragment.MyListFragment;
import us.wetpaws.wydlist.fragment.VacationFragment;

import static us.wetpaws.wydlist.R.id.fab;

public class MainFeedActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private CircleImageView imageProfile;
    private ImageView imageNavHeaderBackground;
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
    private static final String TAG_DESTINATION = "destination";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_FOOD = "food";
    private static final String TAG_HIKING = "hiking";
    private static final String TAG_VACATION = "vacation";
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
        toolbar.setTitleTextColor(Color.WHITE);

        mHandler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        floatingActionButton = (FloatingActionButton) findViewById(fab);

        // Navigation Drawer View's Header Section
        navHeader = navigationView.getHeaderView(0);
        textName = (TextView) navHeader.findViewById(R.id.user_display_name);
        textWebsite = (TextView) navHeader.findViewById(R.id.website);
        imageNavHeaderBackground = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imageProfile = (CircleImageView) navHeader.findViewById(R.id.img_profile);

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

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        toggleFab();

        // Close the navigation drawer when item is pressed.
        drawerLayout.closeDrawers();

        // Refresh the toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                MyListFragment myListFragment = new MyListFragment();
                return myListFragment;
            case 2:
                AdventureFragment adventureFragment = new AdventureFragment();
                return adventureFragment;
            case 3:
                DestinationFragment destinationFragment = new DestinationFragment();
                return destinationFragment;
            case 4:
                EventFragment eventFragment = new EventFragment();
                return eventFragment;
            case 5:
                FoodFragment foodFragment = new FoodFragment();
                return foodFragment;
            case 6:
                HikingFragment hikingFragment = new HikingFragment();
                return hikingFragment;
            case 7:
                VacationFragment vacationFragment = new VacationFragment();
                return vacationFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    // TODO: Create a separate Class for checking the case which returns a String.
    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_mylist:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MYLIST;
                        break;
                    case R.id.nav_adventure:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ADVENTURE;
                        break;
                    case R.id.nav_destination:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_DESTINATION;
                        break;
                    case R.id.nav_events:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_EVENTS;
                        break;
                    case R.id.nav_food:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_FOOD;
                        break;
                    case R.id.nav_hiking:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HIKING;
                        break;
                    case R.id.nav_vacation:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_VACATION;
                        break;
                    case R.id.nav_about_us:
                        // Launch new Intent instead of loading a Fragment;
                        startActivity(new Intent(MainFeedActivity.this, PrivacyPolicyActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // Launch new Intent instead of loading a Fragment;
                        startActivity(new Intent(MainFeedActivity.this, PrivacyPolicyActivity.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_term_condition:
                        // Launch new Intent instead of loading a Fragment;
                        startActivity(new Intent(MainFeedActivity.this, TermAndConditionActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                // Is item in checked state, if not make it in check state
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setCheckable(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // When user is in another fragment and presses back key they sent back to home if they're not already at home fragment
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        if (navItemIndex == 2) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void toggleFab() {
        if (navItemIndex == 0) {
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
        }
    }
}