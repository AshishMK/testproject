package test.com.mygapplication.Logins;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import test.com.mygapplication.R;
import test.com.mygapplication.Utills.GetFBHash;
import test.com.mygapplication.Utills.Screen;
import test.com.mygapplication.application.AppController;
import test.com.mygapplication.interfaces.OnSocialClickfromAdapter;
import test.com.mygapplication.pager_adapter.LoginPagerAdapterSimple;
import test.com.mygapplication.preferences.Preferences;


/**
 * Simple Activity to perform Login and registration process
 * <br>It holds to different fragments #RegisterFragment #LoginFragment for login and registration</br>
 */
public class LoginRegisterActivitySimple extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //View Pager object to hold #RegisterFragment and #LoginFragment
    public ViewPager mPager;

    //TabLayout to hold tabs for #mPager
    TabLayout mTabLayout;

    //Pager Adapter to get #RegisterFragment #LoginFragment for #mPager
    LoginPagerAdapterSimple mAdpater;

    //fb google sdk initialization setup
    LoginButton loginButtonFB;
    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;
    public static final int G_LOGIN = 101;
    int dummy_height = 0,actvityChoice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init Facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        //type of activity selected
        actvityChoice=getIntent().getIntExtra("choice",0);

        //Set content layout for current activty
        setContentView(actvityChoice==0?R.layout.activity_login_register:R.layout.activity_login_strech);

        //Setup ActionBar for activity
        setUpActionBar();

        //check If User login
        if (Preferences.isUserLoggedIn(this)) {
            //    startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        //Initialize the current layout's views
        initViews();
        if(actvityChoice==1) {
            //setup views animation and properties for strach functionality
            setUpStarchViews();
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));//color.toStatusBarColor(this));
            }
        }
        //Setup Listeners on clickable views
        setUpListeners();

        //Method use to print hash key for facebook app
        GetFBHash.printKeyHash(this);

        //Initialize and setup facebook's fields for login and registration
        initFB();

        //Initialize and setup google's fields for login and registration
        initG();
    }

    void setUpStarchViews(){
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setCollapsedTitleTextColor(Color.BLACK);
        mPager.post(new Runnable() {
            @Override
            public void run() {
                mPager.getLayoutParams().height = dummy_height = (int) (Screen.getHeight() - (Screen.dp(238)));

            }
        });
        ((AppBarLayout) findViewById(R.id.app_bar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int scrollYy) {
                final int scrollY = (int) (scrollYy * .75);

                ValueAnimator anim = ValueAnimator.ofInt(mPager.getMeasuredHeightAndState(),
                        scrollY);

                if (dummy_height > 0) {
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mPager.getLayoutParams();
                            //    System.out.println("ssss" + dummy_height + " " + scrollY + " " + lastscroll);
                            layoutParams.height = (dummy_height + (-scrollY));
                            //layoutParams.topMargin =  layoutParams.topMargin+(lastscroll > scrollY ?-1:1);//( dummy_margin + (lastscroll > scrollY ?- scrollY : scrollY));
                            mPager.setLayoutParams(layoutParams);
                        }
                    });
                    anim.start();
                }
            }
        });
    }

    /**
     * Method to initialize common settings for ActionBar
     */
    public void setUpActionBar() {
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initFB() {
        //setting up facebook login registration
        loginButtonFB.setReadPermissions("email");

        // If using in a fragment
        //loginButtonFB.setFragment(this);
        callbackManager = CallbackManager.Factory.create();

        loginButtonFB.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(@NonNull LoginResult loginResult) {

                        //On successful login do A facebook graph request for getting profile informatuion
                        //like id, first_name, last_name, email,gender, birthday, location Check below
                        GraphRequest gRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@NonNull JSONObject object, GraphResponse response) {
                                try {

                                    //Sending profile login data to our server for login using Facebook via POST method
                                    final String name_ = object.getString("first_name") + " " + object.getString("last_name");
                                    RequestBody requestBody = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("facebook_id", URLEncoder.encode(object.getString("id"), "UTF-8"))
                                            .addFormDataPart("device_type", "1")
                                            .addFormDataPart("full_name", URLEncoder.encode(name_, "UTF-8"))
                                            //    .addFormDataPart("email", URLEncoder.encode(object.getString("email"), "UTF-8"))
                                            .build();
                                    Toast.makeText(LoginRegisterActivitySimple.this, "facebook response Please add login url here @ LoginRegisterActivitySimple 139", Toast.LENGTH_SHORT).show();
                            /*        new ExecuteServices().executePost(CommonFields.webserver_url + "facebook-login", new ExecuteServices.OnServiceExecute() {
                                        @Override
                                        public void onServiceExecutedResponse(final String response) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //start new activity here on successful login register via FB account

                                                }
                                            });
                                            //save usere preferences from fb profile
                                         if (!Preferences.saveUser(response, LoginRegisterActivity.this, Preferences.TYPE_FB, null)) {
                                                return;
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //start new activity here on successful login register via FB account
                                               //     startActivity(new Intent(LoginRegisterActivity.this,HomeActivity.class));
                                                    finish();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onServiceExecutedFailed(final String messsage) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //show error message
                                              // Toast.makeText(LoginRegisterActivity.this, "" + messsage, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }, requestBody);*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                            }
                        });

                        //init  GraphRequest to get Facebook profile information
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        gRequest.setParameters(parameters);
                        gRequest.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        //  Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }


    /**
     * This method is called from onActivityResult when a user try to login via Google account
     *
     * @param result Google Signin result received onActivityResult
     */
    private void handleSignInResult(@NonNull GoogleSignInResult result) {
        final GoogleSignInAccount acct = result.getSignInAccount();
        if (result.isSuccess()) {
            try {
                //Sending profile login data to our server for login using Google via POST method
                final String name_ = acct.getDisplayName();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("google_id", URLEncoder.encode(acct.getId(), "UTF-8"))
                        .addFormDataPart("full_name", URLEncoder.encode(name_, "UTF-8"))
                        .addFormDataPart("device_type", "1")
                        .addFormDataPart("email", URLEncoder.encode(acct.getEmail(), "UTF-8"))
                        .build();
                Toast.makeText(this, "Google response please add login url here LoginRegisterActivitySimple 224", Toast.LENGTH_SHORT).show();
           /*     new ExecuteServices().executePost(CommonFields.webserver_url + "google-login", new ExecuteServices.OnServiceExecute() {
                    @Override
                    public void onServiceExecutedResponse(final String response) {
                        pd.dismiss();
                        //save usere preferences from fb profile
                        if (!Preferences.saveUser(response, LoginRegisterActivity.this, Preferences.TYPE_G, acct.getPhotoUrl() == null ? null : acct.getPhotoUrl().toString())) {
                            return;
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(LoginRegisterActivity.this, HomeActivity.class));
                                LoginRegisterActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onServiceExecutedFailed(final String messsage) {
                        pd.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginRegisterActivity.this, "" + messsage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, requestBody);*/
            } catch (UnsupportedEncodingException e) {
            }

        }
    }

    /**
     * Method use to signout an user from google login
     */
    void signOutG() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    public void initG() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Call Facebook callback manager after performing login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //Handle Google Signin after performing login from a gmail account
        if (requestCode == G_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    /**
     * Method used to setup Viewpager with pager adapter
     */
    public void setUpPager() {
        mAdpater = new LoginPagerAdapterSimple(this, getSupportFragmentManager(), 2);
        mAdpater.setOnSocialLoginClickListener(new OnSocialClickfromAdapter() {
            @Override
            public void onFBClicked() {
                loginButtonFB.performClick();
            }

            @Override
            public void onGoogleClicked() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, G_LOGIN);

            }
        });
        mPager.setAdapter(mAdpater);
        mTabLayout.setupWithViewPager(mPager);
    }

    public void initViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_lay);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        loginButtonFB = (LoginButton) findViewById(R.id.login_fb);
        setUpPager();


    }

    public void setUpListeners() {

    }


    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "" + AppController.getAppContext().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
    }
}
