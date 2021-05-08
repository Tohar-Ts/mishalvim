package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
