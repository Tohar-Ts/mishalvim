package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener{

    private String voluName;
    private String voluId;
    LinearLayout formsList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);
        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId =  getIntent().getStringExtra("CLICKED_VOLU_ID");
        formsList = findViewById(R.id.forms_list);
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(),this::createFormsList, this::showError);

    }

    private Void showError(Void unused) {
        return null;
    }

    private Void createFormsList(QuerySnapshot docArr) {
        for (QueryDocumentSnapshot document : docArr) {
            Log.d("createFormsList", document.getId() + " => " + document.getData());
            FormTemplate form = document.toObject(FormTemplate.class);
            Log.d("createFormsList", form.getFormName()+"");
            
        }

        return null;
    }



    @Override
    public void onClick(View v) {

    }
}