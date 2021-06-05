package com.example.mishlavim.adminActivities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import com.example.mishlavim.R;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminFormListActivity extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_admin_form_list, container, false);
        ListView listView=(ListView)view.findViewById(R.id.listview);

        //create ArrayList of String
        final ArrayList<String> arrayList=new ArrayList<>();

        //Add elements to arraylist
        arrayList.add("Form1");
        arrayList.add("Form2");
        arrayList.add("Form3");
        arrayList.add("Form4");
        arrayList.add("Form5");
        arrayList.add("Form6");
        arrayList.add("Form7");
        //Create Adapter
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayList);

        //assign adapter to listview
        listView.setAdapter(arrayAdapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),"clicked item:"+i+" "+arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();


            }
        });
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


