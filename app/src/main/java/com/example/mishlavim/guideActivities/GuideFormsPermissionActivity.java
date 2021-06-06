package com.example.mishlavim.guideActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private String voluName;
    private String voluId;
    private TableLayout formsList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);
        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId =  getIntent().getStringExtra("CLICKED_VOLU_ID");
        formsList = findViewById(R.id.forms_list);
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(),this::createFormsList, this::showError);

    }

    private Void showError(Void unused) {
        return null;
    }

    private Void createFormsList(QuerySnapshot docArr) {
        for (QueryDocumentSnapshot document : docArr) {
            Log.d("createFormsList", document.getId() + " => " + document.getData());
            FormTemplate form = document.toObject(FormTemplate.class);
            Log.d("createFormsList", form.getFormName()+"");
            addToTbl(form.getFormName());
        }

        return null;
    }

    private void addToTbl(String formName) {
        TableRow newRow = new TableRow(this);
        //calculate height
        int height = convertFromDpToPixels(60);
        int padding =  convertFromDpToPixels(10);
        int marginBottom =  convertFromDpToPixels(20);

        //styling
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        rowParams.setMargins(0,marginBottom,0,marginBottom);
        newRow.setLayoutParams(rowParams);
        newRow.setPadding(padding,padding,padding,padding);
        newRow.setBackgroundResource(R.drawable.borders);

        //creating new options image
        ImageView optionImg = new ImageView(this);

        //calculate height
        int width = convertFromDpToPixels(40);
        int marginEnd =  convertFromDpToPixels(25);

        //styling
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        imgParams.setMargins(marginEnd,0,marginEnd,0);
        optionImg.setLayoutParams(imgParams);
        optionImg.setBackgroundResource(R.drawable.ic_round_more_horiz);
        optionImg.setClickable(true);
        optionImg.setOnClickListener(this);
        optionImg.setTag(formName);

        //creating new text view
        TextView formNameView = new TextView(this);

        //calculate height
        marginEnd =  convertFromDpToPixels(40);

        //styling
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtParams.setMargins( marginEnd,0,marginEnd,0);
        formNameView.setLayoutParams(txtParams);
        formNameView.setText(formName);
        formNameView.setGravity(Gravity.RIGHT);
        formNameView.setTextColor(getColor(R.color.black));
        formNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new row to the tablelayout
        newRow.addView(optionImg);
        newRow.addView(formNameView);
        formsList.addView(newRow);

        //adding space
        marginEnd =  convertFromDpToPixels(10);
        Space space = new Space(this);
        TableRow.LayoutParams spcParams =  new TableRow.LayoutParams(0, marginEnd);
        space.setLayoutParams(spcParams);
        formsList.addView(space);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guide_forms_option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    @Override
    public void onClick(View v) {

    }
    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }
}