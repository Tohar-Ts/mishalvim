package com.example.mishlavim.adminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

import com.example.mishlavim.R;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        question.setHint("שאלה "+ numOfQuestions+ ":");
        question.setInputType(InputType.TYPE_CLASS_TEXT);
        question.setPadding(paddingStart,0,paddingEnd,0);

        //adding the new edit text to the linearlayout
        questionsLayout.addView(question);

        //scroll view focus on the new question
        questionsScroll.post(() -> questionsScroll.scrollTo(0, questionsScroll.getBottom()));
    }

    private void processNewForm() {

        loadingProgressBar.setVisibility(View.VISIBLE);

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

        String formName = formNameEditText.getText().toString().trim();
        //adding the form to firestore
        FormTemplate newForm = new FormTemplate(questionsMap, formName);
        FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.formsTemplatesStr(),newForm,
                                                this::onAddingSuccess, this::onAddingFailed);
    }

    private Void onAddingFailed(Void unused) {
        Toast.makeText(getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

    private Void onAddingSuccess(DocumentReference documentReference) {
        Toast.makeText(getContext(), R.string.firebase_success, Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        //updating global to see changes
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }
    private Void updateGlobalFinished(Boolean status){
        if(status)
            Toast.makeText(getActivity(), "המידע עודכן בהצלחה", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "תקלה בעדכון המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

}