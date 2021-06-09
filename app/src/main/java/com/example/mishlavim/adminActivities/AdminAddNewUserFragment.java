package com.example.mishlavim.adminActivities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mishlavim.R;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;
import com.example.mishlavim.model.Volunteer;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddNewUserFragment extends Fragment  implements View.OnClickListener,RadioGroup.OnCheckedChangeListener , AdapterView.OnItemSelectedListener{

    private EditText emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText;
    private Button addButton;
    private ProgressBar loadingProgressBar;
    private Spinner guidesSpinner;
    private RadioGroup typesRadioGroup;
    private Validation validation;
    private String newUserType;
    private String guideName, guideID;
    private ArrayList<String> listOfGuidesName,  listOfGuidesID;
    String thisAdminUid;
    User authUser;

    public AdminAddNewUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisAdminUid = AuthenticationMethods.getCurrentUserID();
        // Inflate the layout for this fragment
         return inflater.inflate(R.layout.fragment_admin_add_new_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init xml views
        guidesSpinner = view.findViewById(R.id.guides_spinner);
        emailEditText = view.findViewById(R.id.newEmail);
        userNameEditText = view.findViewById(R.id.newUserName);
        passwordEditText = view.findViewById(R.id.newPassword);
        verifyPasswordEditText = view.findViewById(R.id.verifyPassword);
        addButton = view.findViewById(R.id.addNewUser);
        loadingProgressBar = view.findViewById(R.id.registerLoading);
        typesRadioGroup = view.findViewById(R.id.typesRg);

        //init guides list
        setSpinner();

        //init validation class
        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        newUserType = FirebaseStrings.guideStr(); //default user type
        authUser = new User();

        loadingProgressBar.setVisibility(View.GONE); //progress bar gone

        addButton.setOnClickListener(this); //buttons listeners
        typesRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.adminRb:
                guidesSpinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.adminStr();
                break;
            case R.id.guideRb:
                guidesSpinner.setVisibility(View.GONE);
                newUserType = FirebaseStrings.guideStr();
                break;
            default:
                newUserType = FirebaseStrings.volunteerStr();
                guidesSpinner.setVisibility(View.VISIBLE);
                break;
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listOfGuidesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guidesSpinner.setAdapter(adapter);
        guidesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //getting the clicked guide from the spinner
        String key = listOfGuidesName.get(position);
        String value = listOfGuidesID.get(position);
        Log.d("guideID", "onItemSelected: guide name is " + key+" guide id is "+value);
        guideName = key;
        guideID = value;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showRegisterFailed(Integer msg) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void registerUser() {
        //validate input - if the input is invalid, don't continue.
        if (validation.validateInput())
            return;
        //parse input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = userNameEditText.getText().toString().trim();

        authUser.setEmail(email);
        authUser.setName(userName);

        loadingProgressBar.setVisibility(View.VISIBLE);
        //register
        AuthenticationMethods.addNewUser(email,password,this::addAuthSuccess,this::addAuthFailed);
    }

    private Void addAuthFailed(Void unused) {
        showRegisterFailed(R.string.register_auth_failed);
        return null;
    }
    private Void addAuthSuccess(String newUserUid) {
        createNewUser(newUserUid);
        return null;
    }

    private void createNewUser(String newUserUid) {
        User fsUser;

        //admin
        if (newUserType.equals(FirebaseStrings.adminStr()))
            // TODO: 09/06/2021 add list of guides and form template
            fsUser = new Admin(authUser.getName(), newUserType, authUser.getEmail(), new HashMap<>(), new HashMap<>());

        //guide
        else if (newUserType.equals(FirebaseStrings.guideStr())) {
            fsUser = new Guide(authUser.getName(), newUserType, authUser.getEmail(), new HashMap<>());
            Admin.addGuide(thisAdminUid, newUserUid, authUser.getName());
        }

        else { //volunteer
            fsUser = new Volunteer(authUser.getName(), newUserType,authUser.getEmail(), guideName, guideID, new HashMap<>(),"", false, new HashMap<>(), "");
            Guide.addVolunteer(guideID, newUserUid, authUser.getName());
        }
        //init a new user data in firestore
        FirestoreMethods.createNewDocument(FirebaseStrings.usersStr(),newUserUid, fsUser, this::updateDbSuccess,this::updateDbFailed);
    }

    private Void updateDbSuccess(Void unused){
        Toast.makeText(getActivity(), newUserType + " was added successfully", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

    private Void updateDbFailed(Void unused){
        showRegisterFailed(R.string.register_failed);
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }


}
