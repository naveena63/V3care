package com.app.ashesha.v3care.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GenerateReferalCode extends AppCompatActivity {
    TextView textViewrefid, textViewreferPoints;
    Button inviteButton;
    String refid;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_referalcode);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Refer Your Friends");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        textViewrefid = findViewById(R.id.textview_refid);
        inviteButton = findViewById(R.id.button_invite);
        textViewreferPoints = findViewById(R.id.textView_rpoints);

        prefManager = new PrefManager(this);
        getReferalId();
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                            "ReferalCode : "+refid);
                    startActivity(shareIntent);
                }catch (Exception e){
                    //   Toast.makeText(RewardPointsActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getReferalId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_REFERAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("refresposne", "refid" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                    {
                        refid = jsonObject.getString("referral_id");
                        textViewrefid.setText("Your referalId is:"+refid);
                        Log.e("referid","refid"+refid);
                    }
                    if (status.equalsIgnoreCase("false")){
                        Toast.makeText(GenerateReferalCode.this, "userid wrong", Toast.LENGTH_SHORT).show();
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
                map.put("user_id", prefManager.getUserId());
                Log.e("refUserid","ref"+prefManager.getUserId());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

