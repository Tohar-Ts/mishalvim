package com.example.mishlavim.guideActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class GuideMainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView guideName;
    private TableLayout voluListLayout;
    BottomNavigationView navBarButtons;
    private String clickedRowName;
    private Guide guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);

        guideName = findViewById(R.id.guideName);
        navBarButtons = findViewById(R.id.bottom_navigation);
        voluListLayout = findViewById(R.id.volu_list_layout);

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
        switch (item.getItemId()) {
            case R.id.go_home:
                return true;
            case R.id.add_user:
                startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.forms:
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
        //delete volunteer
        if (item.getItemId() == R.id.remove_volunteer){
          //TODO - POP ARE YOU SURE? DIALOG
            return true;
        }
        else if (item.getItemId() == R.id.view_volunteer) {
            Log.d("clicked:", clickedRowName + " view" );
            return true;
        }

        else if (item.getItemId() == R.id.edit_volunteer) {
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

}
