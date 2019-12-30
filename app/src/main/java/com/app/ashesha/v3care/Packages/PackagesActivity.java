package com.app.ashesha.v3care.Packages;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ashesha.v3care.R;

import com.app.ashesha.v3care.TimeAndDate.TimesoltActivity;

import com.app.ashesha.v3care.Utils.ApiCallingFlow;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PackagesActivity extends AppCompatActivity implements PackageListener {


    public static String TAG = "PackageActivity";
    List<PackageModel> packageModelslist;
    PackageAdapter packageAdapter;
    RecyclerView recyclerView;
    private String serviceId;
    public ApiCallingFlow apiCallingFlow;
    PrefManager prefManager;
    TextView noPackageAvailable;
    String packag = "package_name";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        prefManager = new PrefManager(this);

        noPackageAvailable = findViewById(R.id.no_packages_available);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.select_package));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        packageModelslist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        serviceId = getIntent().getStringExtra("serviceId");
        Log.i("serviceid", "naveena" + serviceId);
        recyclerView.setLayoutManager(new LinearLayoutManager(PackagesActivity.this));
        requestServiceApi();

    }

    private void requestServiceApi() {

        RelativeLayout parentLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        apiCallingFlow = new ApiCallingFlow(this, parentLayout, false) {
            @Override
            public void callCurrentApiHere() {
                requestServiceApi();
            }
        };
        if (apiCallingFlow.getNetworkState()) {
            packagesList(AppConstants.PACKAGES_URL, serviceId);

        }

    }

    public void packagesList(String packagesUrl, String serviceId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, packagesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        apiCallingFlow.onSuccessResponse();
                        Log.e(TAG, "Response " + packagesUrl);
                        Log.e(TAG, "Response " + s);
                        String status;
                        JSONObject jsonMainObject;
                        try {
                            jsonMainObject = new JSONObject(s);
                            status = jsonMainObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray listArray = jsonMainObject.getJSONArray("packages");
                                JSONObject memberObject;
                                if (packageModelslist != null) {
                                    packageModelslist.clear();
                                }
                                for (int i = 0; i < listArray.length(); i++) {
                                    memberObject = listArray.getJSONObject(i);
                                    PackageModel model = new PackageModel();
                                    model.setPackage_name(memberObject.getString(packag));
                                    model.setPackageId(memberObject.getString("id"));
                                    model.setService_id(memberObject.getString("service_id"));
                                    model.setPackageImage(memberObject.getString("package_image"));
                                    model.setPackage_price(memberObject.getString("package_price"));
                                    model.setSub_package_status(memberObject.getString("sub_package_status"));
                                    model.setExclusionl(memberObject.getString("package_desc"));

                                    packageModelslist.add(model);

                                }
                                if (packageModelslist.size() > 0) {
                                    packageAdapter = new PackageAdapter(packageModelslist, PackagesActivity.this);
                                    recyclerView.setAdapter(packageAdapter);
                                    noPackageAvailable.setVisibility(View.GONE);
                                }

                            } else {
                                noPackageAvailable.setText(jsonMainObject.getString("msg"));
                                noPackageAvailable.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            System.out.println(TAG + " Exception=======>" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        apiCallingFlow.onErrorResponse();

                        try {
                            System.out.println("volley error...." + volleyError.getMessage().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PackagesActivity.this, "Bad internet connection please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("service_id", prefManager.getServiceId());

                Log.e("hddhdh", "dhhdhdh" + prefManager.getServiceId());
//                Log.e("service_id", serviceId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PackagesActivity.this);
// stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onClickAddToCart(PackageModel packagesListModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_package", packagesListModel);
        Intent intent = new Intent(PackagesActivity.this, TimesoltActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
