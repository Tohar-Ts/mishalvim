package com.example.mishlavim.adminActivities;
import java.util.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;
import com.example.mishlavim.model.Validation;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminAddNewUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private String guideName, guideID;
    private ProgressBar loadingProgressBar;
    private RadioGroup typesRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Validation validation;
    private String newUserType;
    private Spinner spinner;
    private ArrayList<String> listOfGuidesName;
    private ArrayList<String> listOfGuidesID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_user);

        spinner = findViewById(R.id.spinner);
        emailEditText = findViewById(R.id.newEmail);
        userNameEditText = findViewById(R.id.newUserName);
        passwordEditText = findViewById(R.id.newPassword);
        EditText verifyPasswordEditText = findViewById(R.id.verifyPassword);
        Button addButton = findViewById(R.id.addNewUser);
        loadingProgressBar = findViewById(R.id.registerLoading);
        typesRadioGroup = findViewById(R.id.typesRg);

        setSpinner();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        newUserType = FirebaseStrings.guideStr(); //default

        loadingProgressBar.setVisibility(View.GONE);

        addButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    public void setSpinner(){
        //SPINNER SETUP
        //get the guides list.
        Global globalInstance = Global.getGlobalInstance();
        Admin admin = globalInstance.getAdminInstance();
        HashMap<String,String> guideList = admin.getGuideList();

        listOfGuidesName = new ArrayList<>(guideList.keySet());
        listOfGuidesID = new ArrayList<>(guideList.values());

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,listOfGuidesName);
        ad.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }

    public void checkUserType(View v) {
        int radioId = typesRadioGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(radioId);
        String wantedType = (String) checkedRadioButton.getText();

        switch (wantedType) {
            case "מנהל":
                spinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.adminStr();
                break;
            case "מדריך":
                spinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.guideStr();
                break;
            default:
                newUserType = FirebaseStrings.volunteerStr();
                spinner.setVisibility(View.VISIBLE);
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

        if (newUserType.equals(FirebaseStrings.adminStr()))
            user = new Admin(userName, newUserType, email, new HashMap<>(), new HashMap<>());

        else if (newUserType.equals(FirebaseStrings.guideStr())) {//guide
            user = new Guide(userName, newUserType, email, new HashMap<>(), new HashMap<>());
            Admin.addGuide(fbUser.getUid(), userName);
        }

        else { //volunteer
            user = new Volunteer(userName, newUserType, email, guideName, guideID, new HashMap<>(),"");
            Guide.addVolunteerByGuideName(fbUser.getUid(), (Volunteer) user);
            Admin.addVolunteer(fbUser.getUid(), userName);
        }

        addUserToDb(fbUser, user);
    }

    private void addUserToDb(FirebaseUser fbUser, User user) {
        db.collection(FirebaseStrings.usersStr())
                .document(fbUser.getUid())
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String key = listOfGuidesName.get(position);
        String value = listOfGuidesID.get(position);
        Log.d("guideID", "onItemSelected: guide name is " + key+" guide id is "+value);
        guideName = key;
        guideID = value;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}