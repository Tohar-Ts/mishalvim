package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentSnapshot;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * this activity show statistics and data about the guide's volunteers and their forms
 **/
public class GuideReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Guide guide;
    private HashMap<String, String> myVolunteersMap;
    private TableLayout volunteersTbl;
    private HashMap<String, Integer> numOfForms = new HashMap<>();
    private TextView avgText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_reports);

        //init the guide an volunteers data
        Global globalInstance = Global.getGlobalInstance();
        guide = globalInstance.getGuideInstance();
        myVolunteersMap = guide.getMyVolunteers();
        volunteersTbl = findViewById(R.id.volunteers_tbl);
        avgText = findViewById(R.id.avg_text);
        startTbl();
    }

    @Override
    public void onClick(View v) {
    }
    private void startTbl(){
        int count = 0;
        for(String voluName : myVolunteersMap.keySet()) {
            count++;
            String voluId = myVolunteersMap.get(voluName);
            if(count!= myVolunteersMap.size()){
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::pushFormsNum, this::showError);
            }
            else{
                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::pushAndGotoCreateTbl, this::showError);
            }
        }
    }

    private Void pushAndGotoCreateTbl(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        assert volu != null;
        Log.d("pushAndGotoCreateTbl",volu.getName()+ " "+ volu.getFinishedForms());
        if(!(volu.getFinishedForms().isEmpty()))
            numOfForms.put(volu.getName(), volu.getFinishedForms().size());
        else
            numOfForms.put(volu.getName(), 0);
        createVolunteersTbl();
        calculateAvg();
        return null;
    }

    private Void pushFormsNum(DocumentSnapshot doc) {
        assert doc != null;
        Volunteer volu = doc.toObject(Volunteer.class);
        assert volu != null;
        Log.d("pushFormsNum",volu.getName()+ " "+ volu.getFinishedForms());
        if(!(volu.getFinishedForms().isEmpty()))
            numOfForms.put(volu.getName(), volu.getFinishedForms().size());
        else
            numOfForms.put(volu.getName(), 0);
        return null;
    }

    private void createVolunteersTbl() {
        for(String voluName : myVolunteersMap.keySet()){
            TableRow newRow = new TableRow(this);;
            TextView name = new TextView(this);
            TextView formsNum = new TextView(this);

            // create name text
//            TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            name.setLayoutParams(txtParams);
            name.setText(voluName);
            name.setGravity(Gravity.START);
            name.setTextColor(getColor(R.color.black));
            name.setSingleLine(false);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            // create num of forms text
//            TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            name.setLayoutParams(txtParams);
            formsNum.setText(numOfForms.get(voluName)+"");
            formsNum.setGravity(Gravity.CENTER);
            formsNum.setTextColor(getColor(R.color.black));
            formsNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            //add to XML
            newRow.addView(formsNum);
            newRow.addView(name);
            volunteersTbl.addView(newRow);

        }
    }

    private void calculateAvg(){
        int allFormsSum = 0;
        int voluNum = myVolunteersMap.size();
        for(String volName : numOfForms.keySet()){
            allFormsSum += numOfForms.get(volName);
            Log.d("calculateAvg", " "+ numOfForms.get(volName));
        }
        Log.d("calculateAvg", " "+ allFormsSum+" "+ voluNum);
        double avgFormsPerVolu = (double)allFormsSum / (double)voluNum;
        avgText.setText("ממוצע טפסים לחניך: " + avgFormsPerVolu);

    }
    private int convertFromDpToPixels(int toConvert){
        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
        return pixels;
    }
    private Void showError(Void unused) {
        Log.e("err","failed to load from dataBase");
        Toast.makeText(getApplicationContext(), "Something went wrong (dataBase)", Toast.LENGTH_SHORT).show();
        return null;
    }
}