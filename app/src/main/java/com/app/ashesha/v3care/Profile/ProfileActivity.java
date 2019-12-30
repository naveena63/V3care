
package com.app.ashesha.v3care.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ashesha.v3care.Cart.CartActivity;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.PrefManager;

public class ProfileActivity extends AppCompatActivity {
    PrefManager prefManager;
    private TextView name, email,phone;
    Button button;
    ImageView user_profile_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefManager = new PrefManager(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.profile));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (TextView) findViewById(R.id.tv_name);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        email = (TextView) findViewById(R.id.tv_email);
        phone = (TextView) findViewById(R.id.tv_phone);
        button = (Button) findViewById(R.id.updateBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,
                        EditProfileActivity.class);
                startActivity(intent);
            }
        });
        name.setText(prefManager.getUsername());
        email.setText(prefManager.getEmailId());
        phone.setText(prefManager.getPhoneNumber());

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
