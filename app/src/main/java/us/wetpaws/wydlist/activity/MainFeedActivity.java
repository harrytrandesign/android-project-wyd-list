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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import us.wetpaws.wydlist.MainActivity;
import us.wetpaws.wydlist.R;
import us.wetpaws.wydlist.SignInPageActivity;
import us.wetpaws.wydlist.adapter.GlideUtil;
import us.wetpaws.wydlist.fragment.AdventureFragment;
import us.wetpaws.wydlist.fragment.DestinationFragment;
import us.wetpaws.wydlist.fragment.HomeFragment;
import us.wetpaws.wydlist.fragment.MyListFragment;

import static us.wetpaws.wydlist.R.id.fab;

public class MainFeedActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private CircleImageView imageProfile;
    private ImageView imageNavHeaderBackground;
    private TextView textName;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    // Static image for testing the background
    private static final String urlNavHeaderBg = "menu_header.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    public static int navItemIndex = 0;

    // Tags used to attach the fragments
    private static final String TAG_LIST = "list";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_PUBLIC = "public";
    private static final String TAG_SEARCH = "search";
    public static String CURRENT_TAG = TAG_LIST;
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
        imageNavHeaderBackground = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imageProfile = (CircleImageView) navHeader.findViewById(R.id.img_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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
            CURRENT_TAG = TAG_LIST;
            loadHomeFragment();
        }
    }

    // Load the navigation menu header, such as header background image, profile name, etc
    private void loadNavHeader() {
        textName.setText(mFirebaseUser.getDisplayName());
        imageNavHeaderBackground.setImageResource(R.drawable.menu_header);

//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageNavHeaderBackground);

        GlideUtil.loadProfileIcon(mFirebaseUser.getPhotoUrl().toString(), imageProfile);

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
                    case R.id.nav_list:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_LIST;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_publicfeed:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PUBLIC;
                        break;
                    case R.id.nav_search:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SEARCH;
                        break;
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
                    case R.id.nav_log_off:
                        // Launch new Intent instead of loading a Fragment;
                        startActivity(new Intent(MainFeedActivity.this, SignInPageActivity.class));
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
                CURRENT_TAG = TAG_LIST;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (navItemIndex == 0) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        }
//        if (navItemIndex > 0) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();

                Intent mainIntent = new Intent(MainFeedActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();

                break;

            default:

                break;

        }

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