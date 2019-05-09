package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.project.R;


public class SendToServerActivity extends AppCompatActivity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_server);
        setTitle("Step 2");
        Intent intent = getIntent();
        String img_name = intent.getExtras().getString("img_name");
        String name = img_name.split("\\.")[0];
        Log.d("myTag", name);

        for (int i = 0; i < 10; i++) {
            Log.d("myTag", "tick" + Integer.toString(i));

        }

    }
}
