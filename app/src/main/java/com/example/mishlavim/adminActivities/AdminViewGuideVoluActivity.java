package com.example.mishlavim.adminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishlavim.R;
import com.example.mishlavim.UserSettingActivity;
import com.example.mishlavim.guideActivities.GuideFormsPermissionActivity;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
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
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminViewGuideVoluActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    TextView guideNameTextView;
    SearchView searchView;
    Button passAllBtn;
    FloatingActionButton homeBtn;
    RecyclerView volunteersView;
    RecyclerAdapter recyclerAdapter;

    HashMap<String, String> volunteers;
    List<String> volunteersNames;
    Guide guide;
    Global global;
    String clickedGuideText, clickedGuideUid; //init guide data from the calling activity
    String clickedVoluText, clickedVoluUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_guide_volu);

        global = Global.getGlobalInstance();
        guide = global.getGuideInstance();
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

        //init guide name
        guideNameTextView.setText(clickedGuideText);
        //init volu list
        recyclerAdapter = new RecyclerAdapter(volunteersNames, this, R.menu.admin_volunteers_menu);
        volunteersView.setAdapter(recyclerAdapter);
        //init buttons listener
        passAllBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        //init search
        searchView = findViewById(R.id.admin_volu_search_bar);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adminHomeFloating:
                startActivity(new Intent(AdminViewGuideVoluActivity.this,
                        AdminNavigationActivity.class));
                break;
            case R.id.passAllVolusBtn:
                //TODO - passing all volunteers from one guide to another
                break;
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
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
                //TODO - show guide spinner or something
                break;
            case R.id.admin_remove_volunteer:
                //TODO - show yes/no dialog.
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

    private Void settingGetUserDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(AdminViewGuideVoluActivity.this,
                UserSettingActivity.class);
        intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.volunteerStr());
        intent.putExtra("CLICKED_USER_ID", clickedVoluUid);
        startActivity(intent);
        return null;
    }

    private Void getUserDocFailed(Void unused){
        Toast.makeText(AdminViewGuideVoluActivity.this, "תקלה בהבאת המידע נסה שנית מאוחר יותר", Toast.LENGTH_SHORT).show();
        return null;
    }
}
