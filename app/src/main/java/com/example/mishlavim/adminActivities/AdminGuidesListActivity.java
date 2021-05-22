package com.example.mishlavim.adminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;

import java.util.ArrayList;

public class AdminGuidesListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_guides_list);
        //Switch simpleSwitch = (Switch) findViewById(R.id.guideSwitch);
        //simpleSwitch.toggle();

        //create object of listview
        ListView listView=(ListView)findViewById(R.id.listview);

        //create ArrayList of String
        final ArrayList<String> arrayList=new ArrayList<>();

        //Add elements to arraylist
        arrayList.add("OLAF");
        arrayList.add("DANNY");
        arrayList.add("DAVID");
        arrayList.add("AYELET");
        arrayList.add("TOHAR");
        arrayList.add("ADI");
        arrayList.add("ELCHANAN");
        //Create Adapter
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        //assign adapter to listview
        listView.setAdapter(arrayAdapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AdminGuidesListActivity.this,"clicked item:"+i+" "+arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void AdminMainActivity(View v){
        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
    public void AdminAddNewUserActivity(View v){
        Intent i = new Intent(this, com.example.mishlavim.AdminAddNewUserActivity.class);
        startActivity(i);
    }

}