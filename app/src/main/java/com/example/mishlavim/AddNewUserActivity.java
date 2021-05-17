package com.example.mishlavim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.model.User;
import com.example.mishlavim.model.UserTypes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, userNameEditText, passwordEditText;
    private ProgressBar loadingProgressBar;
    private RadioGroup typesRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;
    private UserTypes userTypes;
    private String newUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        emailEditText = findViewById(R.id.newEmail);
        userNameEditText = findViewById(R.id.newUserName);
        passwordEditText = findViewById(R.id.newPassword);

        Button addButton = findViewById(R.id.addNewUser);
        loadingProgressBar = findViewById(R.id.registerLoading);
        typesRadioGroup = findViewById(R.id.typesRg);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        validation = new Validation(emailEditText, passwordEditText, userNameEditText,
                loadingProgressBar, getResources());
        userTypes = new UserTypes();
        newUserType = userTypes.getVOLUNTEER(); //default

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
                newUserType = userTypes.getADMIN();
                break;
            case "מדריך":
                newUserType = userTypes.getGUIDE();
                break;
            default:
                newUserType = userTypes.getVOLUNTEER();
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

    private void registerToFirebase(String userName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        addUserToDb(fbUser, userName, email);
                    } else
                        showRegisterFailed();
                });
    }

    private void addUserToDb(FirebaseUser fbUser, String userName, String email) {
        String usersCollection = userTypes.getUSER_COLLECTION();
        String userId = fbUser.getUid();

        User user = new User(userName, newUserType, email);

        db.collection(usersCollection)
                .document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddNewUserActivity.this, newUserType + " was added successfully", Toast.LENGTH_SHORT).show();
                        loadingProgressBar.setVisibility(View.GONE);
                    } else {
                        showRegisterFailed();
                    }

                });
    }


}