package com.myapplication.mishlavim.login;

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

import com.myapplication.mishlavim.R;
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
        //init xml views
        emailForgotEditText = findViewById(R.id.emailForgot);
        sendButton = findViewById(R.id.send);
        btnBack = findViewById(R.id.btn_back);
        mAuth = FirebaseAuth.getInstance();

        sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = emailForgotEditText.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) { //error if email is empty
                        emailForgotEditText.setError("אימייל הינו שדה חובה, אנא מלא אותו");
                        return;
                    }

                    else {
                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) { //succeed
                                            Toast.makeText(forgotPassword.this, "הכנס לכתובת המייל שלך על מנת לאפס את הסיסמה", Toast.LENGTH_SHORT).show();
                                        } else { //error because the email isn't valid
                                            emailForgotEditText.setError("אימייל אינו תקין");
                                        }
                                    }
                                });
                    }

                }

            });

            btnBack.setOnClickListener(new View.OnClickListener() { //go back to login activity
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(forgotPassword.this, LoginActivity.class));
                    overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                    finish();
                }
            });
        }

    }