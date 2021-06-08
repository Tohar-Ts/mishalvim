package com.example.mishlavim.adminActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.DeleteUser;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminGuidesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    RecyclerView guidesView;
    RecyclerAdapter recyclerAdapter;
    List<String> guidesNames;
    Admin admin;

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
        guidesNames = new ArrayList<>(admin.getGuideList().keySet());

        //init xml views
        guidesView = view.findViewById(R.id.guides_recycler_view);
        recyclerAdapter = new RecyclerAdapter(guidesNames, this, R.menu.guide_options_menu);
        guidesView.setAdapter(recyclerAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text

        switch(item.getItemId() ){
            case R.id.view_guide_volunteers:
                Log.d("onMenuItemClick: ", "view volunteer:" + recyclerAdapter.getClickedText() );
                break;
            case R.id.edit_guide:
                Log.d("onMenuItemClick: ", "edit guide:" + recyclerAdapter.getClickedText() );
                break;
            case R.id.remove_guide:
                Log.d("onMenuItemClick: ", "remove guide:" + recyclerAdapter.getClickedText() );
                break;
        }
            return true;
        }

    }

