package com.example.mishlavim.model;

import android.util.Log;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Admin class represents an admin user.
 * Every Admin extends three fields from User class: name, type, email.
 * Admin have his own fields:
 * 'guideList' - a list of all the guides <name, Uid>.
 * 'formsTemplates' - a list of all the forms templates <name, Uid>.
 * 'allVolunteers' - a list of all the volunteers <name, guide>.
 * Admin class provide functions to handle the admin data:
 * Inserting/deleting/searching a guide from 'guideList' map.
 * Inserting/deleting/searching a form template from 'formsTemplates' map.
 * Inserting/deleting/searching a form template from 'allVolunteers' map.
 */
public class Admin extends User {

    private HashMap<String, String> guideList;
    private HashMap<String, String> formsTemplatesList;

    /**
     * Empty constructor
     */
    public Admin() {
    }

    /**
     * Parameterized constructor
     */
    public Admin(String name, String type, String email, HashMap<String, String> guideList, HashMap<String, String> formsTemplatesList) {
        super(name, type, email);
        this.guideList = guideList;
        this.formsTemplatesList = formsTemplatesList;
    }

    /**
     * Adding a Guide to the 'guideList' map for all admins.
     *
     * @param guideID   - the guide Uid
     * @param guideName - Guide object with the guide data.
     */
    public static void addGuide(String adminUid, String guideID, String guideName) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Admin class", "Guide successfully updated!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Admin class", "Error - Admin update failed.");return null;};
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), adminUid, FirebaseStrings.guideListStr(), guideID,
                                        guideName,onSuccess,onFailure);
    }

    /**
     * Adding a formTemplate to the 'formsTemplates' map.
     */
    public static void addFormTemplate(String adminUid, String templateID, String templateName) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Admin class", "Template successfully updated!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Admin class", "Error - Admin update failed.");return null;};

        FirestoreMethods.updateMapKey(FirebaseStrings.formsTemplatesStr(), adminUid, FirebaseStrings.formsTemplatesListStr(), templateID,
                templateName,onSuccess,onFailure);
    }

    /**
     * Deletes a guide from 'guideList' map.
     *
     * @param guideName - guide name to find
     */
    public void deleteGuide(String guideName) {
        // TODO implement here
    }

    /**
     * Deletes a form template from 'formsTemplates' map.
     *
     * @param formName - form name to find
     */
    public void deleteForm(String formName) {
        // TODO implement here
    }

    public HashMap<String, String> getGuideList() {
        return guideList;
    }

    public void setGuideList(HashMap<String, String> guideList) {
        this.guideList = guideList;
    }

    public HashMap<String, String> getAllVolunteers() {
        return formsTemplatesList;
    }

    public void setAllVolunteers(HashMap<String, String> allVolunteers) {
        this.formsTemplatesList = allVolunteers;
    }
}