package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
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
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.function.Function;

public class VolunteerFillOutFormActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private Button nextBtn, backBtn, saveBtn, sendBtn;
    private TextView fireBaseQuestion, questionNumTextView;
    private EditText volunteerAnswer;

    private String formId, voluId, voluName;
    private int numOfQuestions;
    private int numOfCurrentQuestion;
    private HashMap<String, String> questions;
    private HashMap<String, String> savedAnswers;
    private HashMap<String, String> currentAnswers;
    private Volunteer volunteer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_fill_out_form);

        //init xml views
        nextBtn = findViewById(R.id.next_btn);
        backBtn = findViewById(R.id.back_btn);
        saveBtn = findViewById(R.id.save_btn);
        sendBtn = findViewById(R.id.send_btn);
        fireBaseQuestion = findViewById(R.id.question);
        volunteerAnswer = findViewById(R.id.singleAns);
        questionNumTextView = findViewById(R.id.question_number);
        progressBar = findViewById(R.id.questionProgressBar);

        //init open form id
        Global globalInstance = Global.getGlobalInstance();
        volunteer = globalInstance.getVoluInstance();
        voluName = volunteer.getName();
        formId = volunteer.getOpenForm();

        //user is guide
        if (globalInstance.getType().equals(FirebaseStrings.guideStr())){
          voluId = globalInstance.getGuideInstance().getMyVolunteers().get(voluName);
        }
        else{//user is volunteer
            voluId = AuthenticationMethods.getCurrentUserID();
        }
        //getting answers object from firestore
        FirestoreMethods.getDocument(FirebaseStrings.answeredFormsStr(), formId, this::getAnswersObjSuccess, this::showError);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_btn)
            nextBtnOn();
        else if (v.getId() == R.id.back_btn)
            backBtnOn();
        else if (v.getId() == R.id.save_btn)
            saveBtnOn();
        else if (v.getId() == R.id.send_btn)
            sendBtnOn();
    }


    private Void showError(Void unused) {
        Toast.makeText(getApplicationContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
        return null;
    }

    private Void getAnswersObjSuccess(DocumentSnapshot doc) {
        assert doc != null;
        AnsweredForm answersObj = doc.toObject(AnsweredForm.class);

        assert answersObj != null;
        savedAnswers = answersObj.getAnswers();
        currentAnswers = new HashMap<>(savedAnswers);
        numOfCurrentQuestion = 1; //start from the beginning

        //getting questions object from firestore
        FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), answersObj.getTemplateId(), this::getTemplateObjSuccess, this::showError);

        return null;
    }

    private Void getTemplateObjSuccess(DocumentSnapshot doc) {
        assert doc != null;
        FormTemplate templateObj = doc.toObject(FormTemplate.class);

        assert templateObj != null;
        questions = templateObj.getQuestionArr();
        numOfQuestions = questions.size();

        initScreen();
        return null;
    }

    private void initScreen() {
        progressBar.setMax(numOfQuestions);

        showQuestion();

        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    //the next question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void nextBtnOn() {
        parseAnswer();

        if (numOfCurrentQuestion < numOfQuestions)
            numOfCurrentQuestion += 1;

        showQuestion();
    }

    //the previous question button is pressed - now we update the question from firebase, save the
    //answer and refresh the answer box
    private void backBtnOn() {
        parseAnswer();

        if (numOfCurrentQuestion > 1)
            numOfCurrentQuestion -= 1;

        showQuestion();
    }

    private void showQuestion() {
        progressBar.setProgress(numOfCurrentQuestion);

        if(numOfCurrentQuestion == 1)
            backBtn.setVisibility(View.GONE);
        else
            backBtn.setVisibility(View.VISIBLE);

        if(numOfCurrentQuestion == numOfQuestions) {
            nextBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
            sendBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.GONE);
        }


        questionNumTextView.setText("שאלה " + numOfCurrentQuestion);

        fireBaseQuestion.setText(questions.get(numOfCurrentQuestion + ""));

        if (currentAnswers.get(numOfCurrentQuestion + "") != null)
            volunteerAnswer.setText(currentAnswers.get(numOfCurrentQuestion + ""));
        else
            volunteerAnswer.setText("");
    }

    private void parseAnswer() {
        if (volunteerAnswer.getText() != null) {
            if(volunteerAnswer.getText().toString().isEmpty())
                currentAnswers.put(numOfCurrentQuestion + "", "");
            else
                currentAnswers.put(numOfCurrentQuestion + "", volunteerAnswer.getText().toString());
        }
    }

    private void saveBtnOn() {

        progressBar.setVisibility(View.VISIBLE);
        //parsing the last answer
        parseAnswer();
        //sending the updated answers to firestore
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), formId, FirebaseStrings.answersStr(), currentAnswers,
                                                        this::goToMain, this::showError);

    }

    private void sendBtnOn() {
        progressBar.setVisibility(View.VISIBLE);
        //parsing the last answer
        parseAnswer();

        //Validation for empty answers
        if (currentAnswers.containsValue("")){
            Toast.makeText(getApplicationContext(), R.string.empty_answer, Toast.LENGTH_SHORT).show();
            return;
        }

        //sending the updated answers to firestore
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), formId, FirebaseStrings.answersStr(), currentAnswers,
                this::updateOpenForm, this::showError);

        //TODO - updating on work field in answers
        //TODO - updating finished forms map in volunteer
        //TODO - notifying guide
    }
    //update the open form on the volunteer's document.
    private Void updateOpenForm(Void unused){
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), voluId, FirebaseStrings.openForm(),FirebaseStrings.emptyString(),
                this::updateFinishedForms, this::showError);
        return null;
    }
    private Void updateFinishedForms(Void unused){
//        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(),voluId,FirebaseStrings.finishedFormsStr(),,formId,this::updateOnWork,this::showError);
        return null;
    }

    private Void updateOnWork(Void unused){
        return null;
    }

    private Void notifyGuide(Void unused){
        showFinishedForm(unused);
        return null;
    }


    private Void showFinishedForm(Void unused) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VolunteerFillOutFormActivity.this, VolunteerFinishedFormActivity.class);
        startActivity(intent);
        finish();
        return null;
    }

    private Void goToMain(Void unused) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VolunteerFillOutFormActivity.this, VolunteerMainActivity.class);
        startActivity(intent);
        finish();
        return null;
    }


}