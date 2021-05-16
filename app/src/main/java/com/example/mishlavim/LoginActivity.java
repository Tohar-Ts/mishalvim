package com.example.mishlavim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.adminActivities.AdminMainActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final int passwordLength = 5;
    private final String ADMIN = "admin";
    private final String GUIDE = "guide";
    private final String VOLUNTEER = "volunteer";
    private final String USER_COLLECTION = "users";
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isValid = validateInput(email, password);
        if (!isValid)
            return;


        loadingProgressBar.setVisibility(View.VISIBLE);
        loginToFirebase(email, password);
    }

    private boolean validateInput(String email, String password) {
        if (!isEmailValid(email)) {
            setError(emailEditText, R.string.invalid_email);
            return false;
        }
        if (!isPasswordValid(password)) {
            setError(passwordEditText, R.string.invalid_password);
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > passwordLength;
    }

    private void setError(EditText onEditText, Integer errorMsg) {
        onEditText.setError(getString(errorMsg));
        onEditText.requestFocus();
        loadingProgressBar.setVisibility(View.GONE);
        return;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void loginToFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                    getUserType(task);
                else
                    showLoginFailed(R.string.login_failed);
            }
        });
    }

    private void getUserType(@NonNull Task<AuthResult> task) {
        FirebaseUser user = task.getResult().getUser();

        db.collection(USER_COLLECTION).document(user.getUid()).get().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> doc) {

                if (doc.isSuccessful())
                    redirectUserByType(doc.getResult().getData().get("type").toString());
                else
                    showLoginFailed(R.string.login_failed);
            }
        });
    }

    private void redirectUserByType(String type) {

        switch (type) {
            case ADMIN:
                Toast.makeText(this, "admin", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                break;
            case GUIDE:
                Toast.makeText(this, "guide", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, GuideMainActivity.class));
                break;
            case VOLUNTEER:
                Toast.makeText(this, "volunteer", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, VolunteerMainActivity.class));
                break;
            default:
                showLoginFailed(R.string.login_failed);
                break;
        }

        finish();
    }
}

