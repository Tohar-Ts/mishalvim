package com.myapplications.mishlavim.adminActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.model.Adapter.RecyclerAdapter;
import com.myapplications.mishlavim.model.Admin;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.Global;
import com.myapplications.mishlavim.model.Guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminChooseVoluGuideActivity extends AppCompatActivity implements
        View.OnClickListener {
    private TextView chooseGuideVoluName;
    private SearchView searchView;
    private TextView homeBtn;
    ProgressBar chooseVoluLoading;
    private RecyclerView guidesView;
    private RecyclerAdapter recyclerAdapter;
    private List<String> guidesNames;
    private Admin admin;
    private Guide guide;
    private Boolean passAll;
    private String clickedVoluId, clickedVoluName, oldGuideId;
    private String clickedGuideId, clickedGuideName;
    private int volCounter;
    HashMap<String ,String> volunteers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_choose_volu_guide);
        //getting the guide list
        Global global = Global.getGlobalInstance();
        admin = global.getAdminInstance();
        guide = global.getGuideInstance();

        guidesNames = new ArrayList<>(admin.getGuideList().keySet());
        guidesNames.remove(guide.getName());

        //init data
        oldGuideId = getIntent().getStringExtra("CLICKED_OLD_GUIDE_ID");
        clickedVoluName = getIntent().getStringExtra("CLICKED_VOLU_NAME");
        clickedVoluId = getIntent().getStringExtra("CLICKED_VOLU_ID");
        passAll = getIntent().getBooleanExtra("PASS_ALL", false);
        volunteers = guide.getMyVolunteers();

        //init xml views
        guidesView = findViewById(R.id.choose_guide_recycler_view);
        chooseGuideVoluName = findViewById(R.id.chooseGuideVoluName);
        homeBtn = findViewById(R.id.adminHomeChooseFloating);
        chooseVoluLoading = findViewById(R.id.chooseVoluLoading);

        chooseVoluLoading.setVisibility(View.GONE);
        homeBtn.setOnClickListener(this);

        //init prompt text
        if(passAll)
            chooseGuideVoluName.setText( "???? ???? ???????????????? ????" +"\n"+"?????????? " + guide.getName());
        else
            chooseGuideVoluName.setText( "???? ???????? " + clickedVoluName);

        //init the recycle view adapter which is the list of the guides that are displayed to the admin
        recyclerAdapter = new RecyclerAdapter(guidesNames, null, R.menu.guide_options_menu, true, this);
        guidesView.setAdapter(recyclerAdapter);

        searchView = findViewById(R.id.search_barG);
        // change close icon color
        ImageView iconClose = searchView.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.button_blue));
        //change search icon color
        ImageView iconSearch = searchView.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.button_blue));
        //this function allows the searchview to detect the text upon input and perform the search
        //with the selected filter in the recycleview adapter:
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
            case R.id.adminHomeChooseFloating:
                startActivity(new Intent(AdminChooseVoluGuideActivity.this,
                        AdminNavigationActivity.class));
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                finish();
                break;
            default:
                chooseVoluLoading.setVisibility(View.VISIBLE);
                clickedGuideName = recyclerAdapter.getClickedText();
                clickedGuideId = admin.getGuideList().get(clickedGuideName);
                Log.d("adminChooseGuide", "onMenuItemClick: volu- "+clickedVoluName+clickedVoluId+"old guide- "+oldGuideId +"new guide- "+clickedGuideId);
                if(passAll)
                    moveAllVVolunteers();
                else {
                    moveVolunteer();
                }
                break;
        }
    }

    private Void showError(Void unused) {
        chooseVoluLoading.setVisibility(View.GONE);
        Toast.makeText(AdminChooseVoluGuideActivity.this, "?????????? ????????", Toast.LENGTH_SHORT).show();
        return null;
    }

    private void moveVolunteer() {
        Log.d(" AdminChooseVoluGuideActivity:", "starting to update volu" + clickedVoluName + "with guide " + clickedGuideName);
        //update volu myGuideId
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), clickedVoluId, FirebaseStrings.myGuideIdStr(), clickedGuideId,
        this::updateInNewGuide, this::showError);
    }

    //update volu in the new guide map
    private Void updateInNewGuide(Void unused) {
        Log.d(" AdminChooseVoluGuideActivity:", "starting to update guide map" + clickedGuideName);
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), clickedGuideId, FirebaseStrings.myVolunteerStr(),
                clickedVoluName,clickedVoluId,
                this::deleteFromOldGuide, this::showError);
        return null;
    }

    private Void deleteFromOldGuide(Void unused) {
        Log.d(" AdminChooseVoluGuideActivity:", "starting to delete from old guide map" + oldGuideId);
        if((passAll)&&((++volCounter)<volunteers.size())) {
            FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), oldGuideId, FirebaseStrings.myVolunteerStr(),
                    clickedVoluName,
                    this::returnToLoop, this::showError);
        }
        else {
            FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), oldGuideId, FirebaseStrings.myVolunteerStr(),
                    clickedVoluName,
                    this::onUpdateFinished, this::showError);
        }
        return null;
    }

    private Void returnToLoop(Void unused) {
        Log.d(" AdminChooseVoluGuideActivity:", "This volu ended successfully. Continue to next volu.");
        return null;
    }

    private void moveAllVVolunteers() {
        volCounter = 0;
        Log.d(" AdminChooseVoluGuideActivity:", "starting to move all volunteers.");
        for(String voluName: volunteers.keySet()){
            clickedVoluName = voluName;
            clickedVoluId = volunteers.get(voluName);
            Log.d(" AdminChooseVoluGuideActivity:", "moving volu " + clickedVoluName + "to guide " + clickedGuideName);
            moveVolunteer();
        }
        Log.d(" AdminChooseVoluGuideActivity:", "finished moving all volunteers.");
    }


    private Void onUpdateFinished(Void unused) {
        Log.d(" AdminChooseVoluGuideActivity:", "updated map successfully");
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }

    private Void updateGlobalFinished(Boolean status) {
        if (status)
            Toast.makeText(AdminChooseVoluGuideActivity.this, "?????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(AdminChooseVoluGuideActivity.this, "???????? ???????????? ??????????, ???? ?????????? ???????????? ???? ?????????????????? ????????", Toast.LENGTH_SHORT).show();
        chooseVoluLoading.setVisibility(View.GONE);
        startActivity(new Intent(AdminChooseVoluGuideActivity.this,
                AdminNavigationActivity.class));
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
        return null;
    }
}