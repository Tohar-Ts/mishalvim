package com.example.mishlavim.guideActivities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.userExistDialog;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.User;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;

import java.util.ArrayList;
import java.util.HashMap;


public class GuideAddVolunteerFragment extends Fragment implements View.OnClickListener {

    private EditText emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText;
    private Button addButton;
    private ProgressBar loadingProgressBar;
    private Validation validation;
    private String voluName, voluID;
    Global globalInstance;
    String thisGuideUid;
    String thisGuideName;
    Volunteer newVolu;

    public GuideAddVolunteerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_add_volunteer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init xml views
        emailEditText = view.findViewById(R.id.voluNewEmail);
        userNameEditText = view.findViewById(R.id.voluNewUserName);
        passwordEditText = view.findViewById(R.id.voluNewPassword);
        verifyPasswordEditText = view.findViewById(R.id.voluVerifyPassword);
        addButton = view.findViewById(R.id.addNewVolu);
        loadingProgressBar = view.findViewById(R.id.voluRegisterLoading);

        globalInstance = Global.getGlobalInstance(); //init the global instance
        thisGuideUid = globalInstance.getUid(); //getting guide id
        if(thisGuideUid == null){
            Toast.makeText(getActivity(), "תקלה בהצגת המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
            return;
        }
        thisGuideName = globalInstance.getGuideInstance().getName(); //getting guide name

        //init validation class
        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());
        newVolu = new Volunteer(); //init new volunteer data

        loadingProgressBar.setVisibility(View.GONE); //progress bar gone

        addButton.setOnClickListener(this); //buttons listeners
    }


    @Override
    public void onClick(View v) {
        registerVolunteer();
    }

    private void showRegisterFailed(Integer msg) {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void registerVolunteer() {
        //validate input - if the input is invalid, don't continue.
        if (validation.validateInput())
            return;
        //parse input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = userNameEditText.getText().toString().trim();

        newVolu = new Volunteer(userName, FirebaseStrings.volunteerStr(), email, thisGuideName, thisGuideUid, new HashMap<>(),"", false, new HashMap<>(), "");

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
        Guide.addVolunteer(thisGuideUid, newUserUid, newVolu.getName());
        //init a new user data in firestore
        FirestoreMethods.createNewDocument(FirebaseStrings.usersStr(),newUserUid, newVolu, this::updateDbSuccess,this::updateDbFailed);
    }

    private Void updateDbSuccess(Void unused){
        Toast.makeText(getActivity(), "volunteer was added successfully", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        //updating global to see changes
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }

    private Void updateDbFailed(Void unused){
        showRegisterFailed(R.string.register_failed);
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }
    private Void updateGlobalFinished(Boolean status){
        if(status)
            Toast.makeText(getActivity(), "המידע עודכן בהצלחה", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "תקלה בעדכון המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }
}