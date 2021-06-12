package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Spinner;
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

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        openFormDialog.openFormListener{

    private String voluName; //the clicked volunteer name
    private String voluId;//the clicked volunteer id
    private Volunteer voluData; //volu current data from the firestore

private TextView homeButton; //home button

    private ProgressBar loadingProgressBar; //progress bar

    private RecyclerView templateView; //recycle view list
    private RecyclerAdapter recyclerAdapter; //custom adapter for the view
    private HashMap<String, String> templates; //template hashmap
    private List<String> templatesNames; //template names
    private SearchView searchView;  //search bar
    private String clickedFormId; // the clicked form id
    private String clickedFormName; //the clicked form name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);

        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId = getIntent().getStringExtra("CLICKED_VOLU_ID");
        voluData = Global.getGlobalInstance().getVoluInstance();

        //init xml views
        templateView = findViewById(R.id.guide_templates_recycler_view);
        homeButton = findViewById(R.id.guideHomeFloating);
        loadingProgressBar = findViewById(R.id.guideFormsLoading);

        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
        loadingProgressBar.setVisibility(View.VISIBLE);
        searchView = findViewById(R.id.search_bar_permissions);
        // change close icon color
        ImageView iconClose = searchView.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.light_blue2));
        //change search icon color
        ImageView iconSearch = searchView.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.light_blue2));
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        homeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //clicking on go back home button - switch activities
        startActivity(new Intent(getApplicationContext(), GuideNavigationActivity.class));
        overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
        finish();
    }

    private Void onSuccess(Void unused) {
        Toast.makeText(getApplicationContext(),
                "הפעולה הושלמה בהצלחה",
                Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }
    //this function sends an error if unable to get the correct data from firebase
    private Void showError(Void unused) {
        Toast.makeText(GuideFormsPermissionActivity.this, "שגיאה בטעינת המידע", Toast.LENGTH_SHORT).show();
        loadingProgressBar.setVisibility(View.GONE);
        return null;
    }
    //this function returns the data selected from the form and if fails sends appropriate message
    private Void onGetTemplateSuccess(QuerySnapshot result) {
        //init a list of FormTemplates object from the collection
        String formNameField = FirebaseStrings.formNameStr();

        templates = new HashMap<>();
        for (DocumentSnapshot snapshot : result) {
            if (result == null) {
                showError(null);
                return null;
            }
            templates.put((String) snapshot.get(formNameField), snapshot.getId());
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
    //this function determines what to do depending on the user selection on the menu
    public boolean onMenuItemClick(MenuItem item) {
        //getting clicked text
        loadingProgressBar.setVisibility(View.GONE);
        clickedFormName = recyclerAdapter.getClickedText();
        clickedFormId = templates.get(clickedFormName);

        switch (item.getItemId()) {
            case R.id.open_curr_form:
                Log.d("onMenuItemClick", "open form to " + voluName + " form id " + clickedFormId);
                openForm();
                break;
            case R.id.allow_edit:
                Log.d("onMenuItemClick", "allow edit form to " + voluName + " form id " + clickedFormId);
                allowEdit();
                break;
            case R.id.disable_edit:
                Log.d("onMenuItemClick", "disable edit form to " + voluName + " form id " + clickedFormId);
                disableEdit();
                break;
        }
        return true;
    }
    //this function opens the correct form
    private void openForm() {
        //checking if the volunteer already has this form as an open form
        if (voluData.getHasOpenForm()) {
            if (voluData.getOpenFormName().equals(clickedFormName)) {
                Toast.makeText(GuideFormsPermissionActivity.this, "שאלון זה כבר פתוח למתנדב", Toast.LENGTH_SHORT).show();
                return;
            }
            //else - showing do you want to override ?
            DialogFragment dialogFragment = new openFormDialog();
            dialogFragment.show(getSupportFragmentManager(), "openForm");
            return;
        }

        //checking if the volunteer has this form in his answered forms
        if (voluData.getFinishedForms().get(clickedFormName)!=null) {
            Toast.makeText(GuideFormsPermissionActivity.this, "המתנדב כבר ענה על שאלון זה", Toast.LENGTH_SHORT).show();
            return;
        }

        //ok - continue
        Function<DocumentReference, Void> creatingAnswersDocSuccess = (document) -> {
            //updating the openForm field in the volunteer document
            Volunteer.addOpenForm(voluId, clickedFormName, document.getId());
            return onSuccess(null);
        };

        //creating new empty answered form document
        AnsweredForm ansForm = new AnsweredForm(false, true, clickedFormName, clickedFormId, new HashMap<>());
        FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.answeredFormsStr(), ansForm, creatingAnswersDocSuccess, this::showError);
    }

    //this function allows the form for the specific volunteer to be editable
    private void allowEdit() {
        String answersUid = voluData.getFinishedForms().get(clickedFormName);
        //checking if the volunteer has this form as an open form
        if (voluData.getOpenFormName().equals(clickedFormName)) {
            Toast.makeText(GuideFormsPermissionActivity.this, "זהו השאלון שכרגע פתוח למתנדב", Toast.LENGTH_SHORT).show();
            return;
        }

        //checking if the volunteer has this form in his answered
        if (answersUid == null) {
            Toast.makeText(GuideFormsPermissionActivity.this, "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר", Toast.LENGTH_SHORT).show();
            return;
        }

        //updating the answers can edit to true
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), answersUid, FirebaseStrings.finishedCanEditStr()
                , true, this::onSuccess, this::showError);
    }


    //this function disables the ability to edit a specific form for a volunteer
    private void disableEdit() {
        String answersUid = voluData.getFinishedForms().get(clickedFormName);
        //checking if the volunteer has this form as an open form
        if (voluData.getOpenFormName().equals(clickedFormName)) {
            Toast.makeText(GuideFormsPermissionActivity.this, "זהו השאלון שכרגע פתוח למתנדב", Toast.LENGTH_SHORT).show();
            return;
        }
        //checking if the volunteer has this form in his answered forms
        if (answersUid == null) {
            Toast.makeText(GuideFormsPermissionActivity.this, "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר", Toast.LENGTH_SHORT).show();
            return;
        }
        //updating the answers can edit to false
        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), answersUid, FirebaseStrings.finishedCanEditStr()
                , false, this::onSuccess, this::showError);
    }

    @Override
    public void onOpenFormPositiveClick(DialogFragment dialog) {
        //3. updating the openForm fields in the volunteer document
        Function<DocumentReference, Void> creatingAnswersDocSuccess = (document) -> {
            Volunteer.addOpenForm(voluId, clickedFormName, document.getId());
            return onSuccess(null);
        };

        //2. creating new empty answered form document
        Function<Void, Void> deleteOpenFormSuccess = (unused) -> {
            AnsweredForm ansForm = new AnsweredForm(false, true, clickedFormName, clickedFormId, new HashMap<>());
            FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.answeredFormsStr(), ansForm, creatingAnswersDocSuccess, this::showError);
            return null;
        };

        //1. delete the old openForm from answered collection
        FirestoreMethods.deleteDocument(FirebaseStrings.answeredFormsStr(),voluData.getOpenFormId(), deleteOpenFormSuccess,  this::showError);
    }

    @Override
    public void onOpenFormNegativeClick(DialogFragment dialog) {return;}
}


