package com.example.mishlavim.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.forgotPassword;
import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Objects;

//TODO - forgot password function on click
//TODO - check if user is signed in. if yes - skip the login, init and redirect .

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private TextView forPasswordEditText;
    private Validation validation;
    Global globalInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //check if user is logged in
        //TODO
        //init xml views
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        forPasswordEditText = findViewById(R.id.forget_password_btn);
        //init validation class
        validation = new Validation(emailEditText, null, passwordEditText, null,
                loadingProgressBar, getResources());
        globalInstance = Global.getGlobalInstance();

        loginButton.setOnClickListener(this);
        forPasswordEditText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.forget_password_btn){
            finish();
            startActivity(new Intent(getApplicationContext(), forgotPassword.class));
        }
        if(v.getId() ==  R.id.login){
            userLogin();
        }
    }

    private void showError(Integer msg) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void userLogin() {
        //validate input- if the input is invalid don't continue.
        if (validation.validateInput())
            return;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //visible progress bar
        loadingProgressBar.setVisibility(View.VISIBLE);

        //login to firebase
        AuthenticationMethods.signIn(email, password, this::loginSuccess, this::loginFailed);
    }

    private Void loginSuccess(String userUid){
        assert userUid != null;
        //init user id in the global class
        globalInstance.setUid(userUid);
        //get user data from firestore
        FirestoreMethods.getDocument(FirebaseStrings.usersStr(), userUid, this::getUserDocSuccess, this::getUserDocFailed);
        return null;
    }

    private Void loginFailed(Void unused){
        showError(R.string.login_auth_failed);
        return null;
    }

    private Void getUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        redirectUserByType(doc);
        return null;
    }

    private Void getUserDocFailed(Void unused){
        showError(R.string.login_failed);
        return null;
    }

    private void redirectUserByType(DocumentSnapshot document) {
        String type = Objects.requireNonNull(document.get(FirebaseStrings.typeStr())).toString();
        globalInstance.setType(type);

        //init admin and go to admin main screen
        if (type.equals(FirebaseStrings.adminStr())) {
            Admin admin = document.toObject(Admin.class);
            globalInstance.setAdminInstance(admin);
            startActivity(new Intent(LoginActivity.this, AdminNavigationActivity.class));
        }

        //init guide and go to guide main screen
        else if (type.equals(FirebaseStrings.guideStr())) {
            Guide guide = document.toObject(Guide.class);
            globalInstance.setGuideInstance(guide);
            startActivity(new Intent(LoginActivity.this, GuideMainActivity.class));
        }

        //volunteer and go to volunteer main screen
        else if (type.equals(FirebaseStrings.volunteerStr())) {
            Volunteer volu = document.toObject(Volunteer.class);
            globalInstance.setVoluInstance(volu);
            startActivity(new Intent(LoginActivity.this, VolunteerMainActivity.class));
        }

        //error
        else {
            showError(R.string.undefined_user);
        }

        loadingProgressBar.setVisibility(View.GONE);
    }

}
