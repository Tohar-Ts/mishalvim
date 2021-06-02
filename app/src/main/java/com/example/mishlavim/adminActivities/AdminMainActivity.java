package com.example.mishlavim.adminActivities;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.volunteerActivities.FillOutFormActivity;
import com.example.mishlavim.volunteerActivities.FinishedFormActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import android.widget.Toast;

public class AdminMainActivity extends AppCompatActivity {
    //our two buttons for the admin to cycle between forms and guides
//    Button firstFragmentBtn, secondFragmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

//        firstFragmentBtn = findViewById(R.id.fragment1btn);
//        secondFragmentBtn = findViewById(R.id.fragment2btn);

        //set listener on the guides button
//        firstFragmentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                replaceFragment(new AdminGuidesListActivity());
//
//            }
//        });
//        //set listener on the forms button
//        secondFragmentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                replaceFragment(new AdminFormListActivity());
//
//            }
//        });
    }
    //this cycles between the fragments once the button is clicked
//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.frameLayout,fragment);
//        fragmentTransaction.commit();
//
//    }
//    //these are the other activities that are used within the fragments
//    public void fillOutFormActivity(View v){
//        Intent i = new Intent(this, FillOutFormActivity.class);
//        startActivity(i);
//    }
//
//    public void AdminAddNewUserActivity(View v){
//        Intent i = new Intent(this, AdminAddNewUserActivity.class);
//        startActivity(i);
//    }
//    public void AdminCreateFormActivity(View v){
//        Intent i = new Intent(this, AdminCreateFormActivity.class);
//        startActivity(i);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.setting:
//                Toast.makeText(this,"setting selected",Toast.LENGTH_SHORT);
//                return true;
//            case R.id.logOut:
//                Toast.makeText(this,"logout selected",Toast.LENGTH_SHORT);
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                overridePendingTransition(0, 0);
//                return true;
//            default:  return super.onOptionsItemSelected(item);
//        }
//
//    }
       int item_id = item.getItemId();
       if(item_id==R.id.setting){
           Toast.makeText(this,"setting selected",Toast.LENGTH_SHORT).show();
       }
       else if((item_id==R.id.logOut)){
           Toast.makeText(this,"logout selected",Toast.LENGTH_SHORT).show();
       }
       return true;
    }
}


