package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.mishlavim.R;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener{

    private String voluName;
    private String voluId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);
        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId =  getIntent().getStringExtra("CLICKED_VOLU_ID");
    }

    @Override
    public void onClick(View v) {

    }
}