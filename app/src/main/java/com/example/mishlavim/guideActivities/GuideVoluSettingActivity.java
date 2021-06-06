package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mishlavim.R;

public class GuideVoluSettingActivity extends AppCompatActivity {

    private String voluToUpdateName, voluToUpdateId;
    private TextView voluName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_volu_setting);

        //innit xml views
        voluName = findViewById(R.id.edit_volu_name);


        //getting clicked volunteer details
        voluToUpdateName =  getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluToUpdateId =  getIntent().getStringExtra("CLICKED_VOLU_ID");

        //get the volunteer document.
        FirestoreMethods.getDocument(FirebaseStrings.usersStr(),voluToUpdateId,this::onGetDocSuccess,this::onGetDocFailed);

        Global globalInstance = Global.getGlobalInstance();//current user.

        if (globalInstance.getType().equals(FirebaseStrings.adminStr())){
            guidesSpinner = findViewById(R.id.spinner);
            guideName = findViewById(R.id.guideName);
            setSpinner();
            guidesSpinner.setVisibility(View.VISIBLE);
            guideName.setVisibility(View.VISIBLE);
        }

        group = findViewById(R.id.group2);
        userName = findViewById(R.id.edit_volu_name);
//        userName.setText(voluToUpdateName);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
        Button saveBTM = findViewById(R.id.updateBTM);


//        validation = new Validation(emailEditText, null, passwordEditText, null,
//                loadingProgressBar, getResources());

        saveBTM.setOnClickListener(this);

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

    public void setSpinner(){
        //SPINNER SETUP
        //get the guides list.
        Global globalInstance = Global.getGlobalInstance();
        Admin admin = globalInstance.getAdminInstance();
        HashMap<String,String> guideList = admin.getGuideList();

        listOfGuidesName = new ArrayList<>(guideList.keySet());
        listOfGuidesID = new ArrayList<>(guideList.values());

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this,android.R.layout.simple_spinner_dropdown_item,listOfGuidesName);

        guidesSpinner.setAdapter(adapter);
        guidesSpinner.setPrompt(volunteer.getMyGuide());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String key = listOfGuidesName.get(position);
        String value = listOfGuidesID.get(position);
        FirestoreMethods.moveVolunteer(voluToUpdateId,volunteer.getMyGuideId(),key,value);
        reloadScreen();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public Void onGetDocSuccess(DocumentSnapshot documentSnapshot){
        volunteer = documentSnapshot.toObject(Volunteer.class);
        return null;
    }
    public Void onGetDocFailed(Void noUse){return null;}

    }
}