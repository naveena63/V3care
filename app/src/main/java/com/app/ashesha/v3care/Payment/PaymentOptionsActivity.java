package com.app.ashesha.v3care.Payment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class PaymentOptionsActivity extends AppCompatActivity {
    Button online_payment, pay_after_sercie;
    String orderId;
    private TextView purpose_str;
    private TextView buyerName;
    PrefManager prefManager;
    private String totalPriceString;

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = PaymentOptionsActivity.this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }
    InstapayListener listener;
    private void  initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("instmojo_res","res"+response);
                paymentOrder();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG).show();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        prefManager = new PrefManager(this);
        totalPriceString = getIntent().getStringExtra("total_price");
        online_payment = findViewById(R.id.onlinePayment);
        pay_after_sercie = findViewById(R.id.cod);

        pay_after_sercie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentOptionsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentOptionsActivity.this, StatusPageActivity.class);
                startActivity(intent);
                paymentOrder();
            }
        });

        online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_Name = prefManager.getUsername();
                String mobile_Number = prefManager.getPhoneNumber();
                String user_Email = prefManager.getEmailId();
                String price=prefManager.getPrice();
                Toast.makeText(PaymentOptionsActivity.this,user_Name, Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentOptionsActivity.this, mobile_Number, Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentOptionsActivity.this, user_Email, Toast.LENGTH_SHORT).show();
                Toast.makeText(PaymentOptionsActivity.this, price, Toast.LENGTH_SHORT).show();
                Log.e("?","djddjd"+prefManager.getUsername());
                Log.e("dhdhhd","djddjd"+prefManager.getPhoneNumber());
                Log.e("dhdhhd","djddjd"+prefManager.getEmailId());
                Log.e("dhdhhd","djddjd"+prefManager.getPrice());
                callInstamojoPay(user_Email, mobile_Number, price, "For cleaning Services", user_Name);
                pay_after_sercie.setOnClickListener(v1 -> {
                    paymentOrder();
                });
            }
        });
    }

    private void paymentOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.PAYMENT_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              Log.e("payment response","payment respinse"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String order_id = jsonObject1.getString("order_id");
                        prefManager.storeValue(AppConstants.ORDER_ID, true);
                        prefManager.setOrderId(order_id);
                        Log.e("createoRDERiD", "ORDERID:" + order_id);
                        Intent intent = new Intent(PaymentOptionsActivity.this, StatusPageActivity.class);
                        startActivity(intent);
                        Toast.makeText(PaymentOptionsActivity.this, "success ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
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
                Map params = new HashMap<String, String>();
                params.put("user_id", prefManager.getUserId());
                Log.e("paymentUser_id", "user_id" + prefManager.getUserId());

                params.put("address", prefManager.getLoctaion());
                Log.e("loc", "loc is" + prefManager.getLoctaion());

                params.put("location", prefManager.getLandMark());
                Log.e("landmark", "landmark is" + prefManager.getLandMark());

                params.put("door_no", prefManager.getDoorNum());
                Log.e("doornum", "door num is" + prefManager.getDoorNum());

                params.put("service_date", prefManager.getDate());
                Log.e("service_date", "service_date" + prefManager.getDate());

                params.put("total", prefManager.getTotalPrice());
                Log.e("total_price", "total num is" + prefManager.getTotalPrice());

                params.put("payment_type", "cod");

                params.put("reward_points", prefManager.getCuttingPoints());
                Log.e("rewadspoints", "d is" + prefManager.getCuttingPoints());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}