
package com.example.mishlavim.adminActivities;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.alertOpenFormDialog;
import com.example.mishlavim.dialogs.openFormCancelDialog;
import com.example.mishlavim.dialogs.openFormWarningDialog;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminWatchTemplate extends AppCompatActivity implements View.OnClickListener,
        openFormWarningDialog.openFormWarningListener, openFormCancelDialog.openFormCancelListener
{
    private TextView formName;
    private String clickedFormKey;
    private String clickedFormId;
    private HashMap <String, String> questions;
    private FloatingActionButton editBTM;
    private LinearLayout questionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view_old_form);

        editBTM = findViewById(R.id.questionsEditFloating);
        questionsLayout = findViewById(R.id.questionsTemplateLayout);
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


    private Void showError(Void v) {
        Toast.makeText(getApplicationContext(), R.string.firebase_failed, Toast.LENGTH_SHORT).show();
        return null;
    }

    private void getQuestionsFromFirebase() {
        FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), clickedFormId,
                this::onGettingQuestionsSuccess, this::showError);
    }



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

    private void displayFormOnScreen() {
        for (Map.Entry<String, String> qEntry : questions.entrySet()) {
            String question = qEntry.getValue();
            addQuestion(question);
        }
    }

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
        qTextView.setGravity(Gravity.CENTER | Gravity.START);
        qTextView.setBackgroundResource(R.drawable.custom_orange_textview);
        qTextView.setPadding(padding,padding,padding,padding);
        qTextView.setText(question);
        qTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new text view to the linearlayout
        questionsLayout.addView(qTextView);
    }


    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View v) {
        checkFreeFrom();

    }
    // check if there are some open forms so don't touch it.
    private void checkFreeFrom() {
        FirebaseFirestore.getInstance().collectionGroup(FirebaseStrings.usersStr())
                .whereEqualTo(FirebaseStrings.openFormUidStr(),clickedFormId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    showCancelDialog();
                    return;
                }
                showAlertDialog();
                return;
            }
        });

    }

    private void showCancelDialog() {
        DialogFragment dialogFragment = new openFormCancelDialog();
        dialogFragment.show(getSupportFragmentManager(),"cancel");
    }

    private void showAlertDialog() {
        DialogFragment dialogFragment = new alertOpenFormDialog();
        dialogFragment.show(getSupportFragmentManager(),"warning");
    }



    @Override
    public void onOpenFormWarningPositiveClick(DialogFragment dialog) {
        // TODO: 6/9/2021 go to edit form activity.
    }

    @Override
    public void onOpenFormWarningNegativeClick(DialogFragment dialog) {
        return;
    }

    @Override
    public void onOpenFormCancelNeutralClick(DialogFragment dialog) {
        return;
    }
}