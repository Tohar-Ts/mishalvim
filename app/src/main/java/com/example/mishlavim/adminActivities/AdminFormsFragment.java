package com.example.mishlavim.adminActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.UserSettingActivity;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminFormsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    SearchView searchView;
    RecyclerView templateView;
    RecyclerAdapter recyclerAdapter;
    HashMap<String,String> templates;
    List<String> templatesNames;
    String clickedRowText;
    String clickedRowUid;

    public AdminFormsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init xml views
        templateView = view.findViewById(R.id.templates_recycler_view);
        clickedRowText = ""; //default
        clickedRowUid = ""; //default

        searchView = view.findViewById(R.id.search_barB);
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

        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
    }

    private Void showError(Void unused) {
        Toast.makeText(getActivity(), "שגיאה בטעינת המידע", Toast.LENGTH_SHORT).show();
        return null;
    }

    private Void onGetTemplateSuccess(QuerySnapshot result){
        //init a list of FormTemplates object from the collection

        String formNameField = FirebaseStrings.formNameStr();
        templates = new HashMap<>();
        for (DocumentSnapshot snapshot : result) {
            if (result == null) {
                showError(null);
                return null;
            }
            templates.put((String) snapshot.get(formNameField), snapshot.getId());
        }

        //init a list of Form Templates names
        templatesNames = new ArrayList<>(templates.keySet());
//        templates = new ArrayList<>();
//        for (DocumentSnapshot snapshot:result) {
//            templates.add(snapshot.toObject(FormTemplate.class));
//        }
//
//        //init a list of Form Templates names
//        templatesNames = new ArrayList<>();
//        templates.forEach((f) -> templatesNames.add(f.getFormName()));

        //init the recycle view
        recyclerAdapter = new RecyclerAdapter(templatesNames, this, R.menu.templates_options_menu);
        templateView.setAdapter(recyclerAdapter);

        return null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        clickedRowText = recyclerAdapter.getClickedText();
        clickedRowUid = templates.get(clickedRowText);

        switch(item.getItemId() ){
            case R.id.view_template:
                Log.d("onMenuItemClick: ", "view_template:" + recyclerAdapter.getClickedText() );
                Intent intent = new Intent(getActivity().getBaseContext(),
                        AdminWatchTemplate.class);
                intent.putExtra("CLICKED_FORM_VALUE", clickedRowUid);
                intent.putExtra("CLICKED_FORM_KEY", clickedRowText);
                getActivity().startActivity(intent);
                break;
            case R.id.edit_template:
                Log.d("onMenuItemClick: ", "edit_template:" + recyclerAdapter.getClickedText() );
                break;
            case R.id.remove_template:
                Log.d("onMenuItemClick: ", "remove_template:" + recyclerAdapter.getClickedText() );
                break;
        }
        return true;
    }
}