package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.FTPManager;
import com.example.project.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;


public class SendToServerActivity extends AppCompatActivity {
    ProgressWheel wheel;
    String img_name;
    String name;
    boolean finish;
    FTPManager ftpm;
    Handler handler;
    Button play;
    Button home;
    Button downloadxml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_server);
        setTitle("Step 2");
        Intent intent = getIntent();
        img_name = intent.getExtras().getString("img_name");
        name = img_name.split("\\.")[0];
        Log.d("myTag", name);
        play = findViewById(R.id.button3);
        home = findViewById(R.id.button4);
        downloadxml = findViewById(R.id.button5);
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
                                handler.postDelayed(this, 5000);
                            } catch (Exception e) {
                                Log.d("myTag", e.getMessage()+" ");
                                Log.d("myTag", "ftp failed");
                            }

                        }
                        else {
                            Log.d("myTag", "conversion successed");
                            Toast.makeText(getApplicationContext(), "Complete conversion", Toast.LENGTH_SHORT).show();
                            download(name+".mp3");
                            wheel.setVisibility(View.INVISIBLE);
                            play.setVisibility(View.VISIBLE);
                            home.setVisibility(View.VISIBLE);
                            downloadxml.setVisibility(View.VISIBLE);
                            try {
                                ftpm.closeFTP();
                            } catch (Exception e) { }

                        }
                    }
                }, 5000);
            }
        } catch (Exception e) {
            Log.d("myTag", e.getMessage()+" ");
            Log.d("myTag", "ftp failed");
        }


    }

    private void download(String filename) {
        // TODO: change dir?
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "musiccc");
        if (!myDir.exists()) {
            myDir.mkdir();
            Toast.makeText(getApplicationContext(), "Directory not exist, create it", Toast.LENGTH_SHORT).show();
        }
        try {
            if(ftpm.downloadFile(filename, myDir.getPath())){
                Toast.makeText(getApplicationContext(), "Download success", Toast.LENGTH_SHORT).show();
                Log.d("myTag", "download success");
            } else {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
                Log.d("myTag", "download failed");
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getxml(View view) {
        try {
            if(ftpm.connect()) {
                download(name + ".xml");
                ftpm.closeFTP();
            }

        } catch (Exception e) {
            Log.d("myTag", "download xml failed: "+ e.getMessage());
        }
    }

    public void back_to_menu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
