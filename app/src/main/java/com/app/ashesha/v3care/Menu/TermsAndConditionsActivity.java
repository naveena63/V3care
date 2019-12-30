package com.app.ashesha.v3care.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TermsAndConditionsActivity extends AppCompatActivity {

TextView textView;
RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Terms And Conditions");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
       textView=findViewById(R.id.termsId);
       requestQueue= Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://www.v3care.com/api/Services/terms_conditions", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
Log.i("response","response"+response);
                        try {
                            String status=response.getString("status");
                            if(status.equals("success"))
                            {
                                JSONArray jsonArray=response.getJSONArray("terms_conditions");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                                   String t= jsonObject.getString("terms_desc");

                                    textView.setText(t);



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
