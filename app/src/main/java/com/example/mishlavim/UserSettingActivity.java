package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideNavigationActivity;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.Function;

/**
 * Before using the class you have to pass the activity: "CLICKED_USER_TYPE" and"CLICKED_USER_ID".
 * You also have to init the user to update data in the global class.
 */
public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout loginLayout, settingLayout;
    private Button loginBtn, updateBtn;
    private TextView homeButton; //home button
    private ProgressBar progressBar;
    private Global global;
    private String userUpdatingType ,userToUpdateType, userToUpdateUid;
    private User userData;
    private Validation validation;
    EditText newEmailEditText, newUserNameEditText, newPasswordEditText, newVerifyPasswordEditText;
    String newUserName, newEmail, newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        loginLayout = findViewById(R.id.SettingLoginLayout);
        settingLayout = findViewById(R.id.SettingLayout);
        loginBtn = findViewById(R.id.SettingLogin);
        updateBtn = findViewById(R.id.settingUpdateBtn);
        homeButton = findViewById(R.id.settingHomeFloating);
        progressBar = findViewById(R.id.settingLoading);
        newEmailEditText = findViewById(R.id.settingNewEmail);
        newUserNameEditText = findViewById(R.id.settingNewUserName);
        newPasswordEditText = findViewById(R.id.settingNewPassword);
        newVerifyPasswordEditText = findViewById(R.id.settingVerifyPassword);
        //init user data
        userToUpdateType = getIntent().getStringExtra("CLICKED_USER_TYPE");
        userToUpdateUid =  getIntent().getStringExtra("CLICKED_USER_ID");

        //init validation class
        validation = new Validation(newEmailEditText, newUserNameEditText, newPasswordEditText, newVerifyPasswordEditText
                , progressBar, getResources());
        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();

        initUserDataByType();

        //showing the login again screen
        initLoginAgain();
        initHomeButton();

        //init listeners
        loginBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        homeButton.setOnClickListener(this);
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

        newEmailEditText.setText(userData.getEmail());
        newUserNameEditText.setText(userData.getName());
    }

    private void initHomeButton() {
        if(userUpdatingType.equals(FirebaseStrings.adminStr())) {
            homeButton.setBackgroundResource(R.drawable.nav_blue_corners);
        }
        else {
            homeButton.setBackgroundResource(R.drawable.nav_orange_corners);
        }
    }
    private void switchToHomeByUser() {
        //admin is in the setting screen
        if(userUpdatingType.equals(FirebaseStrings.adminStr())) {
            startActivity(new Intent(getApplicationContext(), AdminNavigationActivity.class));
        }
        //guide is in the setting screen
        else {
            startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        }
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
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
        //sign in again
        AuthenticationMethods.signIn(email,password, this::signInSuccess, this::showError);
    }

    private Void signInSuccess(String s) {
        initSetting();
        return null;
    }


    private Void showError(Void unused) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(UserSettingActivity.this, "עדכון נכשל", Toast.LENGTH_SHORT).show();
        return null;
    }

    private void parseSettingInput() {
        // validate input - if the input is invalid, don't continue.
        if (validation.validateInput())
            return;

        newEmail = newEmailEditText.getText().toString().trim();
        newPassword = newPasswordEditText.getText().toString().trim();
        newUserName = newUserNameEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        updateFirestore();
    }


    private void updateFirestore(){
        //Update email on firestore.
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),userToUpdateUid , FirebaseStrings.emailStr(),newEmail,
                this::updateUserName, this::showError);
    }

    private Void updateUserName(Void unused){
        //Update username on firestore.
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), userToUpdateUid , FirebaseStrings.nameStr(), newUserName,
                this::updateFirestoreSuccess, this::showError);
        return null;
    }

    private Void updateFirestoreSuccess(Void unused) {
        //update maps!
        if(userToUpdateType.equals(FirebaseStrings.volunteerStr()))//volunteer is stored in his guide map
            updateGuideVoluMap();
        else if(userToUpdateType.equals(FirebaseStrings.guideStr()))//guide is stored in his admin map
            updateAdminGuideMap();
        else  //admin details are not stored in outside maps
            Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }

    private void updateAdminGuideMap() {
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), global.getGuideInstance().getMyAdminId(), FirebaseStrings.guideListStr(), userData.getName()
                , this::onDeleteGuideMapKey, this::showError);
    }

    private Void onDeleteGuideMapKey(Void unused) {
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), global.getGuideInstance().getMyAdminId(), FirebaseStrings.guideListStr(),
                newUserName, userToUpdateUid
                , this::onUpdateMapKey, this::showError);
        return null;
    }

    private void updateGuideVoluMap() {
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), global.getUid(), FirebaseStrings.myVolunteerStr(), userData.getName()
                , this::onDeleteVoluMapKey, this::showError);
    }

    private Void onDeleteVoluMapKey(Void unused) {
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), global.getUid(), FirebaseStrings.myVolunteerStr(),
                newUserName, userToUpdateUid
                , this::onUpdateMapKey, this::showError);
        return null;
    }

    private Void onUpdateMapKey(Void unused) {
        Log.d(" UserSettingActivity:", "updated map successfully");
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }

    private Void updateGlobalFinished(Boolean status) {
        if (status)
            Toast.makeText(UserSettingActivity.this, "עדכון הסתיים בהצלחה", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(UserSettingActivity.this, "תקלה בעדכון המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        switchToHomeByUser();
        //updating auth
        updateAuth();
        return null;
    }

    private void updateAuth() {
        AuthenticationMethods.updateAuthEmail(newEmail);
        AuthenticationMethods.updateAuthPassword(newPassword);
    }
}