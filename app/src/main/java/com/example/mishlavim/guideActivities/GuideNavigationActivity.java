package com.example.mishlavim.guideActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminAddNewUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import com.example.mishlavim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;
public class GuideNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_navigation);
        //navigation bar listener
        BottomNavigationView navigationBtn = findViewById(R.id.guide_bottom_navigation);
        navigationBtn.setOnNavigationItemSelectedListener(this);

        //setting the main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.guide_fragment_container, new AdminAddNewUserFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment selectedFragment = null;

        //choose a screen to show
        switch(item.getItemId()){
            case R.id.go_home:
                //selectedFragment = new GuideHomeFragments();
                break;
            case R.id.add_user:
                //selectedFragment = new GuideAddNewUserFragments();
                break;
            case R.id.forms:
                //selectedFragment = new GuideFormsFragments();
                break;
        }
        //Transaction
        getSupportFragmentManager().beginTransaction().setCustomAnimations(

                R.anim.fragment_slide_right_to_left,
                R.anim.fragment_exit_right_to_left,
                R.anim.fragment_slide_left_to_right,
                R.anim.fragment_exit_left_to_right
        )
                .replace(R.id.guide_fragment_container, selectedFragment)
                .commit();

        return true;
    }
}





