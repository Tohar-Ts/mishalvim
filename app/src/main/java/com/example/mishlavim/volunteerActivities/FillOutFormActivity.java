package com.example.mishlavim.volunteerActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.adminActivities.AdminCreateFormActivity;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.AnsweredForm;
import com.example.mishlavim.model.FirebaseStrings;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FillOutFormActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;
    public int progress = 1;
    public  int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
    HashMap<String, String> answers;
    public int max = intArray.length;
    public int numOfQuestion;
    private HashMap<String, String> questionArr;
    //xml elements:
    Button btNext;
    Button btBack;
    Button btSave;
    TextView fireBaseQuestion;
    EditText volunteerAnswer;
    String formName;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_out_form);

        init();

        btNext = (Button)findViewById(R.id.next_btn);
        btBack = (Button)findViewById(R.id.back_btn);
        btSave = (Button)findViewById(R.id.save_btn);
        fireBaseQuestion = (TextView)findViewById(R.id.question);
        volunteerAnswer = (EditText)findViewById(R.id.singleAns);
        //the next question button is pressed - now we update the question from firebase, save the
        //answer and refresh the answer box
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = progressBarAnimation.getProgress();
                Toast.makeText(getBaseContext(), "btNEXT detected", Toast.LENGTH_SHORT).show();
                progressBarAnimation.setProgress(progress+1);
                progress ++;

            }
        });
        //the previous question button is pressed - now we update the question from firebase, save the
        //answer and refresh the answer box
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = progressBarAnimation.getProgress();
                Toast.makeText(getBaseContext(), "btBack detected", Toast.LENGTH_SHORT).show();
                progressBarAnimation.setProgress(progress-1);
                progress --;

            }
        });
        getForm();
    }
    private void getNumOfQuestion(){

    }

    private void init() {
        progressBarAnimation = findViewById(R.id.questionProgressBar);
        progressAnimator = ObjectAnimator.ofInt(progressBarAnimation, "progress", progress, max);
        setMax(intArray);
        progressBarAnimation.setMax(max);
        progressBarAnimation.setProgress(progress);
    }

    private void setMax(int[] questionArray){
        max = intArray.length;
    }
    private int getMax(int max){
        return max;
    }
   private void getForm(){
       Global globalInstance = Global.getGlobalInstance();
       Volunteer volu = globalInstance.getVoluInstance();
       HashMap<String, String> openForms = volu.getOpenForms(); //key - form name, values - id in firebase
       String formId = openForms.get(formName); //get the id for the current form
       getFormFromFireBase(formId);
   }

    private void getFormFromFireBase(String formId) {
        db.collection(FirebaseStrings.answeredFormsStr())
                .document(formId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        AnsweredForm answersObj  = document.toObject(AnsweredForm.class);
                        appendAnswears(answersObj);
                        getQuestions(answersObj.getTemplateId());
                    } else {
//                        showAddingFailed();
                    }
//                    loadingProgressBar.setVisibility(View.GONE);
                });
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
                        numOfQuestion = questionArr.size();
//                        appendAnswears(answersObj);
//                        getQuestions(answersObj.getTemplateId());
                    } else {
//                        showAddingFailed();
                    }
//                    loadingProgressBar.setVisibility(View.GONE);
                });
    }
    // this function set the editText with the answear from the firebase

    private void appendAnswears (AnsweredForm answersObj ){
        volunteerAnswer.setText("hello");
    }
}