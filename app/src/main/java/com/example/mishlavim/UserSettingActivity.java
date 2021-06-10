package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideNavigationActivity;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Before using the class you have to pass the activity: "CLICKED_USER_TYPE" and"CLICKED_USER_ID".
 * You also have to init the user to update data in the global class.
 */
public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout loginLayout, settingLayout;
    private Button loginBtn, updateBtn;
    private FloatingActionButton homeBtn;
    private ProgressBar progressBar;
    private Global global;
    private String userUpdatingType ,userToUpdateType, userToUpdateUid;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        loginLayout = findViewById(R.id.SettingLoginLayout);
        settingLayout = findViewById(R.id.SettingLayout);
        loginBtn = findViewById(R.id.SettingLogin);
        updateBtn = findViewById(R.id.settingUpdateBtn);
        homeBtn = findViewById(R.id.settingHomeFloating);
        progressBar = findViewById(R.id.settingLoading);

        //showing the login again screen
        loginLayout.setVisibility(View.VISIBLE);
        settingLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        //init user data
        userToUpdateType = getIntent().getStringExtra("CLICKED_USER_TYPE");
        userToUpdateUid =  getIntent().getStringExtra("CLICKED_USER_ID");
        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();
        initUserDataByType();

        //init listeners
        loginBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SettingLogin:
               parseLoginInput();
                break;
            case R.id.settingUpdateBtn:
                parseSettingInput();
                break;
            case R.id.settingHomeFloating:
                //clicking on go back home button - switch activities
                switchToHomeByUser();
                break;
        }
    }


    private void initUserDataByType() {
        if(userToUpdateType.equals(FirebaseStrings.adminStr())){
            userData = global.getAdminInstance();
        }
        else if(userToUpdateType.equals(FirebaseStrings.guideStr())){
            userData = global.getGuideInstance();
        }
        else if(userToUpdateType.equals(FirebaseStrings.volunteerStr())){
            userData = global.getVoluInstance();
        }
    }

    private void parseSettingInput() {
        //TODO
        switchToHomeByUser();
    }

    private void parseLoginInput() {
        //showing setting screen
        progressBar.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        settingLayout.setVisibility(View.VISIBLE);
        showCurrentData();
        progressBar.setVisibility(View.GONE);
    }

    private void showCurrentData() {
    }

    private void switchToHomeByUser() {
        //admin is in the setting screen
        if(userUpdatingType.equals(FirebaseStrings.adminStr())){
            startActivity(new Intent(getApplicationContext(), AdminNavigationActivity.class));
        }
        //guide is in the setting screen
        else{
            startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        }
        finish();
        overridePendingTransition(0, 0);
    }
}