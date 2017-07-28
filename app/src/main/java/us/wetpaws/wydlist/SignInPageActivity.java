package us.wetpaws.wydlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import us.wetpaws.wydlist.activity.AgeVerifyConsent;
import us.wetpaws.wydlist.activity.MainFeedActivity;
import us.wetpaws.wydlist.adapter.FirebaseUtil;
import us.wetpaws.wydlist.model.User;

public class SignInPageActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = SignInPageActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;
    String photoResolutionSizeString = "s300-c";
    private static GoogleApiClient mGoogleApiClient;
    private final DatabaseReference mFirebaseDatabase = FirebaseUtil.getBaseRef();
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    KenBurnsView backgroundImage;
    TextView termConditionButtonText;
    TextView privacyButtonText;
    SignInButton signUpButton;

    private String usernamePath = FirebaseUtil.getUsernamePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in_page);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

        mFirebaseAuth = FirebaseAuth.getInstance();

        backgroundImage = (KenBurnsView) findViewById(R.id.ken_burn_background_img);

        signUpButton = (SignInButton) findViewById(R.id.sign_in_button);
        signUpButton.setSize(SignInButton.SIZE_WIDE);
        signUpButton.setColorScheme(SignInButton.COLOR_LIGHT);
        signUpButton.setScopes(gso.getScopeArray());
        signUpButton.setOnClickListener(this);
//
//        termConditionButtonText = (TextView) findViewById(R.id.terms_conditions);
//        termConditionButtonText.setOnClickListener(this);
//        privacyButtonText = (TextView) findViewById(R.id.privacy_policy);
//        privacyButtonText.setOnClickListener(this);
    }

    private void signInNow() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            showProgressDialog();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundImage.pause();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount userAccount = result.getSignInAccount();
            firebaseAuthWithGoogle(userAccount);

        } else {

            hideProgressDialog();

            Toast.makeText(this, "Failed to sign in.", Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            mFirebaseDatabase.child(usernamePath).child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        Toast.makeText(SignInPageActivity.this, "User already exists in the database.", Toast.LENGTH_SHORT).show();

                                        hideProgressDialog();

                                        Intent switchIntent = new Intent(SignInPageActivity.this, MainFeedActivity.class);
                                        switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(switchIntent);

                                    } else {

                                        Toast.makeText(SignInPageActivity.this, "User doesn't exist yet, creating user in database, and send to onboarding screen.", Toast.LENGTH_SHORT).show();

                                        String photoQualityEnlargeRes = String.valueOf(mFirebaseUser.getPhotoUrl());
                                        String[] splitString = photoQualityEnlargeRes.split("s96-c");
                                        String firstString = splitString[0];
                                        String secondString = splitString[1];
                                        String newImageString = firstString + photoResolutionSizeString + secondString;

                                        User myUserName = new User(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(), newImageString);

                                        mFirebaseDatabase.child(usernamePath).child(mFirebaseUser.getUid()).setValue(myUserName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(SignInPageActivity.this, "Good Log In Done", Toast.LENGTH_SHORT).show();

                                                hideProgressDialog();

                                                Intent switchIntent = new Intent(SignInPageActivity.this, AgeVerifyConsent.class);
                                                switchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(switchIntent);

                                            }
                                        });

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInPageActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in_button:

                signInNow();

                break;

            default:

                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
