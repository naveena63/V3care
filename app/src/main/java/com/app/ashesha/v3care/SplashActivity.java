package com.app.ashesha.v3care;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.app.ashesha.v3care.Packages.PackagesActivity;
import com.app.ashesha.v3care.TimeAndDate.TimesoltActivity;

import com.app.ashesha.v3care.Utils.AppConstants;
import com.app.ashesha.v3care.Utils.GlobalVariable;
import com.app.ashesha.v3care.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ImageView ivImage;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        getSupportActionBar().hide();
       ivImage=findViewById(R.id.text_logo);
        prefManager = new PrefManager(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        GlobalVariable.deviceWidth = displayMetrics.widthPixels;
        GlobalVariable.deviceHeight = displayMetrics.heightPixels;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, BottomNavActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }
        }, 3000);
    }


    }
