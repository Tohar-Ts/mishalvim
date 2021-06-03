package com.example.mishlavim.volunteerActivities;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.model.AnsweredForm;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VolunteerViewOldFormActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView formName;
    private FloatingActionButton editButton, saveButton;
    private LinearLayout savedAnswersLayout;

    private boolean canEdit;
    private String clickedFormId;
    private String clickedFormName;
    private Map<String, String> answers;
    private HashMap<String, String> questions;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view_old_form);

        //TODO: add a progress bar
        formName = findViewById(R.id.finishedFormName);
        editButton = findViewById(R.id.answersEditFloating);
        saveButton = findViewById(R.id.answersSaveFloating);
        savedAnswersLayout = findViewById(R.id.SavedAnswersLayout);

        //TODO: changes according to firebase
        canEdit = true;
        clickedFormId = "VoQPBSgwVj2gDhgnIQWs";
        clickedFormName = "form name";

        db = FirebaseFirestore.getInstance();

        displayFormName();
        updateEditMode();
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
    }

    public void displayFormName(){
        formName.setText(clickedFormName);
    }

    private void updateEditMode() {
        if(canEdit)
            editButton.setVisibility(View.VISIBLE);
        else
            editButton.setVisibility(View.GONE);

        saveButton.setVisibility(View.GONE);
    }

    private void getAnswersFromFirebase() {
        Function<DocumentSnapshot,Void> onSuccess = this::parseDocToAnsweredFormObj;
        Function<Void,Void> onFailure = this::showError;

        FirestoreMethods.getDocument(FirebaseStrings.answeredFormsStr(), clickedFormId,
                onSuccess, onFailure );
    }

    private Void parseDocToAnsweredFormObj(DocumentSnapshot document){
        assert document != null;
        AnsweredForm answersObj = document.toObject(AnsweredForm.class);

        assert answersObj != null;
        answers = answersObj.getAnswers();

        getTemplateFromFirebase(answersObj.getTemplateId());
        return null;
    }


    private void getTemplateFromFirebase(String templateId) {
        Function<DocumentSnapshot,Void> onSuccess = this::parseDocToFormTemplateObj;
        Function<Void,Void> onFailure = this::showError;

        FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), "Cfxrc4aUw5lnTOsNFk5B",
                                        onSuccess, onFailure );
    }

    private Void parseDocToFormTemplateObj(DocumentSnapshot document) {
        assert document != null;
        FormTemplate questionsObj = document.toObject(FormTemplate.class);

        assert questionsObj != null;
        questions = questionsObj.getQuestionArr();

        displayFormOnScreen();
        return null;
    }


    private void displayFormOnScreen() {

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
        int height = convertFromDpToPixels(64);
        int width = convertFromDpToPixels(330);
        int margin =  convertFromDpToPixels(16);
        int padding = convertFromDpToPixels(16);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(margin,margin,margin,margin);
        qTextView.setLayoutParams(params);
        qTextView.setGravity(Gravity.CENTER | Gravity.START);
        qTextView.setBackgroundResource(R.drawable.orange_textview);
        qTextView.setPadding(padding,padding,padding,padding);
        qTextView.setText(question);
        qTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new text view to the linearlayout
        savedAnswersLayout.addView( qTextView);
    }

    private void addTextAnswer(String answer) {
        //creating new TextView
        TextView aTextView = new TextView(this);

        //calculate height
        int height = convertFromDpToPixels(64);
        int width = convertFromDpToPixels(330);
        int margin =  convertFromDpToPixels(16);
        int padding = convertFromDpToPixels(16);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(margin,margin,margin,margin);
        aTextView.setLayoutParams(params);
        aTextView.setGravity(Gravity.CENTER | Gravity.START);
        aTextView.setPadding(padding,padding,padding,padding);
        aTextView.setText(answer);
        aTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(margin,margin,margin,margin);
        aEditText.setLayoutParams(params);
        aEditText.setPadding(padding,padding,padding,padding);
        aEditText.setGravity(Gravity.CENTER | Gravity.START);
        aEditText.setText(answer);
        aEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        //adding the new text view to the linearlayout
        savedAnswersLayout.addView(aEditText);
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

       updateAnswersInFirebase();
    }

    private void updateAnswersInFirebase() {
        Function<Void,Void> onSuccess = this::reloadScreen;
        Function<Void,Void> onFailure = this::showError;

        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), clickedFormId, FirebaseStrings.answersStr(), answers,
                                                onSuccess, onFailure);
    }

    private Void reloadScreen(Void unused) {
        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
        return null;
    }

    private Void showError(Void v) {
        Toast.makeText(getApplicationContext(), R.string.firebase_failed, Toast.LENGTH_SHORT).show();
        return null;
    }

    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

}