package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideNavigationActivity;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
    private boolean showLoginAgain;
    private User userData;
    private Validation validation;
    EditText newEmail, newUserName, newPassword, newVerifyPassword;

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
        newEmail = findViewById(R.id.settingNewEmail);
        newUserName = findViewById(R.id.settingNewUserName);
        newPassword = findViewById(R.id.settingNewPassword);
        newVerifyPassword = findViewById(R.id.settingVerifyPassword);
        //init user data
        userToUpdateType = getIntent().getStringExtra("CLICKED_USER_TYPE");
        userToUpdateUid =  getIntent().getStringExtra("CLICKED_USER_ID");
        showLoginAgain = getIntent().getBooleanExtra("SHOW_LOGIN", true);

        //init validation class
        validation = new Validation(newEmail, newUserName, newPassword, newVerifyPassword
                , progressBar, getResources());
        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();
        initUserDataByType();

//        showing the login again screen
        if(showLoginAgain)
            initLoginAgain();
        else
            initSetting();

//        init listeners
        loginBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //        validate input - if the input is invalid, don't continue.
        if (validation.validateInput())
            return;
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

    private Void showError(Void unused) {
        Toast.makeText(UserSettingActivity.this, "עדכון נכשל", Toast.LENGTH_SHORT).show();
        return null;
    }

    private void parseSettingInput() {
        //TODO - add validation
        //TODO - update firebase
        switchToHomeByUser();
    }

    private void parseLoginInput() {

        progressBar.setVisibility(View.VISIBLE);
        EditText passwordText = findViewById(R.id.SettingPassword);

        String email = userData.getEmail();
        String password = passwordText.getText().toString().trim();

        if(password.isEmpty()){
            passwordText.setError("יש להזין סיסמה");
            passwordText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        AuthenticationMethods.signIn(email,password, this::signInSuccess, this::showError);
    }



    private Void signInSuccess(String s) {
        initSetting();
        return null;
    }

    private void initLoginAgain() {
        loginLayout.setVisibility(View.VISIBLE);
        settingLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        TextView emailText = findViewById(R.id.SettingEmail);
        emailText.setText(userData.getEmail());
    }

    private void initSetting() {
        loginLayout.setVisibility(View.GONE);
        settingLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        newEmail.setText(userData.getEmail());
        newUserName.setText(userData.getName());
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