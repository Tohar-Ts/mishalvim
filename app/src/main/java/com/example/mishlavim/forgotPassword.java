package com.example.mishlavim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mishlavim.adminActivities.AdminMainActivity;
import com.example.mishlavim.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {
    private EditText emailForgotEditText;
    private Button sendButton;
    private TextView btnBack;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForgotEditText = findViewById(R.id.emailForgot);
        sendButton = findViewById(R.id.send);
        btnBack = findViewById(R.id.btn_back);
        mAuth = FirebaseAuth.getInstance();

        sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = emailForgotEditText.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(forgotPassword.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(forgotPassword.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    startActivity(new Intent(forgotPassword.this, LoginActivity.class));

                }

            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(forgotPassword.this, LoginActivity.class));
                }
            });
        }

    }