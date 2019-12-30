package com.app.ashesha.v3care;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.ashesha.v3care.Menu.MenuFragment;
import com.app.ashesha.v3care.Cart.CartActivity;
import com.app.ashesha.v3care.Orders.OrdersMainFragment;
import com.app.ashesha.v3care.Profile.ProfileActivity;

public class BottomNavActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
              fragment = new HomeFragment();
                break;

                case R.id.myOrders:
                fragment=new OrdersMainFragment();
                break;

            case R.id.action_cart:
              Intent i = new Intent(BottomNavActivity.this,CartActivity.class);
              startActivity(i);
                break;

            case R.id.profile:
                Intent intent = new Intent(BottomNavActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.menu:
                fragment=new MenuFragment();
                break;

        }
        return loadFragment(fragment);
    };

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        // Call the function callInstamojo to start payment here
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFragment());
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BottomNavActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
