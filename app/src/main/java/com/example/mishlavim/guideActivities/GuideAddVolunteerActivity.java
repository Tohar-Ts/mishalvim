package com.example.mishlavim.guideActivities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.AddUserDialog;
import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerFinishedFormActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GuideAddVolunteerActivity extends AppCompatActivity implements View.OnClickListener, AddUserDialog.addUserDialogListener, DeleteUser.deleteUserListener, BottomNavigationView.OnNavigationItemSelectedListener  {
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;
    private FirebaseUser fbUser;
    private Volunteer volunteer;
    BottomNavigationView navBarButtons;
    private Global globalInstance = Global.getGlobalInstance();
    private Guide guide = globalInstance.getGuideInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_add_volunteer);

        //init a the navbar selector variable
        navBarButtons=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.add_user);

        //activate a on click listener for the other buttons:
//        navBarButtons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch(item.getItemId()){
//                    case R.id.add_user:
//                        return true;
//                    case R.id.go_home:
//                        startActivity(new Intent(getApplicationContext(),GuideMainActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.forms:
//                        startActivity(new Intent(getApplicationContext(), VolunteerFinishedFormActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//                return false;
//            }
//        });


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
        navBarButtons.setOnNavigationItemSelectedListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user:
                return true;
            case R.id.go_home:
                startActivity(new Intent(getApplicationContext(), GuideMainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.forms:
                startActivity(new Intent(getApplicationContext(), GuideReportsActivity.class));
                overridePendingTransition(0, 0);
                return true;

        }
        return false;
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
        String myGuide = guide.getName();
        String guideID = mAuth.getUid();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbUser = mAuth.getCurrentUser(); // this is the new user we just added.
                        volunteer = new Volunteer(userName, FirebaseStrings.volunteerStr(), email, myGuide, guideID, new HashMap<>(), "",false);
                        // TODO: 6/5/2021 FIX THIS CONS.
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
        String usersCollection = FirebaseStrings.usersStr();
        String userId = fbUser.getUid();
        db.collection(usersCollection)
                .document(userId)
                .set(volunteer)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(GuideAddVolunteerActivity.this, "המשתמש נוצר בהצלחה!", Toast.LENGTH_SHORT).show();
                        userHasAdd();
                        loadingProgressBar.setVisibility(View.GONE);

                    } else {
                        showRegisterFailed();
                    }
                });
    }
    private void userHasAdd() {
        DialogFragment newFragment = new AddUserDialog();
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
        DialogFragment newFragment = new DeleteUser();
        newFragment.show(getSupportFragmentManager(), "deleteUser");
//        finish();
        // TODO: 5/23/2021 undo operations and delete the user from FB.
    }
    @Override
    public void onAddNeutralClick(DialogFragment dialog) {
        // User touched the dialog's Neutral button
        Log.d("guide", "onDialogNeutralClick:  after dialog closed");
        finish();
//        startActivity(new Intent(GuideAddVolunteerActivity.this, GuideMainActivity.class));

    }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(),guide.getMyVolunteers().get(userNameEditText.getText().toString().trim()),this::onDocumentDeleteSuccess, this::onDeleteFailed);
        loadingProgressBar.setVisibility(View.GONE);
        finish();
    }


    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) {
        DialogFragment newFragment = new AddUserDialog();
        newFragment.show(getSupportFragmentManager(), "addUser");
    }


    public Void onDocumentDeleteSuccess(Void noUse){
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), AuthenticationMethods.getCurrentUserID(),FirebaseStrings.myVolunteerStr(),userNameEditText.getText().toString().trim(),this::onKeyDeleteSuccess,this::onDeleteFailed);
        return null;
    }

    public Void onDeleteFailed(Void noUse){
        Toast.makeText(GuideAddVolunteerActivity.this, "המחיקה נכשלה! באסה!", Toast.LENGTH_SHORT).show();
        return null;
    }
    public Void onKeyDeleteSuccess(Void noUse){
        Toast.makeText(GuideAddVolunteerActivity.this, "המשתמש נמחק בהצלחה! אהוי!", Toast.LENGTH_SHORT).show();
        reloadScreen();
        return null;
    }

    private void reloadScreen() {
        finish();
        startActivity(getIntent());
    }
}
