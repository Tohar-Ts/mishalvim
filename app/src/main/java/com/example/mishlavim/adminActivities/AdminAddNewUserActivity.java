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
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.function.Function;

public class AdminAddNewUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText;
    private Button addButton;
    private ProgressBar loadingProgressBar;
    private Spinner guidesSpinner;
    private RadioGroup typesRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Validation validation;

    private String newUserType;
    private String guideName, guideID;
    private ArrayList<String> listOfGuidesName,  listOfGuidesID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_user);

        //init xml views
        guidesSpinner = findViewById(R.id.spinner);
        emailEditText = findViewById(R.id.newEmail);
        userNameEditText = findViewById(R.id.newUserName);
        passwordEditText = findViewById(R.id.newPassword);
        verifyPasswordEditText = findViewById(R.id.verifyPassword);
        addButton = findViewById(R.id.addNewUser);
        loadingProgressBar = findViewById(R.id.registerLoading);
        typesRadioGroup = findViewById(R.id.typesRg);

        //init guides list
        setSpinner();

        //init firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //init validation class
        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        newUserType = FirebaseStrings.guideStr(); //default user type

        loadingProgressBar.setVisibility(View.GONE); //progress bar gone

        addButton.setOnClickListener(this);
        guidesSpinner.setOnItemSelectedListener(this);
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

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,listOfGuidesName);

        guidesSpinner.setAdapter(adapter);
    }

    public void checkUserType(View v) {
        int radioId = typesRadioGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = findViewById(radioId);
        String wantedType = (String) checkedRadioButton.getText();

        switch (wantedType) {
            case "מנהל":
                guidesSpinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.adminStr();
                break;
            case "מדריך":
                guidesSpinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.guideStr();
                break;
            default:
                newUserType = FirebaseStrings.volunteerStr();
                guidesSpinner.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void registerUser() {
        //validate input - if the input is invalid, don't continue.
        if (validation.validateInput())
            return;
        //parse input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = userNameEditText.getText().toString().trim();

        loadingProgressBar.setVisibility(View.VISIBLE);
        //register
        addUserToFirebase(userName, email, password);
    }

    private void showRegisterFailed(Integer msg) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }

    private void addUserToFirebase(String userName, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                       String newUserUid = mAuth.getCurrentUser().getUid();
                        Log.d("Admin add new user:", "Logged in successfully");
                        createNewUser(newUserUid, userName, email);
                    } else {
                        Log.d("Admin add new user:", "Logged in failed");
                        showRegisterFailed(R.string.register_auth_failed);
                    }
                });
    }

    private void createNewUser(String newUserUid, String userName, String email) {
        User user;

        //admin
        if (newUserType.equals(FirebaseStrings.adminStr()))
            user = new Admin(userName, newUserType, email, new HashMap<>(), new HashMap<>());

        //guide
        else if (newUserType.equals(FirebaseStrings.guideStr())) {
            user = new Guide(userName, newUserType, email, new HashMap<>());
            Admin.addGuide(newUserUid, userName); //TODO - pass this admin id
        }

        else { //volunteer
            user = new Volunteer(userName, newUserType, email, guideName, guideID, new HashMap<>(),"", false);
            Guide.addVolunteerByGuideId(guideID, newUserUid, userName);
            Admin.addVolunteer(newUserUid, userName); //TODO - pass this admin id
        }

        addUserToDb(newUserUid, user);
    }

    private void addUserToDb(String newUserUid, User user) {
        Function<Void,Void> onSuccess = this::updateDbSuccess;
        Function<Void,Void> onFailure = this::updateDbFailed;
        FirestoreMethods.createNewDocument(FirebaseStrings.usersStr(),newUserUid, user, onSuccess, onFailure);
    }

    private Void updateDbSuccess(Void unused){
        Toast.makeText(AdminAddNewUserActivity.this, newUserType + " was added successfully", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

    private Void updateDbFailed(Void unused){
        showRegisterFailed(R.string.register_failed);
        loadingProgressBar.setVisibility(View.GONE);
        return null;
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