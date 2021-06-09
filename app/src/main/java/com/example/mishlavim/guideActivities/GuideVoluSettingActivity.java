package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mishlavim.R;
import com.example.mishlavim.login.Validation;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;




public class GuideVoluSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName, guideName;
    private EditText name, email, password,password_confirm ;
    private Validation validation;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private String voluToUpdateName, voluToUpdateId;
    private Volunteer volunteer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        //getting clicked volunteer details
        voluToUpdateName =  getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluToUpdateId =  getIntent().getStringExtra("CLICKED_VOLU_ID");

        //init FB
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        // init elements
        userName = findViewById(R.id.edit_volu_name);
        guideName = findViewById(R.id.edit_guide_name);
        name = findViewById(R.id.name_guide_vulo_setting);
        email = findViewById(R.id.email_guide_vulo_setting);
        password = findViewById(R.id.password_guide_vulo_setting);
        password_confirm = findViewById(R.id.password_confirm_guide_volu_setting);
        Button saveBTM = findViewById(R.id.updateBTM_vulo_setting);

//        validation = new Validation(emailEditText, null, passwordEditText, null,
//                loadingProgressBar, getResources());

        saveBTM.setOnClickListener(this);

        //get the volunteer document.
        FirestoreMethods.getDocument(FirebaseStrings.usersStr(),voluToUpdateId,this::onGetDocSuccess,this::onGetDocFailed);
    }

    @Override
    public void onClick(View v) {updateUserDetails();}

    private void updateUserDetails(){
        // TODO: 6/6/2021 use validation before update.

        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluToUpdateId,FirebaseStrings.nameStr(),name.getText(),task ->{
            FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluToUpdateId,FirebaseStrings.emailStr(),name.getText(),task2 ->{
                reloadScreen();
                return  null;},this::onGetDocFailed);
            return  null;},this::onGetDocFailed);

    }



    public Void onGetDocSuccess(DocumentSnapshot documentSnapshot){
        volunteer = documentSnapshot.toObject(Volunteer.class);
        return null;
    }
    public Void onGetDocFailed(Void noUse){return null;}

    private void reloadScreen() {
        finish();
        startActivity(getIntent());
    }
}