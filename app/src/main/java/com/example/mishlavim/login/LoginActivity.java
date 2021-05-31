package com.example.mishlavim.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminAddNewUserActivity;
import com.example.mishlavim.adminActivities.AdminMainActivity;
import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Validation;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        validation = new Validation(emailEditText, null, passwordEditText, null,
                loadingProgressBar, getResources());

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    private void userLogin() {
        if (validation.validateInput())
            return;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        loadingProgressBar.setVisibility(View.VISIBLE);
        loginToFirebase(email, password);
    }

    private void showLoginFailed() {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
    }

    private void loginToFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        assert fbUser != null;
                        getUserType(fbUser);
                    } else
                        showLoginFailed();
                });
    }

    private void getUserType(FirebaseUser fbUser) {
        String userId = fbUser.getUid();

        db.collection(FirebaseStrings.usersStr())
                .document(userId)
                .get()
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        redirectUserByType(document);
                    } else
                        showLoginFailed();
                });
    }

    private void redirectUserByType(DocumentSnapshot document) {
        String type = Objects.requireNonNull(document.get(FirebaseStrings.typeStr())).toString();
        Global globalInstance = Global.getGlobalInstance();
        globalInstance.setType(type);

        if (type.equals(FirebaseStrings.adminStr())) {

            Admin admin = document.toObject(Admin.class);
            globalInstance.setAdminInstance(admin);
            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));

        } else if (type.equals(FirebaseStrings.guideStr())) {

            Guide guide = document.toObject(Guide.class);
            globalInstance.setGuideInstance(guide);
            startActivity(new Intent(LoginActivity.this, GuideAddVolunteerActivity.class));

        } else if (type.equals(FirebaseStrings.volunteerStr())) {
            Volunteer volu = document.toObject(Volunteer.class);
            globalInstance.setVoluInstance(volu);
            startActivity(new Intent(LoginActivity.this, VolunteerMainActivity.class));

        } else {
            showLoginFailed();
        }

    }
}
