package com.app.ashesha.v3care;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.User.ForgotPasswordActivity;
import com.app.ashesha.v3care.User.OTPActivity;
import com.app.ashesha.v3care.User.RegisterActivity;
import com.app.ashesha.v3care.Utils.ApiCallingFlow;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = MainActivity.this.getClass().getName();

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    TextView tvGotosignup, forgotPswrd;
    EditText phoneNumber, passwrd;
    Button buttonLogin;
    private PrefManager prefManager;
    private ApiCallingFlow apiCallingFlow;
    private static final String KEY_MOBILE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FB_ID = "fbid";
    private CircleImageView circleImageView;
    private TextView textFbEmail, textFbUserName;
    //goggle+ vriables
    private TextView txtName, txtEmail;
    private static final int RC_SIGN_IN = 007;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic,profile_pic;
    String email, last_name, first_name, id, personName, googleEmail,personPhotoUrl,fbProfileImage;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        prefManager = new PrefManager(this);

        requestQueue = Volley.newRequestQueue(this);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        phoneNumber = findViewById(R.id.etPhone);
        passwrd = findViewById(R.id.etPassword);
        tvGotosignup = findViewById(R.id.tvGotosignup);
        forgotPswrd = findViewById(R.id.forgot_paswrd);
        buttonLogin = findViewById(R.id.buttonLogin);
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        textFbUserName = findViewById(R.id.profile_name);
        textFbEmail = findViewById(R.id.profile_email);
        circleImageView = findViewById(R.id.profile_pic);
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);
//to print facebook hash key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.ashesha.v3care",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {


        }

        tvGotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgotPswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int verify = validate();
                if (verify == 0) {
                    requestServiceApi();
                }

            }
        });

        loginButton.setPermissions(Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("errorreposnse", "" + exception);
                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


//google+ login

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            //googleLogin();

        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("fbresponse", "fbresponse" + object);
                    first_name = object.getString("first_name");
                    last_name = object.getString("last_name");
                    email = object.optString("email");
                    id = object.getString("id");

                    fbProfileImage = "https://graph.facebook.com/" + id + "/picture?type=normal";
                    prefManager.storeValue(AppConstants.FACEBOOK_App_ID, id);
                    prefManager.setFacebookAppId(id);
                    prefManager.storeValue(AppConstants.image, fbProfileImage);
                    prefManager.setFacebookAppId(fbProfileImage);
                    textFbEmail.setText(email);
                    textFbUserName.setText(first_name);
                    Log.e("fbmail", "fbmail" + email);
                    Log.e("fbname", "fbname" + first_name);
                    Log.e("fbname", "fbname" + last_name);
                    Log.e("fbid", "fbid" + id);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    Glide.with(getApplicationContext()).load(fbProfileImage).into(circleImageView);
                    facebookLogin();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void facebookLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SOICAL_LOGINS_FB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("fb Response", "fb Response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
                        String userId = jsonObject1.getString("user_id");
                        String emial = jsonObject1.getString("email");
                        String uname = jsonObject1.getString("fb_username");
                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, userId);
                        prefManager.setUserId(userId);
                        Log.i("fb Userid", "fbuserId" + userId);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, uname);
                        prefManager.setUsername(uname);
                        Log.i("fb username", "name" + uname);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, emial);
                        prefManager.setEmailId(emial);
                        Log.i("fb emailid", "emial" + emial);

                        Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put(KEY_EMAIL, email);
                Log.i("email", "email" + email);

                map.put(KEY_USERNAME, first_name + last_name);
                Log.i("username", "username" + first_name + last_name);

                map.put(KEY_FB_ID, id);
                Log.i("fb Id", "fb ID" + id);

                return map;
            }

        };

        requestQueue.add(stringRequest);
    }

    //GOOGLE+ Integration
    private void googleLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GOOGLE_PLUS_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("google Response", "google Response" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
                        String userId = jsonObject1.getString("user_id");
                        String emial = jsonObject1.getString("email");
                        String uname = jsonObject1.getString("name");
                        prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, userId);
                        prefManager.setUserId(userId);
                        Log.i("google Userid", "userId" + userId);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, uname);
                        prefManager.setUsername(uname);
                        Log.i("google name", "name" + uname);

                        prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, emial);
                        prefManager.setEmailId(emial);
                        Log.i("google emailid", "emial" + emial);

                        Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();

                map.put(KEY_EMAIL, googleEmail);
                Log.i("googleplusemail", "email" + googleEmail);

                map.put(KEY_USERNAME, personName);
                Log.i("googleplususername", "username" + personName);

                return map;
            }

        };
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d("response",""+result);
        }
    }
    private void signIn() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);

                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("googleresponse", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
           personName = acct.getDisplayName();
            //personPhotoUrl = acct.getPhotoUrl().toString();
           googleEmail = acct.getEmail();
            Log.d("email", "display email: " + acct.getEmail());
            Log.d("name", "display name: " + acct.getDisplayName());
            Log.d(TAG, "Name: " + personName + ", email: " + email
                    );
            txtName.setText(personName);
            txtEmail.setText(googleEmail);
            googleLogin();

        } else {
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            Log.d("googlesignin", "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.setIndeterminate(true);
//        }
//       mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//            //mProgressDialog.dismiss();
//        }
//    }

    private void  updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }

    //Login
    private void requestServiceApi() {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            login();
        }
    }

    private void login() {
        final String phone = phoneNumber.getText().toString();
        final String password = passwrd.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");
                            //  Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();

                            String msg = object.getString("msg");
                            switch (status) {
                                case "success":
                                    JSONObject json = object.getJSONObject("user_profile");

                                    String user_id = json.getString("user_id");

                                    String username = json.getString("name");
                                    String email = json.getString("email");
                                    String mobile = json.getString("mobile");

                                    prefManager.storeValue(AppConstants.APP_USER_LOGIN, true);
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_ID, user_id);
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_NAME, json.getString("name"));
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_EMAIL, json.getString("email"));
                                    prefManager.storeValue(AppConstants.APP_LOGIN_USER_MOBILE, json.getString("mobile"));
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                                    startActivity(intent);
                                    prefManager.setUserId(user_id);
                                    Log.e("uesr_id", prefManager.getUserId());
                                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                                    prefManager.setUsername(username);
                                    prefManager.setPhoneNumber(mobile);
                                    prefManager.setEmailId(email);

                                    finish();

                                    break;
                                case "0":
                                    Toast.makeText(MainActivity.this, "Mobile Number Not Registered With Us", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    break;
                                case "error1":
                                    Toast.makeText(MainActivity.this, "otp not verifie pls verify your otp", Toast.LENGTH_SHORT).show();
                                    Intent intet = new Intent(MainActivity.this, OTPActivity.class);
                                    startActivity(intet);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("login", "_--------------Login Response----------------" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallingFlow.onErrorResponse();
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                        Log.i("notlogin", "_---------------------------------" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_MOBILE, phone);
                map.put(KEY_PASSWORD, password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private int validate() {
        int flag = 0;
        if (phoneNumber.getText().toString().isEmpty()) {
            phoneNumber.setError(getString(R.string.enter_valid_number));
            phoneNumber.requestFocus();
            flag = 1;
        } else if (passwrd.getText().toString().isEmpty()) {
            passwrd.setError(getString(R.string.enter_password));
            passwrd.requestFocus();
            flag = 1;
        } else if (phoneNumber.length() != 10) {
            phoneNumber.requestFocus();
            phoneNumber.setError(getString(R.string.error_invalid_mobile_number));
        }
        return flag;
    }

/*

    private void displayMessage(Profile profile){
        Log.d("reponce", String.valueOf(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
*/


    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {

                textFbUserName.setText("");
                textFbEmail.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this, "User Logged out", Toast.LENGTH_LONG).show();
            } else
                loadUserProfile(currentAccessToken);
            loginButton.setReadPermissions("email");


        }
    };
}