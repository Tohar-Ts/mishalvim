package com.example.mishlavim.guideActivities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminMainActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Admin;

import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class GuideMainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener, DeleteUser.deleteUserListener {

    private TextView guideName;
    private TableLayout voluListLayout;
    BottomNavigationView navBarButtons;
    private String clickedRowName;
    private Guide guide;
    SearchView searchBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);

        guideName = findViewById(R.id.guideName);
        navBarButtons = findViewById(R.id.bottom_navigation);
        voluListLayout = findViewById(R.id.volu_list_layout);
        searchBar = findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //when user press submit button in searchview get string as query parameter
            public boolean onQueryTextSubmit(String query) {
                Context context = getApplicationContext();
                //here im checking to see if the search is working upon submit
                Toast.makeText(context,"Our word : "+query,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            //when user type in searchview get string as newText parameter
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //set the current placement of the cursor on "home"
        navBarButtons.setSelectedItemId(R.id.go_home);

        //init the guide data
        Global globalInstance = Global.getGlobalInstance();
        guide = globalInstance.getGuideInstance();

        setGuideName();
        showVolunteerList();
        navBarButtons.setOnNavigationItemSelectedListener(this);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.go_home){
            finish();
            startActivity(new Intent(getApplicationContext(), GuideMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.add_user) {
               startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
               overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.forms) {
               startActivity(new Intent(getApplicationContext(), GuideReportsActivity.class));
               overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        //init clicked id
        clickedRowName = (String) v.getTag();

        //showing the popup menu
        Context myContext = new ContextThemeWrapper(GuideMainActivity.this,R.style.menuStyle);
        PopupMenu popup = new PopupMenu(myContext, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.volunteer_options_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
        //delete volunteer
            case R.id.remove_volunteer:
                DialogFragment newFragment = new DeleteUser();
                newFragment.show(getSupportFragmentManager(), "deleteUser");
                return true;

            case R.id.view_volunteer:
                Log.d("clicked:", clickedRowName + " view" );
                return true;
            case R.id.edit_volunteer:
                Intent intent = new Intent(getApplicationContext(), GuideVoluSettingActivity.class);
                intent.putExtra("CLICKED_VOLU_KEY", clickedRowName);
                intent.putExtra("CLICKED_VOLU_ID", guide.getMyVolunteers().get(clickedRowName));
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
    }

    private void setGuideName() {
        guideName.setText("שלום, " + guide.getName());
    }

    private void showVolunteerList() {
        HashMap<String, String> voluMap = guide.getMyVolunteers();
        for (String voluName : voluMap.keySet())
            addVoluToList(voluName);
    }

    private void addVoluToList(String voluName) {
        //creating new row
       TableRow voluRow = new TableRow(this);

        //calculate height
        int height = convertFromDpToPixels(60);
        int padding =  convertFromDpToPixels(10);
        int marginBottom =  convertFromDpToPixels(20);

        //styling
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        rowParams.setMargins(0,marginBottom,0,marginBottom);
        voluRow.setLayoutParams(rowParams);
        voluRow.setPadding(padding,padding,padding,padding);
        voluRow.setBackgroundResource(R.drawable.borders);

        //creating new options image
        ImageView voluImg = new ImageView(this);

        //calculate height
        int width = convertFromDpToPixels(40);
        int marginEnd =  convertFromDpToPixels(25);

        //styling
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        imgParams.setMargins(marginEnd,0,marginEnd,0);
        voluImg.setLayoutParams(imgParams);
        voluImg.setBackgroundResource(R.drawable.ic_round_more_horiz);
        voluImg.setClickable(true);
        voluImg.setOnClickListener(this);
        voluImg.setTag(voluName);

        //creating new text view
        TextView voluTxt = new TextView(this);

        //calculate height
        marginEnd =  convertFromDpToPixels(40);

        //styling
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtParams.setMargins( marginEnd,0,marginEnd,0);
        voluTxt.setLayoutParams(txtParams);
        voluTxt.setText(voluName);
        voluTxt.setGravity(Gravity.RIGHT);
        voluTxt.setTextColor(getColor(R.color.black));
        voluTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new row to the tablelayout
        voluRow.addView(voluImg);
        voluRow.addView(voluTxt);
        voluListLayout.addView(voluRow);

        //adding space
        marginEnd =  convertFromDpToPixels(10);
        Space space = new Space(this);
        TableRow.LayoutParams spcParams =  new TableRow.LayoutParams(0, marginEnd);
        space.setLayoutParams(spcParams);
        voluListLayout.addView(space);
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }


    private Void getUserDocSuccess(DocumentSnapshot doc){
        assert doc != null;
        Global globalInstance = Global.getGlobalInstance();
        Volunteer volu = doc.toObject(Volunteer.class);
        globalInstance.setVoluInstance(volu);
        startActivity(new Intent(GuideMainActivity.this, VolunteerMainActivity.class));
        return null;
    }

    private Void getUserDocFailed(Void unused){
        showError(R.string.login_failed);
        return null;
    }



    private void showError(Integer msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletePositiveClick(DialogFragment dialog) {
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(),guide.getMyVolunteers().get(clickedRowName),this::onDocumentDeleteSuccess, this::onDeleteFailed);


    }

    @Override
    public void onDeleteNegativeClick(DialogFragment dialog) {

    }

    public Void onDocumentDeleteSuccess(Void noUse){
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), AuthenticationMethods.getCurrentUserID(),FirebaseStrings.myVolunteerStr(),clickedRowName,this::onKeyDeleteSuccess,this::onDeleteFailed);
        return null;
    }

    public Void onDeleteFailed(Void noUse){
        Toast.makeText(GuideMainActivity.this, "המחיקה נכשלה! באסה!", Toast.LENGTH_SHORT).show();
        return null;
    }
    public Void onKeyDeleteSuccess(Void noUse){
        Toast.makeText(GuideMainActivity.this, "המשתמש נמחק בהצלחה! אהוי!", Toast.LENGTH_SHORT).show();
        reloadScreen();
        return null;
    }

    private void reloadScreen() {
        finish();
        startActivity(getIntent());
    }

}
