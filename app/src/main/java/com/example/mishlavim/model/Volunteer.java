package com.example.mishlavim.model;

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
    private String openForm;
    private boolean hasOpenForm;
    private  HashMap<String, String> myFinishedTemplate;
    private String openFormName;

    /**
     * Empty constructor.
     */
    public Volunteer() {

    }

    /**
     * Parameterized constructor.
     */
    public Volunteer(String name, String type, String email, String myGuide, String myGuideId, HashMap<String, String> finishedForms, String openForm, boolean hasOpenForm, HashMap<String, String> myFinishedTemplate, String openFormName) {
        super(name, type, email);
        this.myGuide = myGuide;
        this.myGuideId = myGuideId;
        this.finishedForms = finishedForms;
        this.openForm = openForm;
        this.hasOpenForm = hasOpenForm;
        this.myFinishedTemplate = myFinishedTemplate;
        this.openFormName = openFormName;
    }

    /**
     * Adding a open form to the 'openForms' map of a volunteer.
     * Updating the change in the database.
     *
     * @param voluId - the volunteer Uid
     * @param formId - the form Uid
     * @param form   - AnsweredForm object containing the form data
     */
    public static void addOpenForm(String voluId, String formId, AnsweredForm form) {
        /* TODO implement here */
    }

    /**
     * For the given volunteer, finds the open form id from 'openForms' map given his name,
     * Then returns a Form object with the finished form data.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     * @return AnsweredForm object with the form data from the map
     */
    public static AnsweredForm findOpenForm(String voluId, String formName) {
        /* TODO implement here */
        return null;
    }

    /**
     * For the given volunteer, deletes a open form from 'openForms' map.
     * Updating the change in the database.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     */
    public static void deleteOpenForm(String voluId, String formName) {
        // TODO implement here
    }

    /**
     * For the given volunteer, adding a open form to the 'finishedForms' map.
     * Updating the change in the database.
     *
     * @param voluId - the volunteer Uid
     * @param formId - the form Uid
     * @param form   - AnsweredForm object containing the form data
     * @param onSuccess - function to execute when update success
     * @param onFailed -  function to execute when update failed.
     */
    public static void addFinishedForm(String voluId, String formId, AnsweredForm form, Function<Void,Void> onSuccess, Function<Void,Void> onFailed) {
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(),voluId,FirebaseStrings.finishedFormsStr(),form.getTemplateId(),formId,onSuccess,onFailed);
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

    public HashMap<String, String> getFinishedForms() {
        return finishedForms;
    }

    public void setFinishedForms(HashMap<String, String> finishedForms) {
        this.finishedForms = finishedForms;
    }

    public String getOpenForm() {
        return openForm;
    }

    public HashMap<String, String> getMyFinishedTemplate() {
        return myFinishedTemplate;
    }

    public void setOpenForm(String openForm) {
        this.openForm = openForm;
    }

    public String getMyGuideId() {
        return myGuideId;
    }

    public void setMyGuideId(String myGuideId) {
        this.myGuideId = myGuideId;
    }

    public boolean isHasOpenForm() {
        return hasOpenForm;
    }

    public void setHasOpenForm(boolean hasOpenForm) {
        this.hasOpenForm = hasOpenForm;
    }



}