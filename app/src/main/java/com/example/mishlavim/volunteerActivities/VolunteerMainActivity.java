package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.mishlavim.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;


public class VolunteerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);
    }

    public void fillOutFormActivity(View view) {
        Intent intent = new Intent(VolunteerMainActivity.this, FillOutFormActivity.class);
        startActivity(intent);
    }
    public void viewOldFormActivity(View view) {
        Intent intent = new Intent(VolunteerMainActivity.this, ViewOldFormActivity.class);
        startActivity(intent);
    }
}