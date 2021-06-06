package com.example.mishlavim.adminActivities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.guideActivities.GuideReportsActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,BottomNavigationView.OnNavigationItemSelectedListener, DeleteUser.deleteUserListener {

    private TextView adminName;
    private TableLayout guideListLayout;
    private BottomNavigationView navBarButtons ;
    private Toolbar settingBar;


    private String clickedRowName;
    private String clickedRowId;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminName = findViewById(R.id.WelcomeAdmin);
        guideListLayout = findViewById(R.id.guide_list_layout);
        settingBar = findViewById(R.id.toolbar);
        navBarButtons = findViewById(R.id.admin_bottom_navigation);

        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.guides);

        //init the admin data
        Global globalInstance = Global.getGlobalInstance();
        admin = globalInstance.getAdminInstance();

        setAdminName();
        showVolunteerList();

        //init menu buttons listeners
        setSupportActionBar(settingBar);
        getSupportActionBar().setTitle(null);
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
            startActivity(new Intent(getApplicationContext(), AdminAddNewUserActivity.class));
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
            startActivity(new Intent(getApplicationContext(), AdminCreateFormActivity.class));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
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

    @Override
    public void onClick(View v) {
        //init clicked id
        clickedRowName = (String) v.getTag();
        clickedRowId = admin.getGuideList().get(clickedRowName);
        //showing the popup menu
        Context myContext = new ContextThemeWrapper(AdminMainActivity.this,R.style.menuStyle);
        PopupMenu popup = new PopupMenu(myContext, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.guide_options_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //TODO - guide menu
//        //delete volunteer
//        if (item.getItemId() == R.id.remove_volunteer){
//            DialogFragment newFragment = new DeleteUser();
//            newFragment.show(getSupportFragmentManager(), "deleteUser");
//            return true;
//        }
//        else if (item.getItemId() == R.id.view_volunteer) {
//            Log.d("clicked:", clickedRowName + " view" );
//            return true;
//        }
//
//        else if (item.getItemId() == R.id.edit_volunteer) {
//            Intent intent = new Intent(getApplicationContext(), GuideVoluSettingActivity.class);
//            intent.putExtra("CLICKED_VOLU_KEY", clickedRowName);
//            intent.putExtra("CLICKED_VOLU_ID", guide.getMyVolunteers().get(clickedRowName));
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            return true;
//        }
        return false;
    }

    private void setAdminName() {
        adminName.setText("שלום, " + admin.getName());
    }

    private void showVolunteerList() {
        HashMap<String, String> guideMap = admin.getGuideList();
        for (String guideName : guideMap.keySet())
            addGuideToList(guideName);
    }

    private void addGuideToList(String guideName) {
        //creating new row
        TableRow guideRow = new TableRow(this);

        //calculate height
        int height = convertFromDpToPixels(60);
        int padding =  convertFromDpToPixels(10);
        int marginBottom =  convertFromDpToPixels(20);

        //styling
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        rowParams.setMargins(0,marginBottom,0,marginBottom);
        guideRow.setLayoutParams(rowParams);
        guideRow.setPadding(padding,padding,padding,padding);
        guideRow.setBackgroundResource(R.drawable.borders);

        //creating new options image
        ImageView guideImg = new ImageView(this);

        //calculate height
        int width = convertFromDpToPixels(40);
        int marginEnd =  convertFromDpToPixels(25);

        //styling
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        imgParams.setMargins(marginEnd,0,marginEnd,0);
        guideImg.setLayoutParams(imgParams);
        guideImg.setBackgroundResource(R.drawable.ic_round_more_horiz);
        guideImg.setClickable(true);
        guideImg.setOnClickListener(this);
        guideImg.setTag(guideName);

        //creating new text view
        TextView guideTxt = new TextView(this);

        //calculate height
        marginEnd =  convertFromDpToPixels(40);

        //styling
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtParams.setMargins( marginEnd,0,marginEnd,0);
        guideTxt.setLayoutParams(txtParams);
        guideTxt.setText(guideName);
        guideTxt.setGravity(Gravity.RIGHT);
        guideTxt.setTextColor(getColor(R.color.black));
        guideTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new row to the tablelayout
        guideRow.addView(guideImg);
        guideRow.addView(guideTxt);
        guideListLayout.addView(guideRow);

        //adding space
        marginEnd =  convertFromDpToPixels(10);
        Space space = new Space(this);
        TableRow.LayoutParams spcParams =  new TableRow.LayoutParams(0, marginEnd);
        space.setLayoutParams(spcParams);
        guideListLayout.addView(space);
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(),clickedRowId,this::onDocumentDeleteSuccess, this::onDeleteFailed);
    }

    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) {

    }

    public Void onDocumentDeleteSuccess(Void noUse){
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), AuthenticationMethods.getCurrentUserID(),FirebaseStrings.guideListStr(),clickedRowName,this::onKeyDeleteSuccess,this::onDeleteFailed);
        return null;
    }

    public Void onDeleteFailed(Void noUse){
        Toast.makeText(AdminMainActivity.this, "המחיקה נכשלה! באסה!", Toast.LENGTH_SHORT).show();
        return null;
    }
    public Void onKeyDeleteSuccess(Void noUse){
        Toast.makeText(AdminMainActivity.this, "המשתמש נמחק בהצלחה! אהוי!", Toast.LENGTH_SHORT).show();
        reloadScreen();
        return null;
    }

    private void reloadScreen() {
        finish();
        startActivity(getIntent());
    }

}


