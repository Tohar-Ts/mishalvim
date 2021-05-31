package com.example.mishlavim.model;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
    private HashMap<String, String> allVolunteers;

    /**
     * Empty constructor
     */
    public Admin() {
    }

    /**
     * Parameterized constructor
     */
    public Admin(String name, String type, String email, HashMap<String, String> guideList, HashMap<String, String> allVolunteers) {
        super(name, type, email);
        this.guideList = guideList;
        this.allVolunteers = allVolunteers;
    }

    /**
     * Adding a Guide to the 'guideList' map for all admins.
     *
     * @param guideID   - the guide Uid
     * @param guideName - Guide object with the guide data.
     */
    public static void addGuide(String guideID, String guideName) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(FirebaseStrings.usersStr());

        //getting admin id from the firebase
        usersRef.whereEqualTo(FirebaseStrings.typeStr(), FirebaseStrings.adminStr())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (Objects.requireNonNull(task.getResult()).isEmpty())
                            Log.d("Admin class", "No admin documents found.");
                        else {
                            //succeed at getting the admin id
                            AtomicReference<String> adminId = new AtomicReference<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                adminId.set(document.getId());

                            //updating the guide list
                            addGuideToAdminId(adminId.get(), guideID, guideName);
                        }

                    } else
                        Log.d("Admin class", "cannot find guide in fireBase");
                });
    }

   private static void addGuideToAdminId(String adminId, String guideID, String guideName){
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String guideRef = FirebaseStrings.guideListStr() + "." + guideName;

        //updating the guide list
        db.collection(FirebaseStrings.usersStr())
                .document(adminId)
                .update(guideRef, guideID)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful())
                        Log.d("Admin class", "Guide successfully updated!");
                    else
                        Log.d("Admin class", "Error - Admin update failed.", task2.getException());
                });
    }

    public static void addVolunteer(String voluID, String voluName) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(FirebaseStrings.usersStr());

        //getting admin id from the firebase
        usersRef.whereEqualTo(FirebaseStrings.typeStr(), FirebaseStrings.adminStr())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (Objects.requireNonNull(task.getResult()).isEmpty())
                            Log.d("Admin class", "No admin documents found.");
                        else {
                            //succeed at getting the admin id
                            AtomicReference<String> adminId = new AtomicReference<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                adminId.set(document.getId());

                            //updating the guide list
                            addVolunteerToAdminId(adminId.get(), voluID, voluName);
                        }

                    } else
                        Log.d("Admin class", "cannot find guide in fireBase");
                });

    }
    private static void  addVolunteerToAdminId(String adminId, String voluID, String voluName){
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String voluRef = FirebaseStrings.allVolunteersListStr() + "." + voluName;

        //updating the guide list
        db.collection(FirebaseStrings.usersStr())
                .document(adminId)
                .update(voluRef, voluID)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful())
                        Log.d("Admin class", "Guide successfully updated!");
                    else
                        Log.d("Admin class", "Error - Admin update failed.", task2.getException());
                });
    }
    /**
     * Adding a formTemplate to the 'formsTemplates' map.
     * The function generates a random form id.
     *
     * @param formTemp - FormTemplate object to add.
     */
    public static void addFormTemplate(FormTemplate formTemp) {
        /* TODO implement here */
    }

    /**
     * Finds the guide id from 'guideList' map given his name,
     * Then gets the guide data from the database,
     * And finally init and returns a Guide object with the guide data.
     *
     * @param guideName - guide name to find
     * @return Guide object with the guide data from the database
     */
    public Guide findGuide(String guideName) {
        /* TODO implement here */
        return null;
    }

    /**
     * Finds the form id from 'formsTemplates' map given his name,
     * Then gets the form data from the database,
     * And finally init and returns a FormTemplate object with the form data.
     *
     * @param formName - form name to find
     * @return FormTemplate object with the form data from the database
     */
    public FormTemplate findFormTemplate(String formName) {
        /* TODO implement here */
        return null;
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
        return allVolunteers;
    }

    public void setAllVolunteers(HashMap<String, String> allVolunteers) {
        this.allVolunteers = allVolunteers;
    }
}