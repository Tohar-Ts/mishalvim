package com.example.mishlavim.adminActivities;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.volunteerActivities.FillOutFormActivity;
import com.example.mishlavim.volunteerActivities.FinishedFormActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class AdminMainActivity extends AppCompatActivity {
    //our two buttons for the admin to cycle between forms and guides
//    Button firstFragmentBtn, secondFragmentBtn;
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        BottomNavigationView navBarButtons=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.go_home);

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


            int id = item.getItemId();
            switch (id){
                case R.id.setting:
                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.exit:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;

            }
            return super.onOptionsItemSelected(item);

        }
}


