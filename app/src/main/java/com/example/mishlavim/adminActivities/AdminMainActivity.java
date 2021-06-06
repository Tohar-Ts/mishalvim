package com.example.mishlavim.adminActivities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.guideActivities.GuideReportsActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,BottomNavigationView.OnNavigationItemSelectedListener, DeleteUser.deleteUserListener {

    private TextView adminName;
    private TableLayout guideListLayout;
    private BottomNavigationView navBarButtons;
    private Toolbar settingBar;

    private String clickedRowName;
    private String clickedRowId;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminName = findViewById(R.id.WelcomeAdmin);
        guideListLayout = findViewById(R.id.guide_list_layout);
        settingBar = findViewById(R.id.toolbar);
        navBarButtons = findViewById(R.id.admin_bottom_navigation);

        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.guides);

        //init the admin data
        Global globalInstance = Global.getGlobalInstance();
        admin = globalInstance.getAdminInstance();

        setAdminName();
        showVolunteerList();

        //init menu buttons listeners
        setSupportActionBar(settingBar);
        getSupportActionBar().setTitle(null);
        navBarButtons.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.guides){
            finish();
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.add_user) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminAddNewUserActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.forms) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminFormsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.add_forms) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminCreateFormActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.reports) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminReportsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.setting:
                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.exit:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;

public class AdminMainActivity extends AppCompatActivity {
//
//    SwitchCompat simpleSwitch;
//    RecyclerView guidesList;
//    RecyclerView formsList;
//    Button addNewUser;
//    Button addNewForm;
//
//    //our two buttons for the admin to cycle between forms and guides
////    Button firstFragmentBtn, secondFragmentBtn;
//Toolbar toolbar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_admin);
//
//        // initiate a Switch
//        simpleSwitch = (SwitchCompat) findViewById(R.id.AdminSwitch);
//
//        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->{
//            switchOnClick();
//        });
//        //initiate the Recyclers
//        guidesList = findViewById(R.id.guidesList);
//        formsList =  findViewById(R.id.formsList);
//
//        addNewUser = findViewById(R.id.buttonAddNewUser);
//        addNewForm = findViewById(R.id.buttonFillNewForm);
//
//        // TODO: 5/24/2021 make sure we start with forms not guides on the screen.
//
//// check current state of a Switch (true or false).
////        Boolean switchState = simpleSwitch.isChecked();
//
////        guidesBTM = findViewById(R.id.guidesBTM);
////        formsBTM = findViewById(R.id.formsBTM);
//
//        //set listener on the guides button
////        guidesBTM.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                replaceFragment(new AdminGuidesListActivity());
////
////            }
////        });
//        //set listener on the forms button
////        formsBTM.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                replaceFragment(new AdminFormListActivity());
////
////            }
////        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(null);
//        BottomNavigationView navBarButtons=(BottomNavigationView) findViewById(R.id.bottom_navigation);
//        //set the current placement of the cursor on "home"
//        navBarButtons.setSelectedItemId(R.id.go_home);
//
//        //activate a on click listener for the other buttons:
//        navBarButtons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//
//                switch(item.getItemId()){
//                    case R.id.go_home:
//                        return true;
//                    case R.id.add_user:
//                        startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.forms:
//                        startActivity(new Intent(getApplicationContext(), FinishedFormActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_admin,menu);
//        return true;
//    }
//    public void switchOnClick(){
//        if(simpleSwitch.isChecked()){
//            guidesList.setVisibility(View.VISIBLE);
//            formsList.setVisibility(View.GONE);
//            addNewUser.setVisibility(View.VISIBLE);
//            addNewForm.setVisibility(View.GONE);
//        }
//        else{
//            guidesList.setVisibility(View.GONE);
//            formsList.setVisibility(View.VISIBLE);
//            addNewUser.setVisibility(View.GONE);
//            addNewForm.setVisibility(View.VISIBLE);
//
//        }
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//
//            int id = item.getItemId();
//            switch (id){
//                case R.id.setting:
//                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.exit:
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    overridePendingTransition(0, 0);
//                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//            return super.onOptionsItemSelected(item);
//
//        }
}


