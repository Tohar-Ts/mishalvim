package com.myapplications.mishlavim.guideActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.UserSettingActivity;
import com.myapplications.mishlavim.login.LoginActivity;
import com.myapplications.mishlavim.model.Firebase.AuthenticationMethods;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Global;
import com.myapplications.mishlavim.model.Guide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class GuideNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_navigation);
        //checking if global was updated correctly
        Guide guide = Global.getGlobalInstance().getGuideInstance();
        if(guide == null){
            Toast.makeText(GuideNavigationActivity.this, "אירעה שגיאה בהבאת המידע", Toast.LENGTH_SHORT).show();
            return;
        }
        //setting bar
        Toolbar settingBar = findViewById(R.id.guide_setting_button);
        setSupportActionBar(settingBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        //navigation bar listener
        BottomNavigationView navigationBtn = findViewById(R.id.guide_bottom_navigation);
        navigationBtn.setOnNavigationItemSelectedListener(this);

        //setting the main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.guide_fragment_container, new GuideVolunteerListFragment())
                .setCustomAnimations(R.anim.fragment_fade_in,R.anim.fragment_fade_out)
                .commit();
        //showing hello user
        TextView helloGuide = findViewById(R.id.helloGuide);
        helloGuide.setText( "שלום "+ guide.getName());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    //this function determines what fragment to go to based on the user clicking the bottom nav bar
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment selectedFragment = null;

        //choose a screen to show
        switch(item.getItemId()){
            case R.id.go_home:
                selectedFragment = new GuideVolunteerListFragment();
                break;
            case R.id.add_user:
                selectedFragment = new GuideAddVolunteerFragment();
                break;
            case R.id.forms:
                selectedFragment = new GuideReportsFragment();
                break;
        }
        //Transaction
        assert selectedFragment != null;

        getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.fragment_fade_in,R.anim.fragment_fade_out
        )
                .replace(R.id.guide_fragment_container, selectedFragment)
                .commit();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    //this function determines what the user selects after clicking the 3 dots on top of screen and
    //does the required action (either exit or settings)
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.setting:
                Intent intent = new Intent(GuideNavigationActivity.this,
                        UserSettingActivity.class);
                intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.guideStr());
                intent.putExtra("CLICKED_USER_ID", Global.getGlobalInstance().getUid());
                intent.putExtra("SHOW_LOGIN", false);
                startActivity(intent);
                break;
            case R.id.exit:
                AuthenticationMethods.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        return super.onOptionsItemSelected(item);
    }

}





