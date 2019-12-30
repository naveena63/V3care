package com.app.ashesha.v3care.Cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.Location.LocationActivity;
import com.app.ashesha.v3care.BottomNavActivity;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.ApiCallingFlow;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;
import com.app.ashesha.v3care.Utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    public static String TAG = "CartActivity";

    public static final String KEY_USERID = "user_id";
    public static final String KEY_CART_ID = "cart_id";
    String totalPrice;
    RecyclerView recyclerView;
    CartAdapter adapter;
    Button TotalAmount, addService, couponCode;
    Integer couponAmount;
    ViewGroup viewGroup;
    List<CartModel> timeModels;
    private ApiCallingFlow apiCallingFlow;
     PrefManager prefManager;
    TextView noCartProductsTextView, rewardPoints;
    EditText editTextCoupon;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        noCartProductsTextView = findViewById(R.id.no_cart_available);
        TotalAmount = findViewById(R.id.continue_payment_text);
        addService = findViewById(R.id.add_service_text);
        couponCode = findViewById(R.id.coupon_code);
        timeModels = new ArrayList<>();
        requestQueue=Volley.newRequestQueue(this);
        prefManager = new PrefManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        rewardPoints = (TextView) findViewById(R.id.reward_points);
        CheckBox checkBox = findViewById(R.id.check_box);


        TotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContinue();
            }
        });
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickAddService();
            }
        });
        couponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Enter Coupon");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                View viewInflated = LayoutInflater.from(CartActivity.this).inflate(R.layout.dialogue, (ViewGroup) viewGroup, false);
                Button couponButn = (Button) viewInflated.findViewById(R.id.btnDilougue);
                editTextCoupon = viewInflated.findViewById(R.id.enter_coupon);
                couponButn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String couponCode = editTextCoupon.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.COUPON_CODE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.i("copuonResponse", "couponResponse" + response);

                                    String status = jsonObject.getString("status");

                                    if (status.equals("1")) {
                                        String couponTotal = jsonObject.getString("total");
                                        String couponCode = jsonObject.getString("coupon_code");

                                        prefManager.storeValue(AppConstants.CODE_COUPON, couponCode);
                                        prefManager.setCouponCode("" + couponCode);
                                        Log.e("coupon_code", "couponcode" + couponCode);

                                        couponAmount = jsonObject.getInt("coupon_amount");
                                        prefManager.storeValue(AppConstants.COUPON_AMOUNT, couponAmount);
                                        prefManager.setCouponAmount("" + couponAmount);

                                        editTextCoupon.setText(couponCode);
                                        TotalAmount.setText(couponTotal);
                                        Toast.makeText(CartActivity.this, "coupon Applied", Toast.LENGTH_SHORT).show();
                                    } else if (status.equals("fail")) {
                                        Toast.makeText(CartActivity.this, "coupon not valid or coupon already used", Toast.LENGTH_SHORT).show();
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

                                Map<String, String> map = new HashMap<>();
                                map.put("user_id", prefManager.getUserId());
                                map.put("coupon_code", couponCode);
                                map.put("total_amount", prefManager.getTotalPrice());
                                Log.e("yoal", "dhnswk" + prefManager.getTotalPrice());
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
                builder.setView(viewInflated);
                builder.show();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.cart_page));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isShown()) {
                    if (buttonView.isChecked()) {
                      // addRewardPoints();
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConstants.ADD_REWARD_POINTS, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.e("addRewrdPoint","AddRewd"+response);
                                    String status=jsonObject.getString("status");

                                    if(status.equals("success")){
                                        String grandTotal = jsonObject.getString("grand_total");
                                        String cuttinRewardPoints = jsonObject.getString("reward_points");
                                        String presentRewardPoints = jsonObject.getString("present_reward_points");

                                        TotalAmount.setText(grandTotal);
                                        rewardPoints.setText(presentRewardPoints);
                                        prefManager.storeValue(AppConstants.CUTTINGPOINTS,cuttinRewardPoints);
                                        prefManager.setCuttingPoints(cuttinRewardPoints);

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
                                Map params = new HashMap<String, String>();
                                params.put("user_id", prefManager.getUserId());
                                params.put("total", prefManager.getTotalPrice());
                                Log.e("useridreward", "response" + prefManager.getUserId());
                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                    } else {
                         rewardPoints.setText(prefManager.getRewardsPoints());
                        TotalAmount.setText(prefManager.getTotalPrice());
                    }
                }
            }
        });
        requestServiceApi();
        getRewardsPoints();
    }

     public void getRewardsPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.GET_REWARDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Get Rewards response","rewards response  "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status=jsonObject.getString("status");
                        if(status.equals("success")){
                            String reward_points = jsonObject.getString("reward_points");
                            rewardPoints.setText(reward_points);
                            prefManager.storeValue(AppConstants.REWARD_POINTS, reward_points);
                            prefManager.setRewardsPoints(reward_points);
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = new HashMap<String, String>();
                params.put("user_id", prefManager.getUserId());
                Log.e("useridreward", "response" + prefManager.getUserId());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void onClickContinue() {
        if (totalPrice != null) {
            Intent intent = new Intent(CartActivity.this, LocationActivity.class);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            prefManager.setPrice("" + totalPrice);
        }
    }

    private void requestServiceApi() {
        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        apiCallingFlow = new ApiCallingFlow(this, parentLayout, true) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            timeModels(AppConstants.CART_ITEMS);
        }
    }

    protected void onCLickAddService() {
        Intent intent = new Intent(CartActivity.this, BottomNavActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    private void timeModels(final String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        apiCallingFlow.onSuccessResponse();

                        Log.e(TAG, "Response " + s);
                        String message = "";
                        String status;
                        JSONObject jsonMainObject;
                        JSONObject dataObject;

                        try {
                            jsonMainObject = new JSONObject(s);

                            status = jsonMainObject.getString("status");

                            if (status.equalsIgnoreCase("success")) {

                                JSONArray listArray = jsonMainObject.getJSONArray("cartitems");
                                JSONObject memberOject;

                                if (timeModels != null) {
                                    timeModels.clear();
                                }

                                for (int i = 0; i < listArray.length(); i++) {
                                    memberOject = listArray.getJSONObject(i);
                                    CartModel model = new CartModel();
                                    model.setId(memberOject.getString("id"));
                                    model.setServiceId(memberOject.getString("service_id"));
                                    model.setPackageId(memberOject.getString("package_id"));
                                    model.setPackageName(memberOject.getString("package_name"));
                                    model.setPrice(memberOject.getString("price"));
                                    model.setQuantity(memberOject.getString("qty"));
                                    model.setTimeSlot(memberOject.getString("time_slot"));
                                    model.setUserId(memberOject.getString("user_id"));
                                    model.setDate(memberOject.getString("date"));
                                    model.setServiceDate(memberOject.getString("service_date"));
                                    timeModels.add(model);
                                }
                                if (timeModels.size() > 0) {
                                    adapter = new CartAdapter(timeModels);
                                    recyclerView.setAdapter(adapter);
                                    noCartProductsTextView.setVisibility(View.GONE);
                                    adapter.setItemClickListener(new CartAdapter.OnItemClickListener() {
                                        @Override
                                        public void OnClick(View view, int position, String userID, String Cart_id) {
                                            deleteItems(userID, Cart_id);

                                        }
                                    });
                                }
                            } else {
                                noCartProductsTextView.setText(jsonMainObject.getString("msg"));
                                noCartProductsTextView.setVisibility(View.VISIBLE);
                            }
                            getCartTotalAmount();
                        } catch (Exception e) {
                            //  System.out.println(TAG + " Exception=======>" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        apiCallingFlow.onErrorResponse();
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                Log.e("user_id", "user_id===>" + prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private void getCartTotalAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.CART_TOTAL_AMOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        apiCallingFlow.onSuccessResponse();

                        Log.e(TAG, "Response " + s);
                        String message = "";
                        String status;
                        JSONObject jsonMainObject;
                        JSONObject dataObject;

                        try {

                            jsonMainObject = new JSONObject(s);

                            status = jsonMainObject.getString("status");

                            if (status.equalsIgnoreCase("success")) {
                                totalPrice = jsonMainObject.getString("cart_total");
                                TotalAmount.append(String.valueOf("\n" + getResources().getString(R.string.price_symbl) + jsonMainObject.getString("cart_total")));
                                prefManager.storeValue(AppConstants.TOTAL_PRICE, totalPrice);
                                prefManager.setTotalPrice("" + totalPrice);
                            }

                        } catch (Exception e) {
                            System.out.println(TAG + " Exception=======>" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        apiCallingFlow.onErrorResponse();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();

                params.put("user_id", prefManager.getString(AppConstants.APP_LOGIN_USER_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    private void deleteItems(String userID, String cart_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.DELETE_CART,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        apiCallingFlow.onSuccessResponse();
                        try {
                            JSONObject object = new JSONObject(response);

                            String status = object.getString("status");

                            String msg = object.getString("msg");
                            switch (status) {
                                case "success":
                                    Singleton.getInstance().cartItemsCount = (timeModels.size() - 1);
                                    Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    // getCartTotalAmount();
                                    break;
                                case "0":
                                    Toast.makeText(CartActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(CartActivity.this, "Error Occured Please Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("DELETE CART", "_--------------DELETE Response----------------" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiCallingFlow.onErrorResponse();
                        error.printStackTrace();
                        Toast.makeText(CartActivity.this, "Something Went wrong.. try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERID, userID);
                map.put(KEY_CART_ID, cart_id);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void refreshCartAmount(Context context) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        finish();
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