package com.example.mishlavim.guideActivities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.test.CustomList;
import com.example.mishlavim.test.CustomeArrayAdupter;

import java.util.ArrayList;

public class GuideMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);

        Global globalInstance = Global.getGlobalInstance();
        Guide guide = globalInstance.getGuideInstance();
        guide.getMyVolunteers();
        guide.getName();
        guide.getEmail();

        ListView main_listview = findViewById(R.id.guide_listviw);

        ArrayList<CustomList> testList = new ArrayList<>();
        testList.add(new CustomList(R.drawable.bat_ami_logo, "logo", "test rate"));


        CustomeArrayAdupter arrayAdupter = new CustomeArrayAdupter(this, 0, testList);

        main_listview.setAdapter(arrayAdupter);

    }
}

