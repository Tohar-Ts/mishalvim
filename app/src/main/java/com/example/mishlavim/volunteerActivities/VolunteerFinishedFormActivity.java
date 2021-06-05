package com.example.mishlavim.volunteerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mishlavim.R;

public class VolunteerFinishedFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_finished_form);
    }
    public void VolunteerMainActivity(View v){
        Intent i = new Intent(this, VolunteerMainActivity.class);
        startActivity(i);
        finish();
    }
}