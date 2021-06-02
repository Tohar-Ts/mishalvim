package com.example.mishlavim.volunteerActivities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.model.AnsweredForm;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FillOutFormActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;
    public int numOfCurrentQuestion = 1;
    //public  int[] questionNumArr;
    //HashMap<String, String> answers;
    public FormTemplate form;
    public int numOfQuestion = 10;
    private HashMap<String, String> questionArr;
    private Map<String, String> savedAnswers;
    private Map<String, String> currentAnswers = new HashMap<>();
    //xml elements:
    Button nextBtn;
    Button backBtn;
    Button saveBtn;
    TextView fireBaseQuestion;
    TextView questionNumTextView;
    EditText volunteerAnswer;
    String formName;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_out_form);
        nextBtn = (Button) findViewById(R.id.next_btn);
        backBtn = (Button) findViewById(R.id.back_btn);
        saveBtn = (Button) findViewById(R.id.save_btn);
        fireBaseQuestion = (TextView) findViewById(R.id.question);
        volunteerAnswer = (EditText) findViewById(R.id.singleAns);
        questionNumTextView = (TextView) findViewById(R.id.question_number);
        getForm();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_btn)
            nextBtnOn();
        else if (v.getId() == R.id.back_btn)
            backBtnOn();
        else if (v.getId() == R.id.save_btn)
            saveBtnOn();
    }

    //the next question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void nextBtnOn() {
            if (volunteerAnswer.getText() != null) {
                if(volunteerAnswer.getText().toString().isEmpty())
                    currentAnswers.put(numOfCurrentQuestion + "", "");
                else
                    currentAnswers.put(numOfCurrentQuestion + "", volunteerAnswer.getText().toString());
                Log.d("FillOutFormActivity", ""+currentAnswers.get(numOfCurrentQuestion+""));
            }
            if (numOfCurrentQuestion < numOfQuestion) {
            numOfCurrentQuestion += 1;
            fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion + ""));
            if (savedAnswers.get(numOfCurrentQuestion + "") != null)
                volunteerAnswer.setText(savedAnswers.get(numOfCurrentQuestion + ""));
            else
                volunteerAnswer.setText("");
            questionNumTextView.setText("שאלה " + numOfCurrentQuestion);

            progressBarAnimation.setProgress(numOfCurrentQuestion);
        }

    }

    //the previous question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void backBtnOn() {
        if (volunteerAnswer.getText() != null) {
            if(volunteerAnswer.getText().toString().isEmpty())
                currentAnswers.put(numOfCurrentQuestion + "", "");
            else
                currentAnswers.put(numOfCurrentQuestion + "", volunteerAnswer.getText().toString());
            Log.d("FillOutFormActivity", ""+currentAnswers.get(numOfCurrentQuestion+""));
        }
        if (numOfCurrentQuestion > 1) {
            numOfCurrentQuestion -= 1;
            questionNumTextView.setText("שאלה " + numOfCurrentQuestion);
            fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion + ""));
            if (savedAnswers.get(numOfCurrentQuestion + "") != null)
                volunteerAnswer.setText(savedAnswers.get(numOfCurrentQuestion + ""));
            else
                volunteerAnswer.setText("");
            if (volunteerAnswer.getText() != null) {
                currentAnswers.put(numOfCurrentQuestion + "", volunteerAnswer.getText() + "");
                Log.d("FillOutFormActivity", ""+currentAnswers.get(numOfCurrentQuestion+""));
            }
            progressBarAnimation.setProgress(numOfCurrentQuestion);
        }
    }

    private void saveBtnOn() {
        sendAnswersToDataBase();
    }


    private void getForm() {
        Global globalInstance = Global.getGlobalInstance();
        Volunteer volu = globalInstance.getVoluInstance();
        String formId = volu.getOpenForm(); //answer form id in firebase
        Log.d("name", "  " + volu.getEmail());
        getAnswers("VoQPBSgwVj2gDhgnIQWs");//TODO: change to volunteer openForm ID
    }

    private void getAnswers(String formId) {
        db.collection(FirebaseStrings.answeredFormsStr())
                .document(formId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        Log.d("template", "template ID " + task.getResult().get("templateId"));
                        AnsweredForm answersObj = document.toObject(AnsweredForm.class);
                        if (answersObj == null)
                            Log.d("ERR", "answersObj is null");
                        savedAnswers = answersObj.getAnswers();
                        currentAnswers = savedAnswers;//???
                        getQuestions(answersObj.getTemplateId());
                    } else {
//                        showAddingFailed();
                    }
//                    loadingProgressBar.setVisibility(View.GONE);
                });
    }

//    private void appendAnswers(AnsweredForm answersObj) {
//        volunteerAnswer.setText("hello");
//    }

    private void getQuestions(String templateId) {
        db.collection(FirebaseStrings.formsTemplatesStr())
                .document(templateId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        FormTemplate templateObj = document.toObject(FormTemplate.class);
                        questionArr = templateObj.getQuestionArr();
                        Log.d("question 1", " " + questionArr.get("1"));
                        numOfQuestion = questionArr.size();
                        init();
                    } else {
//                        showAddingFailed();
                    }
//
                });
    }

    private void init() {
        Log.d("number of question", " " + numOfQuestion);
        progressBarAnimation = findViewById(R.id.questionProgressBar);
        progressAnimator = ObjectAnimator.ofInt(progressBarAnimation, "progress", numOfCurrentQuestion, numOfQuestion);
        progressBarAnimation.setProgress(numOfCurrentQuestion);
        fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion + ""));
        //initial the current answers array
//        for (int i = 0; i < numOfQuestion; i++) {
//            currentAnswears.put(i + "", "");
//        }
        if (savedAnswers.get(numOfCurrentQuestion + "") != null)
            volunteerAnswer.setText(savedAnswers.get(numOfCurrentQuestion + ""));
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    private void sendAnswersToDataBase() {
        db.collection( FirebaseStrings.answeredFormsStr())
                .document("VoQPBSgwVj2gDhgnIQWs")
                .update(FirebaseStrings.answersStr(), currentAnswers)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("view old form", "new answers updated successfully!");
                        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    else {
                        Log.d("view old form", "Error - new answers update failed.", task.getException());
                        //showError();
                    }
                });
    }
}