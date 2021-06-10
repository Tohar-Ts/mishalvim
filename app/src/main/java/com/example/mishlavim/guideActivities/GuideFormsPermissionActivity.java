package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.alertOpenFormDialog;
import com.example.mishlavim.dialogs.openFormDialog;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.AnsweredForm;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private String voluName; //the clicked volunteer name
    private String voluId;//the clicked volunteer id
    private Volunteer voluData; //volu current data from the firestore

    private FloatingActionButton homeButton;
    private ProgressBar loadingProgressBar;

    private RecyclerView templateView;
    private RecyclerAdapter recyclerAdapter;
    HashMap<String, String> templates;
    List<String> templatesNames;

    private String clickedFormId; // the clicked form id
    private String clickedFormName;
    private boolean newCanEdit, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);

        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId =  getIntent().getStringExtra("CLICKED_VOLU_ID");
        voluData = Global.getGlobalInstance().getVoluInstance();

        //init xml views
        templateView = findViewById(R.id.guide_templates_recycler_view);
        homeButton = findViewById(R.id.guideHomeFloating);
        loadingProgressBar = findViewById(R.id.guideFormsLoading);
        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
        loadingProgressBar.setVisibility(View.VISIBLE);

        homeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //clicking on go back home button - switch activities
        startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        finish();
        overridePendingTransition(0, 0);
    }

    private Void onSuccess(Void unused) {
        Toast.makeText(getApplicationContext(),
                "הפעולה הושלמה בהצלחה",
                Toast.LENGTH_SHORT).show();
        return null;
    }

    private Void showError(Void unused) {
        Toast.makeText(GuideFormsPermissionActivity.this, "שגיאה בטעינת המידע", Toast.LENGTH_SHORT).show();
        return null;
    }

    private Void onGetTemplateSuccess(QuerySnapshot result){
        //init a list of FormTemplates object from the collection
        String formNameField = FirebaseStrings.formNameStr();

        templates = new HashMap<>();
        for (DocumentSnapshot snapshot:result) {
            if(result == null) {
                showError(null);
                return null;
            }
            templates.put((String)snapshot.get(formNameField), snapshot.getId());
        }

        //init a list of Form Templates names
        templatesNames = new ArrayList<>(templates.keySet());

        //init the recycle view
        recyclerAdapter = new RecyclerAdapter(templatesNames, this, R.menu.guide_forms_option);
        templateView.setAdapter(recyclerAdapter);
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        clickedFormName = recyclerAdapter.getClickedText();
        clickedFormId = templates.get(clickedFormName);

        switch(item.getItemId() ){
            case R.id.open_curr_form:
                Log.d("onMenuItemClick", "open form to "+ voluName+" form id "+ clickedFormId);
                openForm();
                break;
            case R.id.allow_edit:
                Log.d("onMenuItemClick", "allow edit form to "+ voluName+" form id "+ clickedFormId);
                allowEdit();
                break;
            case R.id.disable_edit:
                Log.d("onMenuItemClick", "disable edit form to "+ voluName+" form id "+ clickedFormId);
                disableEdit();
                break;
        }
        return true;
    }

    private void openForm() {
        //checking if the volunteer already has an open form
        if(voluData.getHasOpenForm()){
            if(voluData.getOpenFormName().equals(clickedFormName)){
                Toast.makeText(GuideFormsPermissionActivity.this, "שאלון זה כבר פתוח לחניך", Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO - show dialog, are you sure you want to override current open form?
            Toast.makeText(GuideFormsPermissionActivity.this, "שימו לב שכבר יש שאלון פתוח שילך לאיבוד", Toast.LENGTH_SHORT).show();
            return;
        }
        //ok - continue
        Function<DocumentReference, Void> creatingAnswersDocSuccess = (document)->{
            //updating the openForm field in the volunteer document
            Volunteer.addOpenForm(voluId, clickedFormName, document.getId());
            return onSuccess(null);
            };

        //creating new empty answered form document
        AnsweredForm ansForm = new AnsweredForm(false, true, clickedFormName, clickedFormId, new HashMap<>());
        FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.answeredFormsStr(),ansForm,creatingAnswersDocSuccess, this::showError);
    }


    private void allowEdit() {
        String answersUid = voluData.getFinishedForms().get(clickedFormName);
        //checking if the volunteer has this form in his open forms
        if(answersUid == null){
            Toast.makeText(GuideFormsPermissionActivity.this, "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר", Toast.LENGTH_SHORT).show();
            return;
        }
        //updating the answers can edit to true
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), answersUid, FirebaseStrings.finishedCanEditStr()
                            ,true, this::onSuccess, this::showError);
    }

    private void disableEdit() {
        String answersUid = voluData.getFinishedForms().get(clickedFormName);
        //checking if the volunteer has this form in his open forms
        if(answersUid == null){
            Toast.makeText(GuideFormsPermissionActivity.this, "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר", Toast.LENGTH_SHORT).show();
            return;
        }
        //updating the answers can edit to false
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), answersUid, FirebaseStrings.finishedCanEditStr()
                ,false, this::onSuccess, this::showError);
    }

}


