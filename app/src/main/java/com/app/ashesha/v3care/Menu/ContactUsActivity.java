package com.app.ashesha.v3care.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsActivity extends AppCompatActivity {
    TextView textCompanyName, textcompanyMial, textNmber, address;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        textCompanyName = findViewById(R.id.companyName);
        textcompanyMial = findViewById(R.id.company_mail);
        textNmber = findViewById(R.id.company_phone);
        address = findViewById(R.id.company_addres);
        requestQueue = Volley.newRequestQueue(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Contact Us");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        loadData();
    }

    private void loadData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.CONTACT_US, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = response.getJSONArray("contact_details");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String compnayName = jsonObject.getString("company_name");
                            String mail = jsonObject.getString("company_mail");
                            String phone = jsonObject.getString("company_phone");
                            String addresses = jsonObject.getString("Address");

                            textCompanyName.setText(compnayName);
                            textcompanyMial.setText(mail);
                            textNmber.setText(phone);
                            address.setText(addresses);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
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
