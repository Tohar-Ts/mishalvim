package com.myapplication.mishlavim.volunteerActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.myapplication.mishlavim.R;

public class VolunteerFinishedFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_finished_form);
    }

    /**
     * this function displays a finished form activity
     * @param v the view of the application
     */
    public void VolunteerMainActivity(View v){
        Intent i = new Intent(this, VolunteerMainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
    }
}