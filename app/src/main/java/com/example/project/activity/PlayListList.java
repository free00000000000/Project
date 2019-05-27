package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project.R;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class PlayListList extends AppCompatActivity {
    File myDir;
    File[] music_list;
    String[] files_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_list);
        myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "musiccc/");
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
        Intent intent = new Intent(this, PlayList.class);
        intent.putExtra("wav", name);
        Log.d("myTag", "Next");
        startActivity(intent);

    }
}
