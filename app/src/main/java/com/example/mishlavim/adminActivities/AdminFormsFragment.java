package com.example.mishlavim.adminActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminFormsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    RecyclerView templateView;
    RecyclerAdapter recyclerAdapter;
    List<FormTemplate> templates;
    List<String> templatesNames;

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

        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
    }

    private Void showError(Void unused) {
        return null;
    }

    private Void onGetTemplateSuccess(QuerySnapshot result){
        //init a list of FormTemplates object from the collection
        templates = new ArrayList<>();
        for (DocumentSnapshot snapshot:result) {
            templates.add(snapshot.toObject(FormTemplate.class));
        }

        //init a list of Form Templates names
        templatesNames = new ArrayList<>();
        templates.forEach((f) -> templatesNames.add(f.getFormName()));

        //init the recycle view
        recyclerAdapter = new RecyclerAdapter(templatesNames, this, R.menu.templates_options_menu);
        templateView.setAdapter(recyclerAdapter);
        return null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text

        switch(item.getItemId() ){
            case R.id.view_template:
                Log.d("onMenuItemClick: ", "view_template:" + recyclerAdapter.getClickedText() );
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