package com.example.mishlavim.guideActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminAddNewUserFragment;
import com.example.mishlavim.adminActivities.AdminNavigationActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import com.example.mishlavim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GuideNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_navigation);
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
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
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

//                R.anim.fragment_slide_right_to_left,
//                R.anim.fragment_exit_right_to_left,
//                R.anim.fragment_slide_left_to_right,
//                R.anim.fragment_exit_left_to_right
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.setting:
                //TODO navigate to setting
                Toast.makeText(GuideNavigationActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                AuthenticationMethods.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(0, 0);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}





