package com.example.mishlavim.adminActivities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AdminReportsFragment extends Fragment {

    private Admin admin;
    private HashMap<String, String> guidesMap;
    private TableLayout tbl;
    private TextView allAnsweredFormsTxt;

    public AdminReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init xml
        allAnsweredFormsTxt = view.findViewById(R.id.allAnsweredForms);
        tbl = view.findViewById(R.id.guidesTbl);

        //init local variables
        Global globalInstance = Global.getGlobalInstance();
        admin = globalInstance.getAdminInstance();
        guidesMap = admin.getGuideList();
        showGuides();
        FirestoreMethods.getCollection(FirebaseStrings.answeredFormsStr(), this::showNumOfAnsweredForms,this::showErrors);
    }

    private Void showNumOfAnsweredForms(QuerySnapshot doc) {
        int answeredFormsNum = doc.size();
        allAnsweredFormsTxt.setText("מספר השאלונים שמולאו עד כה: " + answeredFormsNum);
        // TODO: 11/06/2021 check answered Forms delete
        return null;
    }


    private void showGuides() {
        for(String GuideName: guidesMap.keySet()){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_reports, container, false);
    }
    private Void showErrors(Void unused) {
        return  null;
    }

}