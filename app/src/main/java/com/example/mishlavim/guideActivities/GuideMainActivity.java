package com.example.mishlavim.guideActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminAddNewUserActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.volunteerActivities.FillOutFormActivity;
import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.volunteerActivities.FinishedFormActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class GuideMainActivity extends AppCompatActivity{
    private TextView guideName;
    private ImageView options;
//    private Button voluListButton,formsListButton, settingButton, reportsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);
        guideName = findViewById(R.id.guideName);

        //init navBarButtons, navigation bar selector variable
        BottomNavigationView navBarButtons=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.go_home);

        //init the options button for all of the volunteers in the list:
        options = findViewById(R.id.addForm);
        //now register the floating menu to the button:
        registerForContextMenu(options);


        //activate a on click listener for the other buttons:
        navBarButtons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.go_home:
                        return true;
                    case R.id.add_user:
                        startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.forms:
                        startActivity(new Intent(getApplicationContext(), FinishedFormActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        setGuideName();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.floating_menu,menu);
    }

//    public void onButtonClickEvent(View sender)
//    {
//        registerForContextMenu(sender);
//        openContextMenu(sender);
//        unregisterForContextMenu(sender);
//    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       switch(item.getItemId()){
           case R.id.remove_volunteer:
               Toast.makeText(this, "remove_volunteer", Toast.LENGTH_SHORT).show();
               return true;
           case R.id.view_volunteer:
               Toast.makeText(this, "view_volunteer", Toast.LENGTH_SHORT).show();
               return true;
           case R.id.edit_volunteer:
               Toast.makeText(this, "edit_volunteer", Toast.LENGTH_SHORT).show();
               return true;
           default:
               return super.onContextItemSelected(item);
       }
    }

    private void setGuideName() {
        Global globalInstance = Global.getGlobalInstance();
        Guide guide = globalInstance.getGuideInstance();
        guideName.setText(guide.getName()); }

}