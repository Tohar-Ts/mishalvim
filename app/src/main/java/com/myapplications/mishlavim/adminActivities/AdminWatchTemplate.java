
package com.myapplications.mishlavim.adminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.dialogs.openFormWarningDialog;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.FormTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminWatchTemplate extends AppCompatActivity implements View.OnClickListener,
        openFormWarningDialog.openFormWarningListener
{
    private TextView formName, homeButton;
    private String clickedFormKey;
    private String clickedFormId;
    private HashMap <String, String> questions;
    private FloatingActionButton editBTM, saveButton;
    private Button addQuestionButton;
    private LinearLayout questionsLayout;
    private ScrollView questionsScroll;
    private int numOfQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_watch_template_activity);

        //set the xml views for elements:
        addQuestionButton = findViewById(R.id.addNewWatchQuestion);
        addQuestionButton.setOnClickListener(this::onClick);

        editBTM = findViewById(R.id.questionsEditFloating);
        editBTM.setOnClickListener(this::onClick);

        saveButton = findViewById(R.id.SaveFloating);
        saveButton.setOnClickListener(this::onClick);

        homeButton = findViewById(R.id.guideTemplateHomeFloating);
        homeButton.setOnClickListener(this::onClick);

        questionsLayout = findViewById(R.id.questionsTemplateLayout);
        questionsScroll = findViewById(R.id.questionsTemplateScroll);
        //TODO: add a progress bar

        //Getting the clicked form id
        clickedFormKey =  getIntent().getStringExtra("CLICKED_FORM_KEY"); // NAME
        clickedFormId = getIntent().getStringExtra("CLICKED_FORM_VALUE"); // ID
        Log.d("TAG", "onCreate: key " + clickedFormKey);
        Log.d("TAG", "onCreate: id " +clickedFormId);

        formName = findViewById(R.id.finishedFormName);
        formName.setText(clickedFormKey);



        getQuestionsFromFirebase();

    }

    @Override
    //this function sets the click for the various options on the screen
    //either edit questions, save, add new question or view guide.
    public void onClick(View v) {

        if(v.getId() ==  R.id.questionsEditFloating)
            showWarningDialog();
        else if(v.getId() == R.id.SaveFloating)
            saveEditedQuestions();
        else if(v.getId() == R.id.addNewWatchQuestion)
            addQuestionOnScreen();
        else if(v.getId() == R.id.guideTemplateHomeFloating)
            goToHome();

    }
    //this is an error message shown if the data is not available from firebase
    private Void showError(Void v) {
        Toast.makeText(getApplicationContext(), R.string.firebase_failed, Toast.LENGTH_SHORT).show();
        return null;
    }
    //this initiates a go-home intent
    private void goToHome(){
        startActivity(new Intent(getApplicationContext(), AdminNavigationActivity.class));
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
    }
    //this gets the selected questions from the firebase database
    private void getQuestionsFromFirebase() {
        FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), clickedFormId,
                this::onGettingQuestionsSuccess, this::showError);
    }


    //displays the selected form on screen, if failure appropriate message is displayed
    private Void onGettingQuestionsSuccess(DocumentSnapshot document) {
        assert document != null;
        FormTemplate questionsObj = document.toObject(FormTemplate.class);
        Log.d("TAG", "onGettingQuestionsSuccess: "+ questionsObj);
        assert questionsObj != null;
        questions = questionsObj.getQuestionsMap();
        Log.d("TAG", "onGettingQuestionsSuccess: "+ questions);
        displayFormOnScreen();
        return null;
    }
    //this displays the various questions on screen from the hashmap of questions for the selected
    //volunteer
    private void displayFormOnScreen() {
        for (Map.Entry<String, String> qEntry : questions.entrySet()) {
            numOfQuestions++;
            String question = qEntry.getValue();
            addQuestion(question);
        }
    }
    //this function allows the addition of a question
    private void addQuestion(String question) {
        //creating new editText
        TextView qTextView = new TextView(this);
        Log.d("addQuestion", "addQuestion: question is: " +question);


        //calculate height
        int height = convertFromDpToPixels(64);
        int width = convertFromDpToPixels(330);
        int margin =  convertFromDpToPixels(16);
        int padding = convertFromDpToPixels(16);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin,margin,margin,margin);
        qTextView.setLayoutParams(params);
        qTextView.setGravity(Gravity.CENTER);
        qTextView.setBackgroundResource(R.drawable.white_text_background);
        qTextView.setPadding(padding,padding,padding,padding);
        qTextView.setText(question);
        qTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        
