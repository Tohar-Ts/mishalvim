package com.myapplication.mishlavim.adminActivities;
import com.myapplication.mishlavim.R;
import com.myapplication.mishlavim.UserSettingActivity;
import com.myapplication.mishlavim.login.LoginActivity;
import com.myapplication.mishlavim.model.Admin;
import com.myapplication.mishlavim.model.Firebase.AuthenticationMethods;
import com.myapplication.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplication.mishlavim.model.Global;
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
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdminNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);
        //checking if global was updated correctly
        Admin admin = Global.getGlobalInstance().getAdminInstance();
        if(admin == null){
            Toast.makeText(AdminNavigationActivity.this, "אירעה שגיאה בהבאת המידע", Toast.LENGTH_SHORT).show();
            return;
        }
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
                .setCustomAnimations(R.anim.fragment_fade_in,R.anim.fragment_fade_out)
                .commit();
        //showing hello user
        TextView helloAdmin = findViewById(R.id.helloAdmin);
        helloAdmin.setText( "שלום "+ admin.getName());

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

        }
        //Transaction
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.fragment_fade_in,R.anim.fragment_fade_out
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
    //this function listens to the buttons clicked on the menu option (either exit or settings) and
    //acts according to the user selection
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