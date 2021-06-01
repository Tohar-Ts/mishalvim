package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mishlavim.R;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class GuideMainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView guideName;
//    private Button voluListButton,formsListButton, settingButton, reportsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);
        guideName = findViewById(R.id.guideName);
        BottomNavigationView a=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        a.setSelectedItemId(R.id.go_home);
//        voluListButton = findViewById(R.id.guideVolunteerList);
//        formsListButton = findViewById(R.id.guideFormsList);
//        settingButton = findViewById(R.id.guideSetting);
//        reportsButton = findViewById(R.id.guideReports);
//
//        voluListButton.setOnClickListener(this);
//        formsListButton.setOnClickListener(this);
//        settingButton.setOnClickListener(this);
//        reportsButton.setOnClickListener(this);

        setGuideName();
    }

    private void setGuideName() {
        Global globalInstance = Global.getGlobalInstance();
        Guide guide = globalInstance.getGuideInstance();
        guideName.setText(guide.getName());
    }

//    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.guideVolunteerList)
//            startActivity(new Intent(GuideMainActivity.this, GuideVolunteerListActivity.class));
//
//        else if (v.getId() == R.id.guideFormsList)
//            startActivity(new Intent(GuideMainActivity.this, GuideAddVolunteerActivity.class));
//
//        else if (v.getId() == R.id.guideSetting)
//            startActivity(new Intent(GuideMainActivity.this, GuideAddVolunteerActivity.class));
//
//        else if (v.getId() == R.id.guideReports)
//            startActivity(new Intent(GuideMainActivity.this, GuideAddVolunteerActivity.class));
    }
    }