package com.example.mishlavim.volunteerActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.mishlavim.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideNavigationActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

//TODO - in the xml file replace plain text with strings


public class VolunteerMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView helloVolu, homeButton;
    private GridLayout formsLayout;
    private Volunteer volu;
    private Button openFormBtn, logOut;
    private String userUpdatingType;
    private Global global;
    private Guide myGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);

        //init xml views
        helloVolu = findViewById(R.id.HelloVolu);
        formsLayout = findViewById(R.id.finishedFormsLayout);
        openFormBtn = findViewById(R.id.openFormBtn);
        homeButton = findViewById(R.id.guideVoluHomeFloating);
        logOut = findViewById(R.id.voluLogOut);
        TextView myGuidePrompt = findViewById(R.id.myGuidePrompt);

        //init volu object
        Global globalInstance = Global.getGlobalInstance();
        volu = globalInstance.getVoluInstance();
        myGuide = globalInstance.getGuideInstance();

        //init my guide text
        myGuidePrompt.setText("המדריך שלך: " + myGuide.getName());

        if (!volu.getHasOpenForm()){
            openFormBtn.setText("לא קיים\nשאלון פתוח");
            openFormBtn.setClickable(false);
            openFormBtn.setEnabled(false);
        }


        openFormBtn.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        logOut.setOnClickListener(this);
        
        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();
        if(userUpdatingType.compareTo(FirebaseStrings.volunteerStr()) != 0)
            homeButton.setVisibility(View.VISIBLE);
        else
            homeButton.setVisibility(View.GONE);

        initHomeButton();
        setHelloMsg();
        setAnsweredForms();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openFormBtn:
                Intent intent = new Intent(VolunteerMainActivity.this, VolunteerFillOutFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                break;
            case R.id.guideVoluHomeFloating:
                switchToHomeByUser();
                break;
            case R.id.voluLogOut:
                AuthenticationMethods.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                break;
            default:
                //clicked on an old form button
                Button clicked = (Button)v;
                String formKey = clicked.getText().toString();
                Intent intent2 = new Intent(VolunteerMainActivity.this, VolunteerViewOldFormActivity.class);
                intent2.putExtra("CLICKED_FORM_KEY", formKey);
                startActivity(intent2);
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                break;
        }
    }


    private void initHomeButton() {
        if(userUpdatingType.equals(FirebaseStrings.adminStr())) {
            homeButton.setBackgroundResource(R.drawable.nav_blue_corners);
        }
        else {
            homeButton.setBackgroundResource(R.drawable.nav_orange_corners);
        }
    }

    private void switchToHomeByUser() {
        //admin is in the setting screen
        if(userUpdatingType.equals(FirebaseStrings.adminStr())) {
            startActivity(new Intent(getApplicationContext(), AdminNavigationActivity.class));
        }
        //guide is in the setting screen
        else {
            startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        }
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
    }


    private void setHelloMsg() {
        helloVolu.setText("שלום, " + volu.getName());
    }

    private void setAnsweredForms() {
        HashMap<String, String> finishedForms = volu.getFinishedForms();
        for (String formName : finishedForms.keySet())
            addForm(formName);
    }

    private void addForm(String formName) {
        //creating new button
        Button formBtn = new Button(this);

        //calculate height
        int height = convertFromDpToPixels(60);
        int marginTop =  convertFromDpToPixels(15);
        int marginSides =  convertFromDpToPixels(0);
        int marginBottom =  convertFromDpToPixels(10);

        //styling
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = height;
        params.width = GridLayout.LayoutParams.MATCH_PARENT;
        params.setMargins(marginSides, marginTop,marginSides,marginBottom);
        params.setGravity(Gravity.CENTER);
        formBtn.setLayoutParams(params);
        formBtn.setBackgroundResource(R.drawable.pink_button);
        formBtn.setGravity(Gravity.CENTER);
        formBtn.setOnClickListener(this);
        formBtn.setText(formName);
        formBtn.setTextColor(getResources().getColor(R.color.white));
        formBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        Typeface typeface = ResourcesCompat.getFont(VolunteerMainActivity.this, R.font.alef);
        formBtn.setTypeface(typeface);

        //adding the new button view to the linearlayout
        formsLayout.addView(formBtn);
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

}