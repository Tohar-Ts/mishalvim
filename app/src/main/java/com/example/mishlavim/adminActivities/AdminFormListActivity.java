package com.example.mishlavim.adminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.mishlavim.R;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class AdminFormListActivity extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_admin_form_list, container, false);
        return view;
    }

    public void AdminCreateFormActivity(View v) {
        Intent i = new Intent(getActivity(), AdminCreateFormActivity.class);
        startActivity(i);
    }

    public void AdminGuidesListActivity(View v) {
        Intent i = new Intent(getActivity(), AdminGuidesListActivity.class);
        startActivity(i);
    }

    public void AdminMainActivity(View v){
        Intent i = new Intent(getActivity(), AdminMainActivity.class);
        startActivity(i);

    }
}


