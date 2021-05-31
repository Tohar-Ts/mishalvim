package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;

public class ViewOldFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_old_form);
    }
    public void VolunteerMainActivity(View v){
        Intent i = new Intent(this, VolunteerMainActivity.class);
        startActivity(i);
        finish();
    }
}