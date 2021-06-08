package com.example.mishlavim.adminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class AdminFormsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navBarButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forms);


        navBarButtons = findViewById(R.id.admin_forms_bottom_navigation);

        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.forms);
        navBarButtons.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.guides){
            finish();
            startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.add_user) {
            finish();
            //startActivity(new Intent(getApplicationContext(), AdminAddNewUserActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.forms) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminFormsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.add_forms) {
            finish();
//            startActivity(new Intent(getApplicationContext(), AdminCreateFormActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        else if (item.getItemId() == R.id.reports) {
            finish();
            startActivity(new Intent(getApplicationContext(), AdminReportsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }
}