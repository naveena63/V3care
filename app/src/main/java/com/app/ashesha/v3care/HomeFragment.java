package com.app.ashesha.v3care;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.app.ashesha.v3care.services.HomeScreenListener;
import com.app.ashesha.v3care.services.ServiceAdpter;
import com.app.ashesha.v3care.HomeBanners.Banner;
import com.app.ashesha.v3care.services.ServicesListModel;
import com.app.ashesha.v3care.Packages.PackagesActivity;
import com.app.ashesha.v3care.Utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment implements HomeScreenListener {

    public static final String NAMES = "category_name";
    public static final String IMAGES = "category_icon";
    private List<Banner> stringList;
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ViewPager mPager;
    private int currentPage;
    View rootView;
    RequestQueue requestQueue;
    RecyclerView recyclerView ;
    List<ServicesListModel> servicesList;
    private HomeScreenListener homeScreenListener;
    CircleIndicator indicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.logo);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        recyclerView = rootView.findViewById(R.id.recyclerView);
        images = new ArrayList<>();
        names = new ArrayList<>();
        servicesList = new ArrayList<>();
        indicator = rootView.findViewById(R.id.indicator);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        DividerItemDecoration itemDecor2 = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.addItemDecoration(itemDecor2);
        getSliderImages();
        getServices();
        return rootView;
    }

    private void getServices() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ALL_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("services");
                                Log.i("notlogin", "_-----------" + jsonArray);
                                showGrid(jsonArray);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("HOME", "_--------------CATEGORY Response----------------" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Something Went wrong.. try again", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showGrid(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = null;
            try {
                obj = jsonArray.getJSONObject(i);
                images.add(obj.getString(IMAGES));
                names.add(obj.getString(NAMES));
                ServicesListModel model = new ServicesListModel();
                model.setServiceId(obj.getString("id"));
                model.setServiceImage(obj.getString(IMAGES));
                model.setServiceName(obj.getString(NAMES));

                servicesList.add(model);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ServiceAdpter adapter = new ServiceAdpter(servicesList, homeScreenListener);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClickGridItem(String serviceId, String serviceName) {
        Intent intent = new Intent(getActivity(), PackagesActivity.class);
        intent.putExtra("serviceId", serviceId);
        intent.putExtra("service_name", serviceName);
        startActivity(intent);

    }


    private void getSliderImages() {
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.SLIDER_BANNERS,
                response -> {
                    Log.i("bannerresp","banner"+response);
                    stringList = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("banners");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String image = object.getString("banner_image");
                                Banner banner = new Banner();
                                banner.setBannerImage(image);
                            /*    if (imageName != null) {
                                    banner.setImageName(imageName);
                                } else if (imageName.matches(".")) {
                                    banner.setImageName("Banners");
                                }*/
                                stringList.add(banner);
                                //where is picaso
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (stringList.size() > 0) {

                        mPager = rootView.findViewById(R.id.pager);
                        mPager.setAdapter(new MyAdapter(getActivity(), stringList));

                        indicator.setViewPager(mPager);
                        final Handler handler = new Handler();
                        final Runnable Update = () -> {
                            if (currentPage == stringList.size()) {
                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage++, true);
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 5000, 5000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}