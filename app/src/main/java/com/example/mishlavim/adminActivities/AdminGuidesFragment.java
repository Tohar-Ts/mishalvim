package com.example.mishlavim.adminActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.deleteUserActivity;
import com.example.mishlavim.guideActivities.GuideFormsPermissionActivity;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminGuidesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    private SearchView searchView;
    private RecyclerView guidesView;
    private RecyclerAdapter recyclerAdapter;
    private List<String> guidesNames;
    private Admin admin;
    private String clickedGuideId; // the clicked guide id
    private String clickedGuideName;

    public AdminGuidesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_guides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getting the guide list
        Global global = Global.getGlobalInstance();
        admin = global.getAdminInstance();
        //this line checks to make sure that admin isnt null and that there is an active admin
        if(admin == null){
            Toast.makeText(getActivity(), "תקלה בהצגת המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
            return;
        }
        if (admin.getGuideList().isEmpty()) {
            Toast.makeText(getActivity(), "אין מדריכים שמורים במערכת", Toast.LENGTH_SHORT).show();
            return;
        }

        guidesNames = new ArrayList<>(admin.getGuideList().keySet());

        //init xml views
        guidesView = view.findViewById(R.id.guides_recycler_view);
        //init the recycle view adapter which is the list of the guides that are displayed to the admin
        recyclerAdapter = new RecyclerAdapter(guidesNames, this, R.menu.guide_options_menu);
        guidesView.setAdapter(recyclerAdapter);
        searchView = view.findViewById(R.id.search_barA);
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
    //this function detects the clicks on the menu bar, and it goes to the
    //correct activity based on the selection
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        clickedGuideName = recyclerAdapter.getClickedText();
        clickedGuideId = admin.getGuideList().get(clickedGuideName);

        switch(item.getItemId() ){
            case R.id.view_guide_volunteers:
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedGuideId , this::getGuideDocSuccess, this::getGuideDocFailed);
                break;
            case R.id.remove_guide:
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), clickedGuideId , this::deleteGetUserDocSuccess, this::getGuideDocFailed);
                break;
        }
            return true;
        }
    //this is a function that sends a failure message of the guide data fetching fails
    private Void getGuideDocFailed(Void unused) {
        Toast.makeText(getActivity(), "טעינת מידע על המדריך נכשלה", Toast.LENGTH_SHORT).show();
        return null;
    }
    //this is a function that returns the correct document selected
    private Void getGuideDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Guide guide = doc.toObject(Guide.class);
        Global.getGlobalInstance().setGuideInstance(guide);
        Intent intent = new Intent(getActivity().getBaseContext(),
                AdminViewGuideVoluActivity.class);
        intent.putExtra("CLICKED_GUIDE_KEY", clickedGuideName);
        intent.putExtra("CLICKED_GUIDE_ID", clickedGuideId);
        getActivity().startActivity(intent);
        return null;
    }
    //go to volu delete
    private Void deleteGetUserDocSuccess(DocumentSnapshot doc) {
        assert doc != null;
        Guide guide = doc.toObject(Guide.class);
        Global.getGlobalInstance().setGuideInstance(guide);
        //checking if guide has volunteers under him
        assert guide != null;
        if(guide.getMyVolunteers().isEmpty()) {
            Intent intent = new Intent(getActivity().getBaseContext(), deleteUserActivity.class);
            intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.guideStr());
            intent.putExtra("CLICKED_USER_ID", clickedGuideId);
            getActivity().startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
        }
        //you cant delete guide that has volunteers
        else{
            Toast.makeText(getActivity(), "לא ניתן למחוק מדריך שיש לו מתנדבים", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}

