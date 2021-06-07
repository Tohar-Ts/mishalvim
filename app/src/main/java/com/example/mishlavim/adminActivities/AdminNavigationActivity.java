package com.example.mishlavim.adminActivities;
import com.example.mishlavim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class AdminNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);

        //navigation bar listener
        BottomNavigationView navigationBtn = findViewById(R.id.admin_bottom_navigation);
        navigationBtn.setOnNavigationItemSelectedListener(this);

        //setting the main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, new AdminAddNewUserFragments())
                .commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment selectedFragment = null;

        //choose a screen to show
        switch(item.getItemId()){
            case R.id.guides:
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, new AdminAddNewUserFragments())
                .commit();
            break;
            case R.id.add_user:
                selectedFragment = new AdminAddNewUserFragments();
                break;
            case R.id.forms:
            break;
            case R.id.add_forms:

            break;
            case R.id.reports:

            break;
        }
//        //Transaction
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.admin_fragment_container, selectedFragment)
//                .commit();

        return true;
    }
}