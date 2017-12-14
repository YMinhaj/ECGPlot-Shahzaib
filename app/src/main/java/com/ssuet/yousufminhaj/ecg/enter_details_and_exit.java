package com.ssuet.yousufminhaj.ecg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class enter_details_and_exit extends AppCompatActivity {
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details_and_exit);
        init();
    }

    private void init() {
        b1 = (Button) findViewById(R.id.enterdetails);
        listner();
    }

    private void listner() {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(enter_details_and_exit.this,BT_data.class);
                startActivity(i);
            }
        });
    }
}
