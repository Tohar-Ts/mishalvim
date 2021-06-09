package com.example.mishlavim.guideActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
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
import com.example.mishlavim.model.AnsweredForm;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.FormTemplate;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;

public class GuideFormsPermissionActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private String voluName; //the clicked volunteer name
    private String voluId;//the clicked volunteer id
    private TableLayout formsList; //layout
    private String clickedFormId; // the clicked form id
    //private String clickedFormName;
    private HashMap <String, String> templateMap = new HashMap<>();// all template : key = id val = form name
    private boolean newCanEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_forms_permission);
        voluName = getIntent().getStringExtra("CLICKED_VOLU_KEY");
        voluId =  getIntent().getStringExtra("CLICKED_VOLU_ID");
        formsList = findViewById(R.id.forms_list);
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(),this::createFormsList, this::showError);//create table
    }

    private Void createFormsList(QuerySnapshot docArr) {
        for (QueryDocumentSnapshot document : docArr) {
            // go over all the forms templates
            FormTemplate form = document.toObject(FormTemplate.class);
            Log.d("createFormsList", form.getFormName()+"");
            addToTbl(form.getFormName(),document.getId());
        }
        return null;
    }

    private void addToTbl(String formName, String id) {
        TableRow newRow = new TableRow(this);
        //calculate height
        int height = convertFromDpToPixels(60);
        int padding =  convertFromDpToPixels(10);
        int marginBottom =  convertFromDpToPixels(20);

        //styling
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        rowParams.setMargins(0,marginBottom,0,marginBottom);
        newRow.setLayoutParams(rowParams);
        newRow.setPadding(padding,padding,padding,padding);
        newRow.setBackgroundResource(R.drawable.borders);

        //creating new options image
        ImageView optionImg = new ImageView(this);
        //calculate height
        int width = convertFromDpToPixels(40);
        int marginEnd =  convertFromDpToPixels(25);

        //styling
        TableRow.LayoutParams imgParams = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        imgParams.setMargins(marginEnd,0,marginEnd,0);
        optionImg.setLayoutParams(imgParams);
        optionImg.setBackgroundResource(R.drawable.ic_round_more_horiz);
        optionImg.setClickable(true);
        optionImg.setOnClickListener(this);
        optionImg.setTag(id);

        //creating new text view
        TextView formNameView = new TextView(this);

        //calculate height
        marginEnd =  convertFromDpToPixels(40);

        //styling
        TableRow.LayoutParams txtParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtParams.setMargins( marginEnd,0,marginEnd,0);
        formNameView.setLayoutParams(txtParams);
        formNameView.setText(formName);
        formNameView.setGravity(Gravity.RIGHT);
        formNameView.setTextColor(getColor(R.color.black));
        formNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);

        //adding the new row to the tablelayout
        newRow.addView(optionImg);
        newRow.addView(formNameView);
        formsList.addView(newRow);

        //adding space
        marginEnd =  convertFromDpToPixels(10);
        Space space = new Space(this);
        TableRow.LayoutParams spcParams =  new TableRow.LayoutParams(0, marginEnd);
        space.setLayoutParams(spcParams);
        formsList.addView(space);
        templateMap.put(id,formName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guide_forms_option, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.open_curr_form) {
            Log.d("onMenuItemClick", "open form to "+ voluName+" form id "+ clickedFormId);
            // TODO: 07/06/2021  add "are you sure" pop up text: "לפתוח שאלון זה לחניך? שים לב, פעולה זאת תחליף את השאלון הפתוח אצל החניך לשאלון זה"
            HashMap<String, String> answers = new HashMap<>();
            AnsweredForm ansForm = new AnsweredForm(true, true, clickedFormId, answers);
            FirestoreMethods.createNewDocumentRandomId(FirebaseStrings.answeredFormsStr(),ansForm,this::updateOpenForm, this::showError);
            Log.d("onMenuItemClick","ansForm "+ ansForm.getTemplateId());
            return true;
        }
        else if (item.getItemId() == R.id.allow_edit) {
            Log.d("onMenuItemClick", "allow edit form to "+ voluName+" form id "+ clickedFormId);
            newCanEdit = true;
            FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::updateCanEdit, this::showError);
            return true;
        }
        else if (item.getItemId() == R.id.disable_edit) {
            Log.d("onMenuItemClick", "disable edit form to "+ voluName+" form id "+ clickedFormId);
            newCanEdit = false;
            FirestoreMethods.getDocument(FirebaseStrings.usersStr(), voluId, this::updateCanEdit, this::showError);
            return true;
        }
        return false;
    }

    private Void updateCanEdit(DocumentSnapshot doc) {
        Volunteer volu = doc.toObject(Volunteer.class);
        assert volu != null;
        HashMap<String, String> voluFinishedForms = volu.getFinishedForms();
        HashMap<String, String> voluTemplate = volu.getMyFinishedTemplate();
        int i = 0;
        Log.d("findFormToEdit", "clicked template id "+ clickedFormId);
        for (String voluTemplateName : voluTemplate.keySet()) {
            Log.d("findFormToEdit", "finishedTempId "+ voluTemplateName);
            i++;
            if (voluTemplate.get(voluTemplateName).compareTo(clickedFormId) == 0) {//if we found form template in the volunteer's finished templates with the same id
                // the volu does have this form in his finished forms map
                for (String voluFinishedFormName : voluFinishedForms.keySet()) {
                    Log.d("findFormToEdit", "voluFinishedFormName "+ voluFinishedFormName);
                    if (voluFinishedFormName.compareTo(templateMap.get(clickedFormId)) == 0) {
                        //found the needed form
                        Log.d("findFormToEdit", "now update ");
                        FirestoreMethods.updateDocumentField(FirebaseStrings.answeredFormsStr(), voluFinishedForms.get(voluFinishedFormName), FirebaseStrings.canEdit(), newCanEdit, this::onSuccess, this::showError);
                    }
                }
            }
        }
        if (i == voluTemplate.size()) {
            //TODO change to popup dialog with "OK" button
            Log.e("GuideFormsPermissionActivity", "something went wrong");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
            return null;
    }


    private Void updateOpenForm(DocumentReference doc) {
        String newId = doc.getId();
        String formName = templateMap.get(clickedFormId);
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluId, FirebaseStrings.openFormStr(),newId,this::onSuccess, this::showError);
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(),voluId, FirebaseStrings.openFormNameStr(),formName,this::onSuccess, this::showError);
        Log.d("updateOpenForm"," ");
        return null;
    }
    private Void onSuccess(Void unused) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "הפעולה הושלמה בהצלחה",
                Toast.LENGTH_SHORT);
        toast.show();
        return null;
    }
    private Void showError(Void unused) {
        Log.e("GuideFormsPermissionActivity", "something went wrong");
        Toast toast = Toast.makeText(getApplicationContext(),
                "לא ניתן להשלים את הפעולה",
                Toast.LENGTH_SHORT);
        toast.show();
        return null;
    }

    @Override
    public void onClick(View v) {
        clickedFormId = (String)v.getTag();
        //showing the popup menu
        Context myContext = new ContextThemeWrapper(GuideFormsPermissionActivity.this,R.style.menuStyle);
        PopupMenu popup = new PopupMenu(myContext, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.guide_forms_option);
        popup.show();

    }
    private int convertFromDpToPixels(int toConvert){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toConvert, getResources().getDisplayMetrics());
    }
}