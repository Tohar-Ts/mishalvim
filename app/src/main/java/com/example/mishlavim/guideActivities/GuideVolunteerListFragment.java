package com.example.mishlavim.guideActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUserDialog;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class GuideVolunteerListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    RecyclerView guidesView;
    RecyclerAdapter recyclerAdapter;
    List<String> guidesNames;
    Guide guide;
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
                //TODO - show what is the volunteer name?.
                break;
            case R.id.open_form_to_volunteer:
                //moving to the activity, passing the clicked volunteer details, getting the volunteer data into global
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedRowUid , this::OpenGetUserDocSuccess, this::getUserDocFailed);
                break;
            case R.id.remove_volunteer:
               //TODO - show yes/no dialog.
                break;
        }
        return true;
    }

    //search functions
    //TODO - fixing search to recycle view

    //delete dialog functions
    public void onDeletePositiveClick(DialogFragment dialog) {
        Guide.deleteVolunteer(global.getUid(), clickedRowUid);
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(), clickedRowUid, this::onDocumentDeleteSuccess, this::onDeleteFailed);
    }

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
    private Void getUserDocFailed(Void unused){
        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        return null;
    }




}