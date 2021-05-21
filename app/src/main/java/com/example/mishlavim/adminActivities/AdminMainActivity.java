package com.example.mishlavim.adminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.volunteerActivities.fillOutFormActivity;
import com.example.mishlavim.volunteerActivities.viewOldFormActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
    }
    public void AdminGuidesListActivity(View v) {
        Intent i = new Intent(this, AdminGuidesListActivity.class);
        startActivity(i);
    }
    public void AdminFormListActivity(View v){
        Intent i = new Intent(this, AdminFormListActivity.class);
        startActivity(i);
    }
    public void fillOutFormActivity(View v){
        Intent i = new Intent(this, fillOutFormActivity.class);
        startActivity(i);
    }
    public void viewOldFormActivity(View v){
        Intent i = new Intent(this, viewOldFormActivity.class);
        startActivity(i);
    }
}