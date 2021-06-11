package com.example.mishlavim.model;

import android.util.Log;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Volunteer class represents the volunteer user data.
 * Every volunteer extends three fields from User class: name, type, email.
 * Volunteer also have his own field:
 * 'myGuide' - his guide name.
 * 'answeredForms' - an map consists of <Uid, name> for all the forms the volunteer answered.
 * 'openForms' - an map consists of <Uid, name> for all the open forms the volunteer can still edit and answer.
 * Volunteer class provide functions to handle the volunteer data - inserting/deleting/searching a form
 * from 'answeredForms' and 'openForms' maps.
 */
public class Volunteer extends User {

    private String myGuide;
    private String myGuideId;

    private HashMap<String, String> finishedForms;
    private  HashMap<String, String> myFinishedTemplate;

    private String openFormId;
    private String openFormName;
    private boolean hasOpenForm;


    /**
     * Empty constructor.
     */
    public Volunteer() {
    }

    /**
     * Parameterized constructor.
     */
    public Volunteer(String name, String type, String email, String myGuide, String myGuideId, HashMap<String, String> finishedForms, String openFormId, boolean hasOpenForm, HashMap<String, String> myFinishedTemplate, String openFormName) {
        super(name, type, email);
        this.myGuide = myGuide;
        this.myGuideId = myGuideId;
        this.finishedForms = finishedForms;
        this.openFormId = openFormId;
        this.hasOpenForm = hasOpenForm;
        this.myFinishedTemplate = myFinishedTemplate;
        this.openFormName = openFormName;
    }

    /**
     * Adding a open form to the 'openForms' map of a volunteer.
     * Updating the change in the database.
     */
    public static void addOpenForm(String voluUid, String formName, String formUid) {
        Function<Void, Void> onSuccess = unused -> { Log.d("Volunteer class", "openForm successfully updated!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Volunteer class", "Error - openForm update failed.");return null;};

        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), voluUid, FirebaseStrings.openFormNameStr(),
                formName,onSuccess,onFailure);
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), voluUid, FirebaseStrings.openFormUidStr(),
                formUid,onSuccess,onFailure);
        FirestoreMethods.updateDocumentField(FirebaseStrings.usersStr(), voluUid, FirebaseStrings.hasOpenFormStr(),
                true,onSuccess,onFailure);
    }

    /*
     * Getters and setters.
     */

    public String getMyGuide() {
        return myGuide;
    }

    public void setMyGuide(String myGuide) {
        this.myGuide = myGuide;
    }

    public String getMyGuideId() {
        return myGuideId;
    }

    public void setMyGuideId(String myGuideId) {
        this.myGuideId = myGuideId;
    }

    public HashMap<String, String> getFinishedForms() {
        return finishedForms;
    }

    public void setFinishedForms(HashMap<String, String> finishedForms) {
        this.finishedForms = finishedForms;
    }

    public HashMap<String, String> getMyFinishedTemplate() {
        return myFinishedTemplate;
    }

    public void setMyFinishedTemplate(HashMap<String, String> myFinishedTemplate) {
        this.myFinishedTemplate = myFinishedTemplate;
    }

    public String getOpenFormId() {
        return openFormId;
    }

    public void setOpenFormId(String openFormId) {
        this.openFormId = openFormId;
    }

    public String getOpenFormName() {
        return openFormName;
    }

    public void setOpenFormName(String openFormName) {
        this.openFormName = openFormName;
    }

    public boolean getHasOpenForm() {
        return hasOpenForm;
    }

    public void setHasOpenForm(boolean hasOpenForm) {
        this.hasOpenForm = hasOpenForm;
    }
}