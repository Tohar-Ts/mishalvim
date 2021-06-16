package com.myapplications.mishlavim.volunteerActivities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.model.AnsweredForm;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.FormTemplate;
import com.myapplications.mishlavim.model.Global;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class VolunteerViewOldFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView formName;
    private FloatingActionButton editButton, saveButton;
    private LinearLayout savedAnswersLayout;
    private boolean canEdit = false;
    private String clickedFormKey;
    private String clickedFormId;
    private HashMap<String, String> answers;
    private HashMap<String, String> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view_old_form);

        //TODO: add a progress bar
        formName = findViewById(R.id.finishedFormName);
        editButton = findViewById(R.id.answersEditFloating);
        saveButton = findViewById(R.id.answersSaveFloating);
        savedAnswersLayout = findViewById(R.id.SavedAnswersLayout);
        Button backHome =findViewById(R.id.voluBackHome);
        backHome.setOnClickListener(this);
        //Getting the clicked form id
        clickedFormKey =  getIntent().getStringExtra("CLICKED_FORM_KEY");
        Global globalInstance = Global.getGlobalInstance();
        HashMap<String, String> answeredForms = globalInstance.getVoluInstance().getFinishedForms();
        clickedFormId = answeredForms.get(clickedFormKey);
        Log.d("VolunteerViewOldFormActivity: ",clickedFormKey+", "+ clickedFormId);

        displayFormName();
        getAnswersFromFirebase();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.answersEditFloating) {
            changeScreenToEditMode();
            editButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        }
        else if(v.getId() == R.id.answersSaveFloating) {
            saveEditedAnswers();
            editButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.voluBackHome) {
            Intent i = new Intent(this, VolunteerMainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
            finish();
        }
    }
    //error function displayed when an error occurs
    private Void showError(Void v) {
        Toast.makeText(getApplicationContext(), R.string.firebase_failed, Toast.LENGTH_SHORT).show();
        return null;
    }
    //displays the form name
    public void displayFormName(){
        formName.setText(clickedFormKey);
    }

    private void changeScreenToEditMode() {
        savedAnswersLayout.removeAllViews();

        for (Map.Entry<String, String> qEntry : questions.entrySet()) {
            String questionNum = qEntry.getKey();
            String question = qEntry.getValue();
            String answer = answers.get(questionNum);
            if(answer == null)
                answer = "";
            addQuestion(question);
            addEditAnswer(answer);
        }
    }
    //allows the form to be edited and updates the buttons accordingly
    private void updateEditMode() {
        if(canEdit)
            editButton.setVisibility(View.VISIBLE);
        else
            editButton.setVisibility(View.GONE);

        saveButton.setVisibility(View.GONE);
    }
    //gets the answeres from firebase
    private void getAnswersFromFirebase() {
        FirestoreMethods.getDocument(FirebaseStrings.answeredFormsStr(), clickedFormId,
                this::onGettingAnswersSuccess, this::showError);
    }
    //succeeds if getting the answers
    private Void onGettingAnswersSuccess(DocumentSnapshot document){
        assert document != null;
        AnsweredForm answersObj = document.toObject(AnsweredForm.class);

        assert answersObj != null;
        answers = answersObj.getAnswers();
        canEdit = answersObj.getFinishedButCanEdit();
        String templateUid = answersObj.getTemplateId();

        FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), templateUid,
                this::onGettingQuestionsSuccess, this::showError);
        return null;
    }

    private Void onGettingQuestionsSuccess(DocumentSnapshot document) {
        assert document != null;
        FormTemplate questionsObj = document.toObject(FormTemplate.class);
        assert questionsObj != null;
        questions = questionsObj.getQuestionsMap();

        displayFormOnScreen();
        return null;
    }

    private void displayFormOnScreen() {
        updateEditMode();

        for (Map.Entry<String, String> qEntry : questions.entrySet()) {
            String questionNum = qEntry.getKey();
            String question = qEntry.getValue();
            String answer = answers.get(questionNum);
            if(answer == null)
                answer = "";
            addQuestion(question);
            addTextAnswer(answer);
        }

        editButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void addQuestion(String question) {
        //creating new editText
        TextView qTextView = new TextView(this);

        //calculate height
        int width = convertFromDpToPixels(350);
        int margin =  convertFromDpToPixels(8);
        int padding = convertFromDpToPixels(14);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin,margin,margin,margin);
        qTextView.setLayoutParams(params);
        qTextView.setGravity(Gravity.CENTER | Gravity.RIGHT);
        qTextView.setBackgroundResource(R.drawable.custom_orange_textview);
        qTextView.setPadding(padding,padding,padding,padding);
        qTextView.setText(question);
        qTextView.setTextColor( getResources().getColor(R.color.black));
        qTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        Typeface typeface = ResourcesCompat.getFont(VolunteerViewOldFormActivity.this, R.font.alef);
        qTextView.setTypeface(typeface);

        //adding the new text view to the linearlayout
        savedAnswersLayout.addView( qTextView);
    }

    private void addTextAnswer(String answer) {
        //creating new TextView
        TextView aTextView = new TextView(this);

        //calculate height
        int height = convertFromDpToPixels(64);
        int width = convertFromDpToPixels(350);
        int margin =  convertFromDpToPixels(8);
        int padding = convertFromDpToPixels(14);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin,margin,margin,margin);
        aTextView.setBackgroundResource(R.drawable.white_text_background);
        aTextView.setLayoutParams(params);
        aTextView.setGravity(Gravity.CENTER | Gravity.RIGHT);
        aTextView.setPadding(padding,padding,padding,padding);
        aTextView.setText(answer);
        aTextView.setTextColor( getResources().getColor(R.color.black));
        aTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        Typeface typeface = ResourcesCompat.getFont(VolunteerViewOldFormActivity.this, R.font.alef);
        aTextView.setTypeface(typeface);
        //adding the new text view to the linearlayout
        savedAnswersLayout.addView( aTextView);
    }

    private void addEditAnswer(String answer) {
        //creating new editText
        EditText aEditText = new EditText(this);

        //calculate height
        int height = convertFromDpToPixels(64);
        int width = convertFromDpToPixels(330);
        int margin =  convertFromDpToPixels(16);
        int padding = convertFromDpToPixels(16);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin,margin,margin,margin);
        aEditText.setLayoutParams(params);
        aEditText.setPadding(padding,padding,padding,padding);
        aEditText.setGravity(Gravity.CENTER | Gravity.RIGHT);
        aEditText.setText(answer);
        aEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        //adding the new text view to the linearlayout
        savedAnswersLayout.addView(aEditText);
    }

    private void saveEditedAnswers() {
//        loadingProgressBar.setVisibility(View.VISIBLE);
        ViewGroup group = findViewById(R.id.SavedAnswersLayout);

        for (int i = 0, qNum = 1, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String answer = ((EditText)view).getText().toString().trim();
                answers.put(qNum +"" , answer);
                qNum++;
                Log.d("view old form", "adding answer number: " + qNum);
            }
        }
        //update answers in firebase
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), clickedFormId, FirebaseStrings.answersStr(), answers,
                this::reloadScreen, this::showError);
    }

    private Void reloadScreen(Void unused) {
        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        startActivity(getIntent());
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        return null;
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

}