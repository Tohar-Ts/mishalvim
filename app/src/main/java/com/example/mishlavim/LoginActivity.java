package com.example.mishlavim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.model.UserTypes;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private UserTypes userTypes;
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
        userTypes = new UserTypes();

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
                        FirebaseUser user = mAuth.getCurrentUser();
                        getUserType(user);
                    } else
                        showLoginFailed();
                });
    }

    private void getUserType(FirebaseUser user) {
        String usersCollection = userTypes.getUSER_COLLECTION();
        String userId = user.getUid();

        db.collection(usersCollection)
                .document(userId)
                .get()
                .addOnCompleteListener(LoginActivity.this, doc -> {
                    if (doc.isSuccessful())
                        redirectUserByType(doc.getResult().getData().get("type").toString());
                    else
                        showLoginFailed();
                });
    }

    private void redirectUserByType(String type) {

        if (type.equals(userTypes.getADMIN())) {
            Toast.makeText(this, "admin", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminAddNewUserActivity.class));
        } else if (type.equals(userTypes.getGUIDE())) {
            Toast.makeText(this, "guide", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, GuideMainActivity.class));
        } else if (type.equals(userTypes.getVOLUNTEER())) {
            Toast.makeText(this, "volunteer", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, VolunteerMainActivity.class));
        } else {
            showLoginFailed();
        }
        finish();

    }
}

//TODO - print logs