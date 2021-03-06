package com.myapplications.mishlavim.adminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.myapplications.mishlavim.model.FormTemplate;
import com.myapplications.mishlavim.model.Global;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminCreateFormFragment extends Fragment implements View.OnClickListener {
    private EditText formNameEditText;
    private Button saveButton, addQuestionButton;
    private ProgressBar loadingProgressBar;
    private int numOfQuestions;
    private LinearLayout questionsLayout;
    private ScrollView questionsScroll;
    private View view;

    public AdminCreateFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_create_form, container, false);
    }

    @Override
    //init the Xml views and various variables needed for the view to run:
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        formNameEditText = view.findViewById(R.id.formName);
        loadingProgressBar = view.findViewById(R.id.createFormLoading);
        addQuestionButton = view.findViewById(R.id.addNewQuestion);
        saveButton = view.findViewById(R.id.addNewForm);
        questionsLayout = view.findViewById(R.id.questionsLayout);
        questionsScroll = view.findViewById(R.id.questionsScroll);

        numOfQuestions = 3; //default
        loadingProgressBar.setVisibility(View.GONE);

        addQuestionButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    //this functions cycles between adding an additional question on screen or a new form depending on the button selected
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addNewQuestion:
                addQuestionOnScreen();
                break;
            case R.id.addNewForm:
                processNewForm();
                break;
        }
    }

    private int convertFromDpToPixels(int toConvert){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

    private void addQuestionOnScreen() {
        //creating new editText
        EditText question = new EditText(getContext());
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

    //this function adds a new form to the firebase and displays it on-screen
    //additional checks are performed to make sure it is non-null
    private void processNewForm() {

        loadingProgressBar.setVisibility(View.VISIBLE);
        String formName = formNameEditText.getText().toString().trim();
        //validation form name
        if (formName.isEmpty()) {
            formNameEditText.setError("???? ?????????? ???????? ?????? ????????");
            loadingProgressBar.setVisibility(View.GONE);
            return;
        }

        HashMap<String, String> questionsMap = new HashMap<>();
        ViewGroup group = view.findViewById(R.id.questionsLayout);
        int qIndex = 1;
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                String question = ((EditText)view).getText().toString().trim();
                if(question.isEmpty())
                    continue;
                questionsMap.put(qIndex+"", question);
                qIndex++;
            }
        }


        //adding the form to firestore
        FormTemplate newForm = new FormTemplate(questionsMap, formName);
        FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.formsTemplatesStr(),newForm,
                                                this::onAddingSuccess, this::onAddingFailed);
    }
    //this function displays an error if the form is not added successfully for some reason
    private Void onAddingFailed(Void unused) {
        Toast.makeText(getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }
    //this function displays a success message upon the successful addition of a form
    private Void onAddingSuccess(DocumentReference documentReference) {
        Toast.makeText(getContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        //updating global to see changes
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }
    //this function displays either an error or success message depending on the outcome of the update to the firebase database
    private Void updateGlobalFinished(Boolean status){
        if(status)
            Toast.makeText(getActivity(), "?????????? ?????????? ????????????", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "???????? ???????????? ??????????, ???? ?????????? ???????????? ???? ?????????????????? ????????", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

}