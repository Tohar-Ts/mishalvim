package com.myapplications.mishlavim.model;

import android.util.Log;

import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;

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

    /**
     * Empty constructor
     */
    public Admin() {
    }

    /**
     * Parameterized constructor
     */
    public Admin(String name, String type, String email, HashMap<String, String> guideList) {
        super(name, type, email);
        this.guideList = guideList;
    }

    /**
     * Adding a Guide to the 'guideList' map for all admins.
     */
    public static void addGuide(String adminUid, String guideName, String guideID) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Admin class", "Guide successfully updated in admin map!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Admin class", "Error - guide update in admin map failed.");return null;};
        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), adminUid, FirebaseStrings.guideListStr(), guideName,
                                        guideID,onSuccess,onFailure);
    }

    /**
     * Deletes a guide from 'guideList' map.
     */
    public static void deleteGuide(String adminUid, String guideName) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Admin class", "Guide successfully deleted from the map.");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Admin class", "Error - Guide delete from the map failed.");return null;};
        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), adminUid, FirebaseStrings.guideListStr(), guideName ,onSuccess,onFailure);
    }

    public HashMap<String, String> getGuideList() {
        return guideList;
    }

    public void setGuideList(HashMap<String, String> guideList) {
        this.guideList = guideList;
    }

}