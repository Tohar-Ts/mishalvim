package com.myapplications.mishlavim.adminActivities;


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

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.login.Validation;
import com.myapplications.mishlavim.model.Admin;
import com.myapplications.mishlavim.model.Firebase.AuthenticationMethods;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.Global;
import com.myapplications.mishlavim.model.Guide;
import com.myapplications.mishlavim.model.User;
import com.myapplications.mishlavim.model.Volunteer;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddNewUserFragment extends Fragment  implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener ,AdapterView.OnItemSelectedListener
{

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
    User newAuthUser;
    Global globalInstance;

    public AdminAddNewUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        globalInstance = Global.getGlobalInstance(); //init the global instance
        thisAdminUid = globalInstance.getUid(); //getting admin id
        //if admin is null:
        if(thisAdminUid == null){
            Toast.makeText(getActivity(), "???????? ?????????? ??????????, ???? ?????????? ???????????? ???? ?????????????????? ????????", Toast.LENGTH_SHORT).show();
            return;
        }

        //init guides list
        setSpinner();

        //init validation class
        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
                , loadingProgressBar, getResources());

        newUserType = FirebaseStrings.guideStr(); //default user type
        newAuthUser = new User();

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
    /*This function check the selection on the user when he creates a new User from the selection
    screen and shows the correct screen for his selection (he can choose either to create a new admin, guide or volunteer)
     */
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
        Admin admin = globalInstance.getAdminInstance();
        if(admin == null){
            Toast.makeText(getActivity(), "???????? ?????????? ??????????, ???? ?????????? ???????????? ???? ?????????????????? ????????", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,String> guideList = admin.getGuideList();
        //if guide list is empty show a msg
        if(guideList.isEmpty()){
            Toast.makeText(getActivity(), "?????????? ???????????????? ????????", Toast.LENGTH_SHORT).show();
            return;
        }
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

        newAuthUser.setEmail(email);
        newAuthUser.setName(userName);

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
            fsUser = new Admin(newAuthUser.getName(), newUserType, newAuthUser.getEmail(), new HashMap<>());

        //guide
        else if (newUserType.equals(FirebaseStrings.guideStr())) {
            fsUser = new Guide(newAuthUser.getName(), newUserType, newAuthUser.getEmail(), new HashMap<>(), globalInstance.getUid());
            Admin.addGuide(thisAdminUid, newAuthUser.getName(), newUserUid);
        }

        else { //volunteer
            fsUser = new Volunteer(newAuthUser.getName(), newUserType, newAuthUser.getEmail(), guideID, new HashMap<>(),"", false, new HashMap<>(), "");
            Guide.addVolunteer(guideID, newUserUid, newAuthUser.getName());
        }
        //init a new user data in firestore
        FirestoreMethods.createNewDocument(FirebaseStrings.usersStr(),newUserUid, fsUser, this::updateDbSuccess,this::updateDbFailed);
    }

    private Void updateDbSuccess(Void unused){
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
            Toast.makeText(getActivity(), "?????????? ?????????? ????????????", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "???????? ???????????? ??????????, ???? ?????????? ???????????? ???? ?????????????????? ????????", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }


}
