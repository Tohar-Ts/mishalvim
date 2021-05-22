package com.example.mishlavim.adminActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.Validation;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;
import com.example.mishlavim.model.UserTypes;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminAddNewUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText guideName;
    private ProgressBar loadingProgressBar;
    private RadioGroup typesRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;
    private String newUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_user);

        emailEditText = findViewById(R.id.newEmail);
        userNameEditText = findViewById(R.id.newUserName);
        passwordEditText = findViewById(R.id.newPassword);
        EditText verifyPasswordEditText = findViewById(R.id.verifyPassword);
        guideName = findViewById(R.id.guideName);
        Button addButton = findViewById(R.id.addNewUser);
        loadingProgressBar = findViewById(R.id.registerLoading);
        typesRadioGroup = findViewById(R.id.typesRg);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        newUserType = UserTypes.getGUIDE(); //default

        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    public void checkUserType(View v) {
        int radioId = typesRadioGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(radioId);
        String wantedType = (String) checkedRadioButton.getText();

        switch (wantedType) {
            case "מנהל":
                guideName.setVisibility(View.GONE);
                newUserType = UserTypes.getADMIN();
                break;
            case "מדריך":
                guideName.setVisibility(View.GONE);
                newUserType = UserTypes.getGUIDE();
                break;
            default:
                newUserType = UserTypes.getVOLUNTEER();
                guideName.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void registerUser() {

        if (validation.validateInput())
            return;
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = userNameEditText.getText().toString().trim();

        loadingProgressBar.setVisibility(View.VISIBLE);
        registerToFirebase(userName, email, password);
    }

    private void showRegisterFailed() {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), R.string.register_failed, Toast.LENGTH_SHORT).show();
    }

    private void registerToFirebase(String userName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        createNewUser(fbUser, userName, email);
                    } else
                        showRegisterFailed();
                });
    }

    private void createNewUser(FirebaseUser fbUser, String userName, String email) {
        User user;

        if (newUserType.equals(UserTypes.getADMIN()))
            user = new Admin(userName, newUserType, email);

        else if (newUserType.equals(UserTypes.getGUIDE()))
            user = new Guide(userName, newUserType, email, new HashMap<>());

        else { //volunteer
            String myGuide = guideName.getText().toString().trim();
            user = new Volunteer(userName, newUserType, email, myGuide);
            Guide.addVolunteerByGuideName(fbUser.getUid(), (Volunteer) user);
        }

        addUserToDb(fbUser, user);
    }

    private void addUserToDb(FirebaseUser fbUser, User user) {
        String usersCollection = UserTypes.getUSER_COLLECTION();
        String userId = fbUser.getUid();

        db.collection(usersCollection)
                .document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminAddNewUserActivity.this, newUserType + " was added successfully", Toast.LENGTH_SHORT).show();
                        loadingProgressBar.setVisibility(View.GONE);
                    } else {
                        showRegisterFailed();
                    }
                });
    }
}