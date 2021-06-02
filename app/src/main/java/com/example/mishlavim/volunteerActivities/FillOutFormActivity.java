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
    HashMap<String, String> answers;
    public FormTemplate form;
    public int numOfQuestion = 10;
    private HashMap<String, String> questionArr;
    private Map<String, String> answerArr;
    //xml elements:
    Button btNext;
    Button btBack;
    Button btSave;
    TextView fireBaseQuestion;
    TextView questionNumTextView;
    EditText volunteerAnswer;
    String formName;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_out_form);
        btNext = (Button)findViewById(R.id.next_btn);
        btBack = (Button)findViewById(R.id.back_btn);
        btSave = (Button)findViewById(R.id.save_btn);
        fireBaseQuestion = (TextView)findViewById(R.id.question);
        volunteerAnswer = (EditText)findViewById(R.id.singleAns);
        questionNumTextView = (TextView)findViewById(R.id.question_number);
        getForm();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.next_btn)
            nextBtnOn();
        else if(v.getId() == R.id.back_btn)
            backBtnOn();
    }
    //the next question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void nextBtnOn() {
        Log.d("can click", "next");
        numOfCurrentQuestion = progressBarAnimation.getProgress();
        //Toast.makeText(getBaseContext(), "btNEXT detected", Toast.LENGTH_SHORT).show();
        if(numOfCurrentQuestion < numOfQuestion)
            numOfCurrentQuestion += 1;
        fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion+""));
        if(answerArr.get(numOfCurrentQuestion+"") != null)
            volunteerAnswer.setText(answerArr.get(numOfCurrentQuestion+""));
        else
            volunteerAnswer.setText("");
        questionNumTextView.setText( "שאלה "+ numOfCurrentQuestion);
        progressBarAnimation.setProgress(numOfCurrentQuestion);

    }
    //the previous question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void backBtnOn() {
        numOfCurrentQuestion = progressBarAnimation.getProgress();
        //Toast.makeText(getBaseContext(), "btBack detected", Toast.LENGTH_SHORT).show();
        if(numOfCurrentQuestion>1)
            numOfCurrentQuestion -= 1;
        questionNumTextView.setText( "שאלה "+ numOfCurrentQuestion);
        fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion+""));
        if(answerArr.get(numOfCurrentQuestion+"") != null)
            volunteerAnswer.setText(answerArr.get(numOfCurrentQuestion+""));
        else
            volunteerAnswer.setText("");
        questionNumTextView.setText( "שאלה "+ numOfCurrentQuestion);
        progressBarAnimation.setProgress(numOfCurrentQuestion);
    }
//    private void setMax(int numOfQuestion){
//        max = numOfQuestion;
//    }
//    private int getMax(int max){
//        return max;
//    }

    private void getForm(){
        Global globalInstance = Global.getGlobalInstance();
        Volunteer volu = globalInstance.getVoluInstance();
        String formId = volu.getOpenForm(); //answer form id in firebase
        Log.d("name", "  "+ volu.getEmail());
        getAnswers("VoQPBSgwVj2gDhgnIQWs");//TODO: change to volunteer openForm ID
   }

    private void getAnswers(String formId) {
        db.collection(FirebaseStrings.answeredFormsStr())
                .document(formId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("template","template ID "+ task.getResult().get("templateId"));
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        AnsweredForm answersObj = document.toObject(AnsweredForm.class);
                        if(answersObj == null)
                            Log.d("ERR","answersObj is null");
                        //appendAnswers(answersObj);
                        answerArr = answersObj.getAnswers();
                        getQuestions(answersObj.getTemplateId());
                    } else {
//                        showAddingFailed();
                    }
//                    loadingProgressBar.setVisibility(View.GONE);
                });
    }
    private void appendAnswers (AnsweredForm answersObj ){
        volunteerAnswer.setText("hello");
    }
    private void getQuestions(String templateId) {
        db.collection(FirebaseStrings.formsTemplatesStr())
                .document(templateId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        FormTemplate templateObj  = document.toObject(FormTemplate.class);
                        questionArr = templateObj.getQuestionArr();
                        Log.d("question 1", " "+ questionArr.get("1"));
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
        fireBaseQuestion.setText(questionArr.get(numOfCurrentQuestion+""));
        btNext.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }

    // this function set the editText with the answer from the firebase

//    private void getDataFromFirebase() {
//        db.collection(FirebaseStrings.answeredFormsStr())
//                .document(clickedFormId)
//                .get()
//                .addOnCompleteListener(VolunteerViewOldFormActivity.this, task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("view old form", "got answers successfully!");
//                        DocumentSnapshot document = task.getResult();
//                        assert document != null;
//                        AnsweredForm answersObj = document.toObject(AnsweredForm.class);
//                        answers = answersObj.getAnswers();
//                        Log.d("view old form", "answers are: " +  answers);
//                        getTemplateFromFirebase(answersObj.getTemplateId());
//                    } else {
//                        Log.d("view old form", "Error - getting answers failed.", task.getException());
//                        showError();
//                    }
//                });
//    }
//
//    private void getTemplateFromFirebase(String templateId) {
//        db.collection(FirebaseStrings.formsTemplatesStr())
//                .document("Cfxrc4aUw5lnTOsNFk5B")
//                .get()
//                .addOnCompleteListener(VolunteerViewOldFormActivity.this, task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("view old form", "got questions successfully!");
//                        DocumentSnapshot document = task.getResult();
//                        assert document != null;
//                        FormTemplate questionsObj = document.toObject(FormTemplate.class);
//                        assert questionsObj != null;
//                        questions = questionsObj.getQuestionArr();
//                        Log.d("view old form", "questions are: " + questions);
//                        displayFormOnScreen();
//                    } else {
//                        Log.d("view old form", "Error - getting questions failed.", task.getException());
//                        showError();
//                    }
//                });
//    }
//
//
//    private void displayFormOnScreen() {
//
//        }
//    }
}