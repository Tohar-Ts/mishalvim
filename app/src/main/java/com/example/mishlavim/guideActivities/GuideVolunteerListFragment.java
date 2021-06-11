package com.example.mishlavim.guideActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import com.example.mishlavim.R;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;

import android.content.Intent;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.example.mishlavim.UserSettingActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class GuideVolunteerListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    SearchView searchView; //search bar
    RecyclerView guidesView; //recycle list view
    RecyclerAdapter recyclerAdapter; //custom recycle adapter
    List<String> guidesNames; //list of guides names
    Guide guide; //a single guide
    Global global;
    String clickedRowText;
    String clickedRowUid;

    public GuideVolunteerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_volunteer_list, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getting the guide list
        global = Global.getGlobalInstance();
        guide = global.getGuideInstance();
        if(guide == null){
            Toast.makeText(getActivity(), "תקלה בהצגת המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
            return;
        }
        if (guide.getMyVolunteers().isEmpty()) {
            Toast.makeText(getActivity(), "אין מדריכים שמורים במערכת", Toast.LENGTH_SHORT).show();
            return;
        }

        guidesNames = new ArrayList<>(guide.getMyVolunteers().keySet());
        clickedRowText = ""; //default
        clickedRowUid = ""; //default

        //init xml views
        guidesView = view.findViewById(R.id.volunteers_recycler_view);
        recyclerAdapter = new RecyclerAdapter(guidesNames, this, R.menu.volunteer_options_menu);
        guidesView.setAdapter(recyclerAdapter);

        //init search
        searchView = view.findViewById(R.id.search_bar);
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
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        clickedRowText = recyclerAdapter.getClickedText();
        clickedRowUid =  guide.getMyVolunteers().get(clickedRowText);

        switch(item.getItemId() ){
            case R.id.view_volunteer:
                //getting the volunteer data and moving to volunteer main
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedRowUid , this::viewGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.edit_volunteer:
                //moving to setting activity
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedRowUid , this::settingGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.open_form_to_volunteer:
                //moving to the activity, passing the clicked volunteer details, getting the volunteer data into global
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedRowUid , this::OpenGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.remove_volunteer:
                //TODO - remove user
                break;
        }
        return true;
    }

//    //delete dialog functions
//    public void onDeletePositiveClick(DialogFragment dialog) {
//        Guide.deleteVolunteer(global.getUid(), clickedRowUid);
//        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(), clickedRowUid, this::onDocumentDeleteSuccess, this::onDeleteFailed);
//    }

    public Void onDocumentDeleteSuccess(Void noUse){
        Toast.makeText(getActivity(), "המחיקה הסתיימה בהצלחה", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "יש להכנס מחדש לאפליקציה על מנת לראות את השינויים", Toast.LENGTH_SHORT).show();
        return null;
    }

    public Void onDeleteFailed(Void noUse){
        Toast.makeText(getActivity(), "מחיקה נכשלה", Toast.LENGTH_SHORT).show();
        return null;
    }

    //go to watch volunteer as a guide
    private Void viewGetUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(getActivity().getBaseContext(),
                VolunteerMainActivity.class);
        getActivity().startActivity(intent);
        return null;
    }

    //go to open a form to the volunteer
    private Void OpenGetUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(getActivity().getBaseContext(),
                GuideFormsPermissionActivity.class);
        intent.putExtra("CLICKED_VOLU_KEY", clickedRowText);
        intent.putExtra("CLICKED_VOLU_ID", clickedRowUid);
        getActivity().startActivity(intent);
        return null;
    }

    private Void settingGetUserDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        global.setVoluInstance(volu);
        Intent intent = new Intent(getActivity().getBaseContext(),
                UserSettingActivity.class);
        intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.volunteerStr());
        intent.putExtra("CLICKED_USER_ID", clickedRowUid);
        getActivity().startActivity(intent);
        return null;
    }

    private Void getUserDocFailed(Void unused){
        Toast.makeText(getActivity(), "שגיאה בהבאת המידע", Toast.LENGTH_SHORT).show();
        return null;
    }




}