//        Typeface face=Typeface.createFromAsset(getAssets(),"font/alef.ttf");
//        qTextView.setTypeface(face);
        //qTextView.setTypeface(Typeface.createFromAsset(getAssets(), "alef.ttf"));

        //adding the new text view to the linearlayout
        questionsLayout.addView(qTextView);
    }


    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

    //this allows the selected form to be edited
    private void changeScreenToEditMode() {
        questionsLayout.removeAllViews();
        editBTM.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        addQuestionButton.setVisibility(View.VISIBLE);

        for (Map.Entry<String, String> qEntry : questions.entrySet()) {
            String question = qEntry.getValue();
            addEditQuestion(question);
        }
    }
    //this adds a new question to the form that has been edited
    private void addEditQuestion(String question) {
        //creating new editText
        EditText aEditText = new EditText(this);

        //calculate height
        int height = convertFromDpToPixels(50);
        int marginTop =  convertFromDpToPixels(24);
        int paddingEnd = convertFromDpToPixels(12);
        int paddingStart = convertFromDpToPixels(12);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  height);
        params.setMargins(0,marginTop,0,0);
        aEditText.setLayoutParams(params);
        aEditText.setBackgroundResource(R.drawable.custom_input);
        aEditText.setGravity(Gravity.CENTER | Gravity.START);
        aEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        aEditText.setPadding(paddingStart,0,paddingEnd,0);
        aEditText.setText(question);

        //adding the new text view to the linearlayout
        questionsLayout.addView(aEditText);
    }

    //this function saves the edited question in the firebase so it will not be lost upon the app closing
    private void saveEditedQuestions() {
//        loadingProgressBar.setVisibility(View.VISIBLE);
        ViewGroup group = findViewById(R.id.questionsTemplateLayout);
        questions = new HashMap<>();

        for (int i = 0, qNum = 1, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String answer = ((EditText)view).getText().toString().trim();
                if(answer.isEmpty()) continue;
                questions.put(qNum +"" , answer);
                qNum++;
                Log.d("view Template form", "adding answer number: " + qNum);
            }
        }
        //update answers in firebase
        FirestoreMethods.updateDocumentField(FirebaseStrings.formsTemplatesStr(), clickedFormId, FirebaseStrings.questionsMapStr(), questions,
                this::reloadScreen, this::showError);
    }

    //this adds the question on the screen so admin can then enter text and save it
    private void addQuestionOnScreen() {
        //creating new editText
        EditText question = new EditText(this);
        numOfQuestions ++;

        //calculate height
        int height = convertFromDpToPixels(50);
        int marginTop =  convertFromDpToPixels(24);
        int paddingEnd = convertFromDpToPixels(12);
        int paddingStart = convertFromDpToPixels(12);

        //styling
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  height);
        params.setMargins(0,marginTop,0,0);
        question.setLayoutParams(params);
        question.setBackgroundResource(R.drawable.custom_input);
        question.setGravity(Gravity.CENTER | Gravity.START);
        question.setHint("???????? "+ numOfQuestions+ ":");
        question.setInputType(InputType.TYPE_CLASS_TEXT);
        question.setPadding(paddingStart,0,paddingEnd,0);

        //adding the new edit text to the linearlayout
        questionsLayout.addView(question);

        //scroll view focus on the new question
        questionsScroll.post(() -> questionsScroll.scrollTo(0, questionsScroll.getBottom()));
    }

    //screen reloaded once the question is finished
    private Void reloadScreen(Void unused) {
        Toast.makeText(getApplicationContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
        return null;
    }


    //this is a warning dialog displayed once the condition for the warning is met
    private void showWarningDialog() {
        DialogFragment dialogFragment = new openFormWarningDialog();
        dialogFragment.show(getSupportFragmentManager(),"warning");
    }


    @Override
    public void onOpenFormWarningPositiveClick(DialogFragment dialog) {
        changeScreenToEditMode();
    }

    @Override
    public void onOpenFormWarningNegativeClick(DialogFragment dialog) {
        return;
    }


}