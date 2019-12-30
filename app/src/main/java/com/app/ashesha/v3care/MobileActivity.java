package com.app.ashesha.v3care;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.Payment.PaymentOptionsActivity;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    RequestQueue requestQueue;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        editText = findViewById(R.id.edittextMobile);
        button = findViewById(R.id.submit);
        requestQueue = Volley.newRequestQueue(this);
        prefManager = new PrefManager(this);
        editText.setText(prefManager.getPhoneNumber());
        loadSavedPreferences();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int verify = validate();
                if (verify == 0) {
                    data();
                } else {
                    validate();
                }
            }

            private void data() {
                final String mobile = editText.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.Mobile_number, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Mobileresponse", "Mobileresponse" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")) {

                                Toast.makeText(MobileActivity.this, "mobile Number is updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MobileActivity.this, PaymentOptionsActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("" + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map params = new HashMap<String, String>();
                        params.put("user_id", new PrefManager(getApplicationContext()).getUserId());
                        params.put("phone", mobile);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });


    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        editText.setText(sharedPreferences.getString("string_et1",""));
    }
    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void saveData(){
        savePreferences("string_et1", editText.getText().toString());
    }
    @Override
    public void onBackPressed(){
        saveData();
        super.onBackPressed();
    }

    private int validate() {
        int flag = 0;
        if (editText.getText().toString().isEmpty()) {
            editText.setError("enter number");
            editText.requestFocus();
            flag = 1;
        }


        return flag;
    }

}
