package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mishlavim.R;

public class GuideVoluSettingActivity extends AppCompatActivity {

    private String voluToUpdateName, voluToUpdateId;
    private TextView voluName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_volu_setting);

        //innit xml views
        voluName = findViewById(R.id.edit_volu_name);


        //getting clicked volunteer details
        voluToUpdateName =  getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluToUpdateId =  getIntent().getStringExtra("CLICKED_VOLU_ID");

        voluName.setText(voluToUpdateName);

    }
}