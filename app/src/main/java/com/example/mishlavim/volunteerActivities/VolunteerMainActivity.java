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

import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

//TODO - in the xml file replace plain text with strings


public class VolunteerMainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView helloVolu;
    private GridLayout formsLayout;
    private Volunteer volu;
    BottomNavigationView navBarButtons;
    private FloatingActionButton openFormBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);

        //init xml views
        helloVolu = findViewById(R.id.HelloVolu);
        formsLayout = findViewById(R.id.finishedFormsLayout);
        openFormBtn = findViewById(R.id.openFormBtn);
        navBarButtons = findViewById(R.id.bottom_navigation);
        navBarButtons.getMenu().getItem(0).setCheckable(false);
        //init volu object
        Global globalInstance = Global.getGlobalInstance();
        volu = globalInstance.getVoluInstance();

        if (!volu.getHasOpenForm()){
            openFormBtn.setVisibility(View.GONE);
        }

        openFormBtn.setOnClickListener(this);
        setHelloMsg();
        setAnsweredForms();
        showMenu();
        navBarButtons.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public void onClick(View v) {
        //clicked the open form button
        if(v.getId() ==  R.id.openFormBtn) {
            Intent intent = new Intent(VolunteerMainActivity.this, VolunteerFillOutFormActivity.class);
            startActivity(intent);
        }

        //clicked on an old form button
        else {
            Button clicked = (Button)v;
            String formKey = clicked.getText().toString();
            Intent intent = new Intent(VolunteerMainActivity.this, VolunteerViewOldFormActivity.class);
            intent.putExtra("CLICKED_FORM_KEY", formKey);
            startActivity(intent);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.go_home){
            finish();
//            startActivity(new Intent(getApplicationContext(), GuideMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.add_user) {
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.forms) {
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    private void showMenu() { // a function that shows the menu to the guides
        Global globalInstance = Global.getGlobalInstance();
        //guide enter to volunteer screen
        if(globalInstance.getType().equals(FirebaseStrings.guideStr())) {
            navBarButtons.setVisibility(View.VISIBLE);
            String voluIdFromGuide = globalInstance.getGuideInstance().getMyVolunteers().get(volu.getName());
        }
        else{
            navBarButtons.setVisibility(View.GONE);
            String voluIdFromGuide = AuthenticationMethods.getCurrentUserID();

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

}