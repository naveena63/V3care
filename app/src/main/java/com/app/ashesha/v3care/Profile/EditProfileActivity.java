package com.app.ashesha.v3care.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.Cart.CartActivity;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.ApiCallingFlow;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    PrefManager prefManager;
    public static final String KEY_USERID = "user_id";
    public static final String KEY_MOBILE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    EditText editext1, editext2, editext3;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        prefManager = new PrefManager(this);
        editext1 = (EditText) findViewById(R.id.et_name);
        editext2 = (EditText) findViewById(R.id.et_email);
        editext3 = (EditText) findViewById(R.id.et_phonenum);

        submit = (Button) findViewById(R.id.subbtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestServiceApi();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editext1.setText(prefManager.getUsername());
        editext2.setText(prefManager.getEmailId());
        editext3.setText(prefManager.getPhoneNumber());
    }

    private void requestServiceApi() {

        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        ApiCallingFlow apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            editProfile();
        }
    }

    private void editProfile() {

        SharedPreferences sp = getApplicationContext().getSharedPreferences(
                "sharedPrefName", 0);


        final String userid = sp.getString("userid", "defaultvalue");
        final String userName = editext1.getText().toString();
        final String email = editext2.getText().toString();
        final String phonenumber = editext3.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.SAVE_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String myResponce = jsonObject.getString("status");

                            if (myResponce.equals("success")) {
//                                String user_id = jsonObject.getString("userid");
//                                SharedPreferences sp = getApplicationContext().getSharedPreferences(
//                                        "sharedPrefName", 0);
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.putString("userid", user_id);
//                                editor.apply();
                                Toast.makeText(EditProfileActivity.this, "Profile Successfulley updated", Toast.LENGTH_SHORT).show();
                                prefManager.setPhoneNumber(phonenumber);
                                prefManager.setEmailId(email);
                                prefManager.setUsername(userName);
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (myResponce.equals("error")) {

                                // Toast.makeText(EditProfileActivity.this, "Please Enter Valid otp", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("log", "_-------------- Response----------------" + response);
                        //   Toast.makeText(EditProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();
                        Log.i("uyt", "_----" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_MOBILE, phonenumber);
                map.put(KEY_EMAIL, email);
                map.put(KEY_NAME, userName);
                map.put(KEY_USERID, userid);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                break;


        }
        return super.onOptionsItemSelected(item);

    }

}

