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
import com.example.mishlavim.model.Volunteer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private String voluName; //the clicked volunteer name
    private String voluId;//the clicked volunteer id

    private FloatingActionButton homeButton;

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
        //init xml views
        templateView = findViewById(R.id.guide_templates_recycler_view);
        homeButton = findViewById(R.id.guideHomeFloating);
        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
        homeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //clicking on go back home button - switch activities
        startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        finish();
        overridePendingTransition(0, 0);
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
        recyclerAdapter = new RecyclerAdapter(templatesNames, this, R.menu.templates_options_menu);
        templateView.setAdapter(recyclerAdapter);
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
                disableEdit();
                Log.d("onMenuItemClick", "disable edit form to "+ voluName+" form id "+ clickedFormId);
                break;
        }
        return true;
    }

    private void openForm() {
    }

    private void allowEdit() {
    }

    private void disableEdit() {
    }


}
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        if (item.getItemId() == R.id.open_curr_form) {
//            Log.d("onMenuItemClick", "open form to "+ voluName+" form id "+ clickedFormId);
//            // TODO: 07/06/2021  add "are you sure" pop up text: "לפתוח שאלון זה לחניך? שים לב, פעולה זאת תחליף את השאלון הפתוח אצל החניך לשאלון זה"
////            HashMap<String, String> answers = new HashMap<>();
////            AnsweredForm ansForm = new AnsweredForm(true, true, clickedFormId, answers);
////            FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.answeredFormsStr(),ansForm,this::updateOpenForm, this::showError);
////            Log.d("onMenuItemClick","ansForm "+ ansForm.getTemplateId());
//            return true;
//        }
//        else if (item.getItemId() == R.id.allow_edit) {
//            Log.d("onMenuItemClick", "allow edit form to "+ voluName+" form id "+ clickedFormId);
//            newCanEdit = true;
//            FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::updateCanEdit, this::showError);
//            return true;
//        }
//        else if (item.getItemId() == R.id.disable_edit) {
//            Log.d("onMenuItemClick", "disable edit form to "+ voluName+" form id "+ clickedFormId);
//            newCanEdit = false;
//            FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::updateCanEdit, this::showError);
//            return true;
//        }
//        return false;
//    }
//
//    private Void updateCanEdit(DocumentSnapshot doc) {
////        Volunteer volu = doc.toObject(Volunteer.class);
////        assert volu != null;
////        HashMap<String, String> voluFinishedForms = volu.getFinishedForms();
////        HashMap<String, String> voluTemplate = volu.getMyFinishedTemplate();
////        int i = 0;
////        Log.d("findFormToEdit", "clicked template id "+ clickedFormId);
////        for (String voluTemplateName : voluTemplate.keySet()) {
////            Log.d("findFormToEdit", "finishedTempId "+ voluTemplateName);
////            i++;
////            if (voluTemplate.get(voluTemplateName).compareTo(clickedFormId) == 0) {//if we found form template in the volunteer's finished templates with the same id
////                // the volu does have this form in his finished forms map
////                for (String voluFinishedFormName : voluFinishedForms.keySet()) {
////                    Log.d("findFormToEdit", "voluFinishedFormName "+ voluFinishedFormName);
////                    if (voluFinishedFormName.compareTo(templateMap.get(clickedFormId)) == 0) {
////                        //found the needed form
////                        Log.d("findFormToEdit", "now update ");
////                        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), voluFinishedForms.get(voluFinishedFormName), FirebaseStrings.canEdit(), newCanEdit, this::onSuccess, this::showError);
////                    }
////                }
////            }
//        }
//        if (i == voluTemplate.size()) {
//            //TODO change to popup dialog with "OK" button
//            Log.e("GuideFormsPermissionActivity", "something went wrong");
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר",
//                    Toast.LENGTH_SHORT);
//            toast.show();
//        }
//            return null;
//    }
//
//
//    private Void updateOpenForm(DocumentReference doc) {
////        String newId = doc.getId();
////        String formName = templateMap.get(clickedFormId);
////        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluId, FirebaseStrings.openFormStr(),newId,this::onSuccess, this::showError);
////        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluId, FirebaseStrings.openFormNameStr(),formName,this::onSuccess, this::showError);
////        Log.d("updateOpenForm"," ");
//        return null;
//    }
//    private Void onSuccess(Void unused) {
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "הפעולה הושלמה בהצלחה",
//                Toast.LENGTH_SHORT);
//        toast.show();
//        return null;
//    }
