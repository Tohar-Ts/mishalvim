package com.example.mishlavim.guideActivities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.Validation;
import com.example.mishlavim.dialogs.*;
import com.example.mishlavim.model.GlobalUserDetails;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.UserTypes;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class GuideAddVolunteerActivity extends AppCompatActivity implements View.OnClickListener, addUserDialog.addUserDialogListener, deleteUser.deleteUserListener  {
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;
    private FirebaseUser fbUser;

    private GlobalUserDetails globalInstance = GlobalUserDetails.getGlobalInstance();
    private Guide guide = globalInstance.getGuideInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_add_volunteer);

        emailEditText = findViewById(R.id.newEmail);
        userNameEditText = findViewById(R.id.newUserName);
        passwordEditText = findViewById(R.id.newPassword);
        EditText verifyPasswordEditText = findViewById(R.id.verifyPassword);
        Button addButton = findViewById(R.id.addNewUser);
        loadingProgressBar = findViewById(R.id.registerLoading);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        validation = new Validation(emailEditText,userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUser();
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
        String myGuide =  guide.getName();;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbUser = mAuth.getCurrentUser(); // this is the new user we just added.
                        Volunteer volunteer = new Volunteer(userName, UserTypes.getVOLUNTEER(), email, myGuide);
                        createNewUser(fbUser, volunteer);
                    } else
                        showRegisterFailed();
                });
    }

    private void createNewUser(FirebaseUser fbUser, Volunteer volunteer) {
        Guide.addVolunteerByGuideName(fbUser.getUid(), volunteer);
        addUserToDb(fbUser, volunteer);
    }

    private void addUserToDb(FirebaseUser fbUser, Volunteer volunteer) {
        String usersCollection = UserTypes.getUSER_COLLECTION();
        String userId = fbUser.getUid();

        db.collection(usersCollection)
                .document(userId)
                .set(volunteer)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(GuideAddVolunteerActivity.this, "Volunteer was added successfully", Toast.LENGTH_SHORT).show();
                        userHasAdd();
                        loadingProgressBar.setVisibility(View.GONE);

                    } else {
                        showRegisterFailed();
                    }
                });
    }
    private void userHasAdd() {
        DialogFragment newFragment = new addUserDialog();
        newFragment.show(getSupportFragmentManager(), "addUser");
    }
    @Override
    public void onAddPositiveClick(DialogFragment dialog) {
        Log.d("guide", "onDialogPositiveClick: after dialog closed");
        finish();
        startActivity(new Intent(GuideAddVolunteerActivity.this, GuideAddVolunteerActivity.class));
    }

    @Override
    public void onAddNegativeClick(DialogFragment dialog) {
        Log.d("guide", "onDialogNegativeClick: after dialog closed");
        //Show dialog to confirm delete user.
        DialogFragment newFragment = new deleteUser();
        newFragment.show(getSupportFragmentManager(), "deleteUser");
//        finish();
        // TODO: 5/23/2021 undo operations and delete the user from FB.
    }
    @Override
    public void onAddNeutralClick(DialogFragment dialog) {
        // User touched the dialog's Neutral button
        Log.d("guide", "onDialogNeutralClick:  after dialog closed");
        finish();
        startActivity(new Intent(GuideAddVolunteerActivity.this, GuideMainActivity.class));

    }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        guide.deleteVolunteer(fbUser.getDisplayName());
        loadingProgressBar.setVisibility(View.GONE);
        finish();
        startActivity(new Intent(GuideAddVolunteerActivity.this, GuideMainActivity.class));

    }


    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) {
        DialogFragment newFragment = new addUserDialog();
        newFragment.show(getSupportFragmentManager(), "addUser");
    }
}
