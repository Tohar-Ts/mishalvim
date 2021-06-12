package com.example.mishlavim.adminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishlavim.R;
import com.example.mishlavim.UserSettingActivity;
import com.example.mishlavim.dialogs.DeleteUserDialog;
import com.example.mishlavim.dialogs.passAllVolunteersDialog;
import com.example.mishlavim.guideActivities.GuideFormsPermissionActivity;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminViewGuideVoluActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        passAllVolunteersDialog.passAllVolunteersListener, DeleteUserDialog.deleteUserListener, AdapterView.OnItemSelectedListener
{
    TextView guideNameTextView;
    SearchView searchView;
    Button passAllBtn;
    FloatingActionButton homeBtn;
    RecyclerView volunteersView;
    RecyclerAdapter recyclerAdapter;
    private Spinner guide_spinner;
    HashMap<String, String> volunteers;
    List<String> volunteersNames;
    Guide guide;
    Global global;
    String clickedGuideText, clickedGuideUid; //init guide data from the calling activity
    String clickedVoluText, clickedVoluUid;
    private ArrayList<String> listOfGuidesName,  listOfGuidesID;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_guide_volu);

        global = Global.getGlobalInstance();
        guide = global.getGuideInstance();
        //check to make sure non null element is given
        if (guide == null) {
            Toast.makeText(AdminViewGuideVoluActivity.this, "תקלה בהצגת המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
            return;
        }
        if (guide.getMyVolunteers().isEmpty()) {
            Toast.makeText(AdminViewGuideVoluActivity.this, "אין מתנדבים שמורים במערכת למדריך זה", Toast.LENGTH_SHORT).show();
            return;
        }
        //init data
        clickedGuideText = getIntent().getStringExtra("CLICKED_GUIDE_KEY");
        clickedGuideUid = getIntent().getStringExtra("CLICKED_GUIDE_ID");
        volunteers = guide.getMyVolunteers();
        volunteersNames = new ArrayList<>(volunteers.keySet());
        clickedVoluText = ""; //default
        clickedVoluUid = ""; //default

        //init xml views
        volunteersView = findViewById(R.id.volunteers_recycler_view);
        guideNameTextView = findViewById(R.id.adminGuideName);
        passAllBtn = findViewById(R.id.passAllVolusBtn);
        homeBtn = findViewById(R.id.adminHomeFloating);
        guide_spinner = findViewById(R.id.view_guides_volunteers_spinner);

        //init guide name
        guideNameTextView.setText(clickedGuideText);
        
        //init volu list
        recyclerAdapter = new RecyclerAdapter(volunteersNames, this, R.menu.admin_volunteers_menu);
        volunteersView.setAdapter(recyclerAdapter);
        
        //init buttons listener
        passAllBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        //init spinner
        setSpinner();
        
        //init search
        searchView = findViewById(R.id.admin_volu_search_bar);
        //search function searches based on user input and displays the results
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    //this function does the selected action based on the user selection
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adminHomeFloating:
                startActivity(new Intent(AdminViewGuideVoluActivity.this,
                        AdminNavigationActivity.class));
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                finish();
                break;
            case R.id.passAllVolusBtn:
                DialogFragment dialogFragment = new passAllVolunteersDialog();
                dialogFragment.show(getSupportFragmentManager(), "passAll");
                break;
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    //this function does the selected action from the menu bar
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        clickedVoluText = recyclerAdapter.getClickedText();
        clickedVoluUid = volunteers.get(clickedVoluText);

        switch(item.getItemId() ){
            case R.id.admin_view_volunteer:
                //getting the volunteer data and moving to volunteer main
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(),clickedVoluUid , this::viewGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.admin_edit_volunteer:
                //moving to setting activity
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedVoluUid, this::settingGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.change_guide:
                //moving to the activity, passing the clicked volunteer details, getting the volunteer data into global
                guide_spinner.setVisibility(View.VISIBLE);
                flag = false;
                break;
            case R.id.admin_remove_volunteer:
                DialogFragment dialogFragment = new DeleteUserDialog();
                dialogFragment.show(getSupportFragmentManager(), "deleteUser");
                break;
        }
        return true;
    }


    //go to watch volunteer as admin
    private Void viewGetUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        startActivity(new Intent(AdminViewGuideVoluActivity.this, VolunteerMainActivity.class));
        return null;
    }
    //this function returns the correct user setting based on the user info
    private Void settingGetUserDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(AdminViewGuideVoluActivity.this,
                UserSettingActivity.class);
        intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.volunteerStr());
        intent.putExtra("CLICKED_USER_ID", clickedVoluUid);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
        return null;
    }

    private Void getUserDocFailed(Void unused){
        Toast.makeText(AdminViewGuideVoluActivity.this, "תקלה בהבאת המידע נסה שנית מאוחר יותר", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    //spinne visibility diaglog
    public void passAllVolunteersPositiveClick(DialogFragment dialog) {
        flag = true;
        guide_spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void passAllVolunteersNegativeClick(DialogFragment dialog) { return; }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        // TODO: 6/11/2021 implement this method - delete user.
    }

    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) { return; }


    public void setSpinner(){
        //SPINNER SETUP
        //get the guides list.
        Global globalInstance = Global.getGlobalInstance();
        Admin admin = globalInstance.getAdminInstance();
        if(admin == null){
            Toast.makeText(AdminViewGuideVoluActivity.this, "תקלה בהצגת המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,String> guideList = admin.getGuideList();
        guideList.remove(clickedGuideUid); // remove current guide from list.
        //if guide list is empty show a msg
        if(guideList.isEmpty()){
            Toast.makeText(AdminViewGuideVoluActivity.this, "רשימת המדריכים ריקה", Toast.LENGTH_SHORT).show();
            return;
        }
        listOfGuidesName = new ArrayList<>(guideList.keySet());
        listOfGuidesID = new ArrayList<>(guideList.values());

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, listOfGuidesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guide_spinner.setAdapter(adapter);
        guide_spinner.setOnItemSelectedListener(this);
    }

    //SPINNER METHOD
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 6/11/2021 implement or delete this spinner. NOTE TO DESIGN BETTER THE SPINNER ON THE XML FILE
        //when user choose one item from the spinner you got the values so you can use them.
        String key = listOfGuidesName.get(position);
        String value = listOfGuidesID.get(position);

        if (flag){
            //this case move ALL volunteer
        }
        else{
            //move only 1 volunteer. use clickedVoluUid
        }
    }

    //SPINNER METHOD
    @Override
    public void onNothingSelected(AdapterView<?> parent) { return;}
}
