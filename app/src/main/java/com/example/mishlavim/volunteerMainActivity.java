package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.mishlavim.databinding.ActivityMainBinding;


public class volunteerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);
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