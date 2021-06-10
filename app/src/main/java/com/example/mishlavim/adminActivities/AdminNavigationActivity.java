package com.example.mishlavim.adminActivities;
import com.example.mishlavim.R;
import com.example.mishlavim.UserSettingActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdminNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);
        //setting bar
        Toolbar settingBar = findViewById(R.id.admin_setting_button);
        setSupportActionBar(settingBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        //navigation bar listener
        BottomNavigationView navigationBtn = findViewById(R.id.admin_bottom_navigation);
        navigationBtn.setOnNavigationItemSelectedListener(this);

        //showing the main fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, new AdminGuidesFragment())
                .commit();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment selectedFragment = null;

        //choose a screen to show
        switch(item.getItemId()){
            case R.id.guides:
                selectedFragment = new AdminGuidesFragment();
                break;
            case R.id.add_user:
                selectedFragment = new AdminAddNewUserFragment();
                break;
            case R.id.forms:
                selectedFragment = new AdminFormsFragment();
                break;
            case R.id.add_forms:
                selectedFragment = new AdminCreateFormFragment();
            break;
            case R.id.reports:
                selectedFragment = new AdminReportsFragment();
            break;
        }
        //Transaction
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.fragment_fade_in,R.anim.fragment_fade_out
//                R.anim.fragment_slide_right_to_left,
//                R.anim.fragment_exit_right_to_left,
//                        R.anim.fragment_slide_left_to_right,
//                        R.anim.fragment_exit_left_to_right
                )
                .replace(R.id.admin_fragment_container, selectedFragment)
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
                Intent intent = new Intent(AdminNavigationActivity.this,
                        UserSettingActivity.class);
                intent.putExtra("CLICKED_USER_TYPE", FirebaseStrings.adminStr());
                intent.putExtra("CLICKED_USER_ID", Global.getGlobalInstance().getUid());
                intent.putExtra("SHOW_LOGIN", false);
                startActivity(intent);
                Toast.makeText(AdminNavigationActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
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