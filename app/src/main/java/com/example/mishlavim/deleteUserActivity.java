package com.example.mishlavim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mishlavim.dialogs.passAllVolunteersDialog;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;

import java.util.HashMap;

public class deleteUserActivity extends AppCompatActivity implements View.OnClickListener, passAllVolunteersDialog.passAllVolunteersListener{

    private Button deleteBtn;
    private TextView email;
    private ProgressBar progressBar;
    private Global global;
    private User userData;
    private String userToDeleteType, userToDeleteUid, userUpdatingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        deleteBtn = findViewById(R.id.deleteBtn);
        progressBar = findViewById(R.id.deleteLoading);
        email = findViewById(R.id.deleteEmail);
        //init user data
        userToDeleteType = getIntent().getStringExtra("CLICKED_USER_TYPE");
        userToDeleteUid =  getIntent().getStringExtra("CLICKED_USER_ID");

        global = Global.getGlobalInstance();
        userUpdatingType = global.getType();

        initUserDataByType();
        email.setText(userData.getEmail());

        //init listeners
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deleteBtn:
                showDialog();
                break;
//            case R.id.settingHomeFloating:
//                //clicking on go back home button - switch activities
//                switchToHomeByUser();
//                break;
        }
    }

    private void initUserDataByType() {
        if(userToDeleteType.equals(FirebaseStrings.guideStr())){
            userData = global.getGuideInstance();
        }
        else if(userToDeleteType.equals(FirebaseStrings.volunteerStr())){
            userData = global.getVoluInstance();
        }
    }

    private void showDialog() {
        //NOTICE!! you cannot delete guide of he still have volunteers!
        //TODO - change dialog text to - "this action will delete all user relative data! are you sure?"
        DialogFragment dialogFragment = new passAllVolunteersDialog();
        dialogFragment.show(getSupportFragmentManager(), "passAll");
    }

    private Void showError(Void unused) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(deleteUserActivity.this, "המחיקה נכשלה", Toast.LENGTH_SHORT).show();
        return null;
    }

    private Void showLoginError(Void unused) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(deleteUserActivity.this, "סיסמה לא נכונה", Toast.LENGTH_SHORT).show();
        return null;
    }
    @Override
    public void passAllVolunteersPositiveClick(DialogFragment dialog) {
        parseInput();
    }

    private void parseInput() {

        progressBar.setVisibility(View.VISIBLE);
        EditText passwordText = findViewById(R.id.deletePassword);

        String email = userData.getEmail();
        String password = passwordText.getText().toString().trim();

        if(password.isEmpty()){
            passwordText.setError("יש להזין סיסמה");
            passwordText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        AuthenticationMethods.signIn(email,password, this::signInSuccess, this::showLoginError);
    }


    private Void signInSuccess(String s) {
        if(userToDeleteType.equals(FirebaseStrings.guideStr())){
            Log.d("deleteUserActivity: ", "attempting to delete this guide:" + userToDeleteUid);
            deleteGuide();
        }
        else if(userToDeleteType.equals(FirebaseStrings.volunteerStr())){
            Log.d("deleteUserActivity: ", "attempting to delete this volunteer:" + userToDeleteUid);
            deleteVolu();
        }
        return null;
    }

    private void deleteGuide(){
        //delete in admin map
        Admin.deleteGuide(global.getUid(), userData.getName());

        //delete doc from firestore
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(), userToDeleteUid, this::deleteSuccess, this::showError);
    }


    private void deleteVolu(){
        //delete in the guide map
        Guide.deleteVolunteer(global.getUid(), userData.getName());

        //delete doc from firestore
        Log.d("deleteUser:", "starting to deleted volu doc");
        FirestoreMethods.deleteDocument(FirebaseStrings.usersStr(), userToDeleteUid, this::deleteVoluAnsweredForms, this::showError);
    }

    private Void deleteVoluAnsweredForms(Void unused) {
        Log.d("deleteUser:", "starting to deleted volu form");
        //deleting volu answered forms from answers collection
        HashMap<String, String> answeredForms = global.getVoluInstance().getFinishedForms();

        if(answeredForms.size() == 0){
            Log.d("deleteUser:", "no answered forms for this volunteer");
            deleteVoluOpenForm();
            return null;
        }

        for(String formUid : answeredForms.values()){
            Log.d("deleteUser:", "trying to delete volu answered form:"+ formUid);
            FirestoreMethods.deleteDocument(FirebaseStrings.answeredFormsStr(), formUid, this::singleFormDeleted, this::showError);
        }

        deleteVoluOpenForm();
        return null;
    }

    private Void singleFormDeleted(Void unused){
        Log.d("deleteUser:", "deleted volu form");
        return null;
    }

    private void deleteVoluOpenForm(){
        //deleting volu open form from answers collection
        if(global.getVoluInstance().getHasOpenForm()){
            Log.d("deleteUser:", "deleted volu open form");
            FirestoreMethods.deleteDocument(FirebaseStrings.answeredFormsStr(),global.getVoluInstance().getOpenFormId(), this::deleteSuccess, this::showError);
        }
        else {
            Log.d("deleteUser:", "no open forms for this volunteer");
            deleteSuccess(null);
        }
    }

    private Void deleteSuccess(Void unused) {
        Global.updateGlobalData(this::updateGlobalFinished);
        return null;
    }

    private Void updateGlobalFinished(Boolean status) {
        if (status)
            Toast.makeText(deleteUserActivity.this, "המחיקה הסתיימה בהצלחה", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(deleteUserActivity.this, "תקלה בעדכון המידע, יש לסגור ולפתוח את האפליקציה מחדש", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        //delete from auth
        AuthenticationMethods.deleteAuthUser();
        return null;
    }

    @Override
    public void passAllVolunteersNegativeClick(DialogFragment dialog) {
    return;
    }
}