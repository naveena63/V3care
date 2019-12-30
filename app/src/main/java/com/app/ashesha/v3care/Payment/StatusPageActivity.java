package com.app.ashesha.v3care.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.ashesha.v3care.BottomNavActivity;
import com.app.ashesha.v3care.R;

public class StatusPageActivity extends AppCompatActivity {
Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_page);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatusPageActivity.this,BottomNavActivity.class);
                startActivity(i);

            }
        });
    }
}
