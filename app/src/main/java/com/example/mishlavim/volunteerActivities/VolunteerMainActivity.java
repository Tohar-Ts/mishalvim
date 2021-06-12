package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
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

import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.guideActivities.GuideNavigationActivity;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

//TODO - in the xml file replace plain text with strings


public class VolunteerMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView helloVolu;
    private GridLayout formsLayout;
    private Volunteer volu;
    private Button openFormBtn;
    private TextView openFormTxt;
    private FloatingActionButton homeBtn;
    private String userUpdatingType;
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);

        //init xml views
        helloVolu = findViewById(R.id.HelloVolu);
        formsLayout = findViewById(R.id.finishedFormsLayout);
        openFormBtn = findViewById(R.id.openFormBtn);
        homeBtn = findViewById(R.id.voluMainHomeFloating);
        //init volu object
        Global globalInstance = Global.getGlobalInstance();
        volu = globalInstance.getVoluInstance();

        if (!volu.getHasOpenForm()){
            openFormBtn.setText("לא קיים\nשאלון פתוח");
            openFormBtn.setClickable(false);
            openFormBtn.setEnabled(false);
        }

        openFormBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);

        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();
        if(userUpdatingType.compareTo(FirebaseStrings.volunteerStr()) != 0){
            homeBtn.setVisibility(View.VISIBLE);
        }
        setHelloMsg();
        setAnsweredForms();
        showGuideHomeBtn();
    }

    @Override
    public void onClick(View v) {
        //clicked the open form button
        if(v.getId() ==  R.id.openFormBtn) {
            Intent intent = new Intent(VolunteerMainActivity.this, VolunteerFillOutFormActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
            finish();
        }
        else if(v.getId() ==  R.id.voluMainHomeFloating){
            //clicking on go back home button - switch activities
            switchToHomeByUser();
        }
        //clicked on an old form button
        else {
            Button clicked = (Button)v;
            String formKey = clicked.getText().toString();
            Intent intent = new Intent(VolunteerMainActivity.this, VolunteerViewOldFormActivity.class);
            intent.putExtra("CLICKED_FORM_KEY", formKey);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
            finish();
        }
    }

    private void showGuideHomeBtn() { // a function that shows an home button to a guide
        Global globalInstance = Global.getGlobalInstance();
        //guide enter to volunteer screen
        //TODO - replace with floating
        if(globalInstance.getType().equals(FirebaseStrings.guideStr())) {
//            navBarButtons.setVisibility(View.VISIBLE);
//            String voluIdFromGuide = globalInstance.getGuideInstance().getMyVolunteers().get(volu.getName());
        }
        else{
//            navBarButtons.setVisibility(View.GONE);
//            String voluIdFromGuide = AuthenticationMethods.getCurrentUserID();
        }


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
        int width = convertFromDpToPixels(300);
        int marginTop =  convertFromDpToPixels(15);
        int marginSides =  convertFromDpToPixels(10);
        int marginBottom =  convertFromDpToPixels(10);

        //styling
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = height;
        params.width = width;
        params.setMargins(marginSides, marginTop,marginSides,marginBottom);
        params.setGravity(Gravity.CENTER);
        formBtn.setLayoutParams(params);
        formBtn.setBackgroundResource(R.drawable.pink_button);
        formBtn.setGravity(Gravity.CENTER);
        formBtn.setOnClickListener(this);
        formBtn.setText(formName);
        formBtn.setTextColor(getResources().getColor(R.color.white));
        formBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
   

        //adding the new button view to the linearlayout
        formsLayout.addView(formBtn);
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }
    private void switchToHomeByUser() {
        //admin is in the setting screen
        if(userUpdatingType.equals(FirebaseStrings.adminStr())){
            startActivity(new Intent(getApplicationContext(), AdminNavigationActivity.class));
        }
        //guide is in the setting screen
        else{
            startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        }
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
    }

}