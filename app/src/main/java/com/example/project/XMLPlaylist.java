package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.activity.PlayList;
import com.example.project.activity.PlayPianoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class XMLPlaylist extends AppCompatActivity {

    File myDir;
    File[] music_list;
    String[] files_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlplaylist);
        myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "musiccc/xml/");
        music_list = myDir.listFiles();
        files_name = new String[music_list.length];
        for(int i=0; i<music_list.length; ++i) {
            files_name[i] = music_list[i].getName();
        }

        ListView listview = findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files_name);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onClickListView);
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            goNext(files_name[position]);
//            Toast.makeText(PlayListList.this,"點選第 "+(position +1) +" 個 \n內容："+files_name[position], Toast.LENGTH_SHORT).show();
        }
    };

    public void goNext(String name) {
        Intent intent = new Intent(this, PlayPianoActivity.class);
        intent.putExtra("musicxml", name);
        Log.d("myTag", "Next");
        startActivity(intent);

    }

}
