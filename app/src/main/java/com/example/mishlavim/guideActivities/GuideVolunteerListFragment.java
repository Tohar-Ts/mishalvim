package com.example.mishlavim.guideActivities;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUserDialog;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Adapter.MyListAdapter;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class GuideVolunteerListFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, DeleteUserDialog.deleteUserListener, SearchView.OnQueryTextListener,BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView guideName;
    private TableLayout voluListLayout;
    BottomNavigationView navBarButtons;
    private String clickedRowName;
    private Guide guide;
    private Toolbar settingBar;
    SearchView searchBar;
    private ListView listViewActivity;
    private ArrayList<String> voluNames;
    private ArrayAdapter adapter;
    private View view;

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
        this.view = view;
        guideName = view.findViewById(R.id.guideName);
        //navBarButtons = view.findViewById(R.id.bottom_navigation);
        //voluListLayout = findViewById(R.id.volu_list_layout);
        settingBar = view.findViewById(R.id.toolbar);
        //add the searchBar and set the method for searching
        searchBar = view.findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(this);
        //set the current placement of the cursor on "home"
        //navBarButtons.setSelectedItemId(R.id.go_home);
        //init the guide data
        Global globalInstance = Global.getGlobalInstance();
        guide = globalInstance.getGuideInstance();
        //init voluNames
        voluNames= new ArrayList<String>();
        setGuideName();
        showVolunteerList();
//        setSupportActionBar(settingBar);
//        getSupportActionBar().setTitle(null);
        //navBarButtons.setOnNavigationItemSelectedListener(this);
        listViewActivity = view.findViewById(R.id.listview);
        adapter =new MyListAdapter(getContext(),R.layout.list_item, voluNames);
        listViewActivity.setAdapter(adapter);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.setting_menu, menu);
////        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.setting:
//                Toast.makeText(GuideMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.exit:
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                overridePendingTransition(0, 0);
//                Toast.makeText(GuideMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                break;
//
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.go_home){
//            finish();
//            startActivity(new Intent(getApplicationContext(), GuideMainActivity.class));
//            return true;
//        }
//        else if (item.getItemId() == R.id.add_user) {
//            startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        } else if (item.getItemId() == R.id.forms) {
//            startActivity(new Intent(getApplicationContext(), GuideReportsActivity.class));
//            overridePendingTransition(0, 0);
//            return true;
//        }
        return false;
    }

    @Override
    public void onClick(View v) {
        //init clicked id
        clickedRowName = (String) v.getTag();
//        //showing the popup menu
//        Context myContext = new ContextThemeWrapper(GuideMainActivity.this,R.style.menuStyle);
//        PopupMenu popup = new PopupMenu(myContext, v);
//        popup.setOnMenuItemClickListener(this);
//        popup.inflate(R.menu.volunteer_options_menu);
//        popup.show();
    }


    //additional pop-up for the button in the list_view
//    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.volunteer_options_menu, popup.getMenu());
//        popup.show();
//    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        //delete volunteer
//        if (item.getItemId() == R.id.remove_volunteer) {
//            DialogFragment newFragment = new DeleteUserDialog();
//            newFragment.show(getSupportFragmentManager(), "deleteUser");
//            return true;
//        }
//        else if (item.getItemId() == R.id.view_volunteer) {
//            FirestoreMethods.getDocument(FirebaseStrings.usersStr(),  guide.getMyVolunteers().get(clickedRowName), this::getUserDocSuccess, this::getUserDocFailed);
//            Log.d("clicked:", clickedRowName + " view" );
//            return true;
//        }
//
//        else if (item.getItemId() == R.id.open_form_to_volunteer) {
//            Intent intent = new Intent(getApplicationContext(), GuideFormsPermissionActivity.class);
//            intent.putExtra("CLICKED_VOLU_KEY", clickedRowName);
//            intent.putExtra("CLICKED_VOLU_ID", guide.getMyVolunteers().get(clickedRowName));
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            return true;
//        }
//        else if (item.getItemId() == R.id.view_volunteer) {
//            FirestoreMethods.getDocument(FirebaseStrings.usersStr(), guide.getMyVolunteers().get(clickedRowName), this::getUserDocSuccess, this::getUserDocFailed);
//            Log.d("clicked:", clickedRowName + " view" );
//            return true;
//        }
        return false;
    }

    private void setGuideName() {
        guideName.setText("שלום, " + guide.getName());
    }
    private Void getUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        Global globalInstance = Global.getGlobalInstance();
        Volunteer volu = doc.toObject(Volunteer.class);
        globalInstance.setVoluInstance(volu);
        startActivity(new Intent(getActivity(), VolunteerMainActivity.class));
        return null;
    }

    private Void getUserDocFailed(Void unused){
        showError(R.string.login_failed);
        return null;
    }
    private void showVolunteerList() {
        HashMap<String, String> voluMap = guide.getMyVolunteers();
        for (String voluName : voluMap.keySet())
            addVoluToList(voluName);
    }
    //add the current volunteer to the Array of volunteers
    private void addVoluToList(String voluName) {
        voluNames.add(voluName);
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(),guide.getMyVolunteers().get(clickedRowName),this::onDocumentDeleteSuccess, this::onDeleteFailed);
    }

    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) {
    }

    public Void onDocumentDeleteSuccess(Void noUse){
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), AuthenticationMethods.getCurrentUserID(),FirebaseStrings.myVolunteerStr(),clickedRowName,this::onKeyDeleteSuccess,this::onDeleteFailed);
        return null;
    }

    public Void onDeleteFailed(Void noUse){
        Toast.makeText(getActivity(), "המחיקה נכשלה! באסה!", Toast.LENGTH_SHORT).show();
        return null;
    }
    public Void onKeyDeleteSuccess(Void noUse){
        Toast.makeText(getActivity(), "המשתמש נמחק בהצלחה! אהוי!", Toast.LENGTH_SHORT).show();
        reloadScreen();
        return null;
    }

    private void reloadScreen() {
//        finish();
//        startActivity(getIntent());
    }
    private void showError(Integer msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Context context = getContext();
        //here im checking to see if the search is working upon submit
        Toast.makeText(context,"Our word : "+query,Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    //when user type in searchview get string as newText parameter
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }





}