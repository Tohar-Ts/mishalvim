package com.example.mishlavim.adminActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.FormTemplate;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminCreateFormActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText formNameEditText, question1EditText, question2EditText, question3EditText;
    private Button addQuestionButton, saveButton;
    private ProgressBar loadingProgressBar;
    private FirebaseFirestore db;
    private int numOfQuestions;
    private LinearLayout questionsLayout;
    private ScrollView questionsScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_form);

        formNameEditText = findViewById(R.id.formName);
        question1EditText = findViewById(R.id.question1);
        question2EditText = findViewById(R.id.question2);
        question3EditText = findViewById(R.id.question3);
        loadingProgressBar = findViewById(R.id.createFormLoading);
        addQuestionButton = findViewById(R.id.addNewQuestion);
        saveButton = findViewById(R.id.addNewForm);
        questionsLayout = findViewById(R.id.questionsLayout);
        questionsScroll = findViewById(R.id.questionsScroll);
        db = FirebaseFirestore.getInstance();
        numOfQuestions = 3;

        addQuestionButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.addNewQuestion)
            addQuestionOnScreen();
        else if(v.getId() == R.id.addNewForm)
            processNewForm();
    }

    private void addQuestionOnScreen() {
        //creating new editText
        EditText question = new EditText(this);
        numOfQuestions ++;

        //calculate height
        int width = convertFromDpToPixels(50);
        int marginTop =  convertFromDpToPixels(24);
        int paddingEnd = convertFromDpToPixels(12);
        int paddingStart = convertFromDpToPixels(12);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,  width);
        params.setMargins(0,marginTop,0,0);
        question.setLayoutParams(params);
        question.setBackgroundResource(R.drawable.custom_input);
        question.setGravity(Gravity.CENTER | Gravity.START);
        question.setHint("שאלה "+ numOfQuestions+ ":");
        question.setInputType(InputType.TYPE_CLASS_TEXT);
        question.setPadding(paddingStart,0,paddingEnd,0);
        questionsLayout.addView(question);
        questionsScroll.post(new Runnable() {
            public void run() {
                questionsScroll.scrollTo(0, questionsScroll.getBottom());
            }
        });

    }

    private void processNewForm() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> questionsMap = new HashMap<>();
        ViewGroup group = findViewById(R.id.questionsLayout);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String question = ((EditText)view).getText().toString().trim();
                questionsMap.put(i+"", question);
            }
        }
        String formName = formNameEditText.getText().toString().trim();
        addFormToDataBase(formName, questionsMap);
    }

    private void addFormToDataBase(String formName, HashMap<String, String> questionsMap) {
        FormTemplate newForm = new FormTemplate(questionsMap, formName,new HashMap<>());
        db.collection( FirebaseStrings.formsTemplatesStr())
                .document()
                .set(newForm)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminCreateFormActivity.this, formName + " was added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        showAddingFailed();
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                });
    }

    private void showAddingFailed() {
        Toast.makeText(getApplicationContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
    }


    private int convertFromDpToPixels(int toConvert){
       int pixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
       return pixels;
    }

}