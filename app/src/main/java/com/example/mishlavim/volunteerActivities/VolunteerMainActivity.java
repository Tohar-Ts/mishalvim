package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;


public class VolunteerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);
    }

    public void fillOutFormActivity(View v) {
        Intent i = new Intent(this, fillOutFormActivity.class);
        startActivity(i);
    }
    public void viewOldFormActivity(View v){
        Intent i = new Intent(this, viewOldFormActivity.class);
        startActivity(i);
    }

}