package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mishlavim.login.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView username;
    private EditText name, email, password,password_confirm ,phone;
    private Validation validation;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

//        username = findViewById(R.id.user_name);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
//        phone = findViewById(R.id.phone);
        Button saveBTM = findViewById(R.id.updateBTM_vulo_setting);

//        validation = new Validation(emailEditText, null, passwordEditText, null,
//                loadingProgressBar, getResources());


        saveBTM.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {updateUserDetails();}

    private void updateUserDetails(){

    }
}