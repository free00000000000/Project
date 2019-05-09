package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.project.FTPManager;
import com.example.project.R;
import com.pnikosis.materialishprogress.ProgressWheel;


public class SendToServerActivity extends AppCompatActivity {
    ProgressWheel wheel;
    String img_name;
    String name;
    boolean finish;
    FTPManager ftpm;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_server);
        setTitle("Step 2");
        Intent intent = getIntent();
        img_name = intent.getExtras().getString("img_name");
        name = img_name.split("\\.")[0];
        Log.d("myTag", name);

        wheel = findViewById(R.id.progress_wheel);
        wheel.setBarColor(Color.RED);

//        wheel.setVisibility(View.INVISIBLE);
        Log.d("myTag", "ftp connecting...");
        ftpm = new FTPManager();

        try {
            finish = false;
            if (ftpm.connect()) {

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!finish) {
                            try {
                                finish = ftpm.findFile(name+".mp3");
                                Log.d("myTag", "nnnnnnnnnnnnnn");
                                handler.postDelayed(this, 5000);


                            } catch (Exception e) {
                                Log.d("myTag", e.getMessage()+" ");
                                Log.d("myTag", "ftp failed");
                            }

                        }
                        else {
                            Log.d("myTag", "conversion successed");
                            Toast.makeText(getApplicationContext(), "Complete conversion", Toast.LENGTH_SHORT).show();
                            wheel.setVisibility(View.INVISIBLE);
                            try {
                                ftpm.closeFTP();
                            } catch (Exception e) { }

                        }
                    }
                }, 5000);
//                if(finish) {
//                    handler.removeCallbacks(myRunnable);
//                }
            }
        } catch (Exception e) {
            Log.d("myTag", e.getMessage()+" ");
            Log.d("myTag", "ftp failed");
        }
//        t.start();
//        Log.d("myTag", "conversion successed");
//        Toast.makeText(getApplicationContext(), "Complete conversion", Toast.LENGTH_SHORT).show();


    }

}
