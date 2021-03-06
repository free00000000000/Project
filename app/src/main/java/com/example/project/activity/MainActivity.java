package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.project.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void uploadImage(View view) {
        Intent intent = new Intent(this, UploadImageActivity.class);
        startActivity(intent);
    }

    public void go_to_piano(View view){
        Intent intent = new Intent(this, PlayPianoActivity.class);
        startActivity(intent);
    }

    public void go_to_playlist(View view){
        Intent intent = new Intent(this,PlayListList.class);
        startActivity(intent);
    }
}
