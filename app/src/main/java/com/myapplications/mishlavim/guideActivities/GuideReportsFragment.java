package com.myapplications.mishlavim.guideActivities;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.Global;
import com.myapplications.mishlavim.model.Guide;
import com.myapplications.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentSnapshot;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class GuideReportsFragment extends Fragment implements View.OnClickListener{

    private Guide guide;
    private HashMap<String, String> myVolunteersMap;
    private GridLayout volunteersTbl;
    private HashMap<String, Integer> numOfForms = new HashMap<>();
    private TextView avgText;
    private View view;
    ProgressBar progressBar;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public GuideReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        //init xml views
        avgText = view.findViewById(R.id.avg_text);

        //init the guide and volunteers data
        Global globalInstance = Global.getGlobalInstance();
        guide = globalInstance.getGuideInstance();
        myVolunteersMap = guide.getMyVolunteers();
        volunteersTbl = view.findViewById(R.id.volunteers_tbl);
        progressBar = view.findViewById(R.id.guideReportsLoading);
       progressBar.setVisibility(View.VISIBLE);
        startTbl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_reports, container, false);
    }

    @Override
    public void onClick(View v) {
    }

    private void startTbl(){
        if(myVolunteersMap.isEmpty()){
            Toast.makeText(getActivity(), "אין מתנדבים", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            avgText.setText("אין שאלונים שמולאו ");
            return;
        }

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
        //TODO fix this function makes the report crash
        if(!(volu.getFinishedForms().isEmpty()))
            numOfForms.put(volu.getName(), volu.getFinishedForms().size());
        else
            numOfForms.put(volu.getName(), 0);
        Log.d("pushAndGotoCreateTbl:", "pushAndGotoCreateTbl");
        createTblVoluForms();
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

    private void createTblVoluForms() {
        for(String voluName : myVolunteersMap.keySet()){
            //new row
//            TableRow newRow = new TableRow(getActivity());;
//            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//            newRow.setBackgroundResource(R.drawable.table);
//            newRow.setLayoutParams(rowParams);
//
//            //new relative layout
//            RelativeLayout newLinerLayout = new RelativeLayout(getActivity());
//            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            int paddingTop = convertFromDpToPixels(10);
//            int paddingRight = convertFromDpToPixels(15);
//            int paddingLeft = convertFromDpToPixels(25);
//            int paddingBottom = convertFromDpToPixels(10);
//            newLinerLayout.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
//            newRow.setLayoutParams(relativeParams);
//
//            //name layout params
//            RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            nameParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            nameParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//            //num layout params
//            RelativeLayout.LayoutParams numParams = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            numParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            numParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            int marginLeft = convertFromDpToPixels(80);
//            numParams.setMargins(marginLeft,0,0,0);

            //getting the font
            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.alef);

            // create name text
            GridLayout.LayoutParams nameParams = new GridLayout.LayoutParams();
            TextView name = new TextView(getActivity());
            nameParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            nameParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            nameParams.setGravity(Gravity.FILL_HORIZONTAL);
            int marginRight = convertFromDpToPixels(10);
            int marginLeft = convertFromDpToPixels(60);
            int marginTop = convertFromDpToPixels(10);
            nameParams.setMargins(marginLeft,marginTop,marginRight,0);
            name.setText(voluName);
            name.setTextColor(getResources().getColor(R.color.button_blue2));
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            name.setGravity(Gravity.RIGHT);
            name.setTypeface(typeface);
            name.setLayoutParams(nameParams);


            // create num of forms text
            GridLayout.LayoutParams numParams = new GridLayout.LayoutParams();
            numParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            numParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            numParams.setGravity(Gravity.FILL_HORIZONTAL);
            numParams.setMargins(marginLeft,marginTop,marginRight,0);

            TextView formsNum = new TextView(getActivity());
            formsNum.setText(numOfForms.get(voluName)+"");
            formsNum.setTextColor(getResources().getColor(R.color.button_blue2));
            formsNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            formsNum.setGravity(Gravity.RIGHT);
            formsNum.setTypeface(typeface);
            formsNum.setLayoutParams(numParams);

            //add to XML
            volunteersTbl.addView(name);
            volunteersTbl.addView(formsNum);
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
        avgText.setText("ממוצע שאלונים למתנדב: " + df2.format(avgFormsPerVolu));
        progressBar.setVisibility(View.GONE);

    }

    private int convertFromDpToPixels(int toConvert){
        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
        return pixels;
    }

    private Void showError(Void unused) {
        progressBar.setVisibility(View.GONE);
        Log.e("err","failed to load from dataBase");
        Toast.makeText(getActivity(), "Something went wrong (dataBase)", Toast.LENGTH_SHORT).show();
        return null;
    }
}