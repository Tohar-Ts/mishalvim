package com.myapplication.mishlavim.adminActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.mishlavim.R;
import com.myapplication.mishlavim.UserSettingActivity;
import com.myapplication.mishlavim.deleteUserActivity;
import com.myapplication.mishlavim.dialogs.passAllVolunteersDialog;
import com.myapplication.mishlavim.model.Adapter.RecyclerAdapter;
import com.myapplication.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplication.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplication.mishlavim.model.Global;
import com.myapplication.mishlavim.model.Guide;
import com.myapplication.mishlavim.model.Volunteer;
import com.myapplication.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminViewGuideVoluActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        passAllVolunteersDialog.passAllVolunteersListener
{
    private TextView guideNameTextView;
    private SearchView searchView;
//    private Button passAllBtn;
    private TextView homeBtn;
    private RecyclerView volunteersView;
    private RecyclerAdapter recyclerAdapter;
    private HashMap<String, String> volunteers;
    private List<String> volunteersNames;
    private Guide guide;
    private Global global;
    private String clickedGuideText, clickedGuideUid; //init guide data from the calling activity
    private String clickedVoluText, clickedVoluUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_guide_volu);

        global = Global.getGlobalInstance();
        guide = global.getGuideInstance();

        //init xml views
        volunteersView = findViewById(R.id.volunteers_recycler_view);
        guideNameTextView = findViewById(R.id.adminGuideName);
//        passAllBtn = findViewById(R.id.passAllVolusBtn);
        homeBtn = findViewById(R.id.adminHomeFloating);

        //init buttons listener
//        passAllBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

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


        //init guide name
        guideNameTextView.setText(clickedGuideText);
        
        //init volu list
        recyclerAdapter = new RecyclerAdapter(volunteersNames, this, R.menu.admin_volunteers_menu, false, null);
        volunteersView.setAdapter(recyclerAdapter);
        


        //init search
        searchView = findViewById(R.id.admin_volu_search_bar);
        // change close icon color
        ImageView iconClose = searchView.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.light_blue2));
        //change search icon color
        ImageView iconSearch = searchView.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.light_blue2));
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
                startActivity(new Intent(getApplicationContext(),
                        AdminNavigationActivity.class));
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                finish();
                break;
//            case R.id.passAllVolusBtn:
//                DialogFragment dialogFragment = new passAllVolunteersDialog();
//                dialogFragment.show(getSupportFragmentManager(), "passAll");
//                break;
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
                Intent intent = new Intent(AdminViewGuideVoluActivity.this, AdminChooseVoluGuideActivity.class);
                intent.putExtra("CLICKED_VOLU_NAME", clickedVoluText);
                intent.putExtra("CLICKED_VOLU_ID", clickedVoluUid);
                intent.putExtra("CLICKED_OLD_GUIDE_ID", clickedGuideUid);
                intent.putExtra("PASS_ALL", false);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                Log.d("adminVieGuide", "onMenuItemClick: volu- "+clickedVoluText+clickedVoluUid+"guide- "+ clickedGuideUid);
                break;
            case R.id.admin_remove_volunteer:
                //moving to delete activity
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(),clickedVoluUid , this::deleteGetUserDocSuccess, this::getUserDocFailed);
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

    //go to volu delete
    private Void deleteGetUserDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(AdminViewGuideVoluActivity.this, deleteUserActivity.class);
        intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.volunteerStr());
        intent.putExtra("CLICKED_USER_ID", clickedVoluUid);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        return null;
    }

    private Void getUserDocFailed(Void unused){
        Toast.makeText(AdminViewGuideVoluActivity.this, "שגיאה בהבאת המידע", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void passAllVolunteersPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(AdminViewGuideVoluActivity.this,
                    AdminChooseVoluGuideActivity.class);
        intent.putExtra("CLICKED_VOLU_NAME", "");
        intent.putExtra("CLICKED_VOLU_ID", "");
        intent.putExtra("CLICKED_OLD_GUIDE_ID", clickedGuideUid);
        intent.putExtra("PASS_ALL", true);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
    }

    @Override
    public void passAllVolunteersNegativeClick(DialogFragment dialog) { return; }


}
