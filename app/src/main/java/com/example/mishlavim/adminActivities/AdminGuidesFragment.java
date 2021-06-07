package com.example.mishlavim.adminActivities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mishlavim.R;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.User;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminGuidesFragment extends Fragment {

    public AdminGuidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        //init xml views
//        guidesSpinner = view.findViewById(R.id.spinner);
//        emailEditText = view.findViewById(R.id.newEmail);
//        userNameEditText = view.findViewById(R.id.newUserName);
//        passwordEditText = view.findViewById(R.id.newPassword);
//        verifyPasswordEditText = view.findViewById(R.id.verifyPassword);
//        addButton = view.findViewById(R.id.addNewUser);
//        loadingProgressBar = view.findViewById(R.id.registerLoading);
//        typesRadioGroup = view.findViewById(R.id.typesRg);
//
//        //init guides list
//        setSpinner();
//        //init validation class
//        validation = new Validation(emailEditText, userNameEditText, passwordEditText, verifyPasswordEditText
//                , loadingProgressBar, getResources());
//
//        newUserType = FirebaseStrings.guideStr(); //default user type
//        authUser = new User();
//
//        loadingProgressBar.setVisibility(View.GONE); //progress bar gone
//
//        addButton.setOnClickListener(this); //buttons listeners
//        typesRadioGroup.setOnCheckedChangeListener(this);
//        guidesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_guides, container, false);
    }
}