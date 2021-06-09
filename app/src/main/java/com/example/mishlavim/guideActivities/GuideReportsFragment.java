package com.example.mishlavim.guideActivities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mishlavim.R;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mishlavim.R;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.google.android.material.color.MaterialColors.getColor;


public class GuideReportsFragment extends Fragment implements View.OnClickListener{

    private Guide guide;
    private HashMap<String, String> myVolunteersMap;
    private TableLayout volunteersTbl;
    private HashMap<String, Integer> numOfForms = new HashMap<>();
    private TextView avgText;
    private View view;

    public GuideReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        //init xml views

        //init the guide an volunteers data
        Global globalInstance = Global.getGlobalInstance();
        guide = globalInstance.getGuideInstance();
        myVolunteersMap = guide.getMyVolunteers();
        volunteersTbl = view.findViewById(R.id.volunteers_tbl);
        avgText = view.findViewById(R.id.avg_text);
        startTbl();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_reports, container, false);
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
        //TODO fix this function makes the report crash
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
            TableRow newRow = new TableRow(getActivity());;
            TextView name = new TextView(getActivity());
            TextView formsNum = new TextView(getActivity());

            // create name text
//            TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            name.setLayoutParams(txtParams);
            name.setText(voluName);
            name.setGravity(Gravity.RIGHT);
//            name.setTextColor(getColor(R.color.black));
            //TODO fixthe get color function which doesnt work
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

            // create num of forms text
//            TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            name.setLayoutParams(txtParams);
            formsNum.setText(numOfForms.get(voluName)+"");
            formsNum.setGravity(Gravity.RIGHT);
//            formsNum.setTextColor(getColor(R.color.black));
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
        avgText.setText("ממוצע טפסים לחניך:" + avgFormsPerVolu);

    }
    private int convertFromDpToPixels(int toConvert){
        int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
        return pixels;
    }
    private Void showError(Void unused) {
        Log.e("err","failed to load from dataBase");
        Toast.makeText(getActivity(), "Something went wrong (dataBase)", Toast.LENGTH_SHORT).show();
        return null;
    }

}