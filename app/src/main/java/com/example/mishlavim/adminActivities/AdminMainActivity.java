package com.example.mishlavim.adminActivities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.volunteerActivities.fillOutFormActivity;
import com.example.mishlavim.volunteerActivities.viewOldFormActivity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import androidx.appcompat.widget.SwitchCompat;


public class AdminMainActivity extends AppCompatActivity {

    SwitchCompat simpleSwitch;
    RecyclerView guidesList;
    RecyclerView formsList;
    Button addNewUser;
    Button addNewForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        // initiate a Switch
        simpleSwitch = (SwitchCompat) findViewById(R.id.AdminSwitch);

        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->{
            switchOnClick();
        });
        //initiate the Recyclers
        guidesList = findViewById(R.id.guidesList);
        formsList =  findViewById(R.id.formsList);

        addNewUser = findViewById(R.id.buttonAddNewUser);
        addNewForm = findViewById(R.id.buttonFillNewForm);

        // TODO: 5/24/2021 make sure we start with forms not guides on the screen.

// check current state of a Switch (true or false).
//        Boolean switchState = simpleSwitch.isChecked();

//        guidesBTM = findViewById(R.id.guidesBTM);
//        formsBTM = findViewById(R.id.formsBTM);

        //set listener on the guides button
//        guidesBTM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                replaceFragment(new AdminGuidesListActivity());
//
//            }
//        });
        //set listener on the forms button
//        formsBTM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                replaceFragment(new AdminFormListActivity());
//
//            }
//        });
    }
    //this cylces between the fragments once the button is clicked
    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }
    //these are the other activities that are used within the fragments
    public void fillOutFormActivity(View v){
        Intent i = new Intent(this, fillOutFormActivity.class);
        startActivity(i);
    }
    public void createNewFormActivity(View v){
        Intent i = new Intent(this, CreateFormActivity.class);
        startActivity(i);
    }
    public void viewOldFormActivity(View v){
        Intent i = new Intent(this, viewOldFormActivity.class);
        startActivity(i);
    }
    public void AdminAddNewUserActivity(View v){
        Intent i = new Intent(this, AdminAddNewUserActivity.class);
        startActivity(i);
    }
    public void switchOnClick(){
        if(simpleSwitch.isChecked()){
            guidesList.setVisibility(View.VISIBLE);
            formsList.setVisibility(View.GONE);
            addNewUser.setVisibility(View.VISIBLE);
            addNewForm.setVisibility(View.GONE);
        }
        else{
            guidesList.setVisibility(View.GONE);
            formsList.setVisibility(View.VISIBLE);
            addNewUser.setVisibility(View.GONE);
            addNewForm.setVisibility(View.VISIBLE);

        }
    }
}