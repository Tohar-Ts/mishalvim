package com.example.mishlavim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.activities.volunteerActivities.volunteerMainActivity;
import com.example.mishlavim.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the content view (replacing `setContentView`)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonEnter.setText("היכנס");
    }

    public void nextActivity(View v){
        Intent i = new Intent(this, volunteerMainActivity.class);
        startActivity(i);
    }

}
