package com.example.mishlavim.model;

import android.util.Log;
import android.widget.Toast;

import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.mishlavim.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Guide class represents an guide user.
 */
public class Guide extends User {

    private HashMap<String, String> myVolunteers;

    public Guide() {
    }

    public Guide(String name, String type, String email, HashMap<String, String> myVolunteers) {
        super(name, type, email);
        this.myVolunteers = myVolunteers;
    }

    public static void addVolunteerByGuideName(String voluID, Volunteer volu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(UserTypes.getUSER_COLLECTION());
        String guideName = volu.getMyGuide();

        //getting the given guide id
        usersRef.whereEqualTo("name", guideName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (task.getResult().isEmpty()) {
                            Log.d("Guide", "Cannot find guide in fireBase.");
                        } else {
                            AtomicReference<String> guideId = new AtomicReference<>();
                            //success at getting the guide id by his name
                            for (QueryDocumentSnapshot document : task.getResult())
                                guideId.set(document.getId());

                            //updating the guide list
                            addVolunteerByGuideId(guideId.get(), voluID, volu);
                        }

                    } else {
                        Log.d("Guide", "cannot find guide in fireBase");
                    }

                });

    }

    public static void addVolunteerByGuideId(String guideId, String voluID, Volunteer volu) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String voluRef = "myVolunteers." + volu.getName();

        //updating the guide list
        db.collection(UserTypes.getUSER_COLLECTION())
                .document(guideId)
                .update(voluRef, voluID)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful())
                        Log.d("Guide", "Guide successfully updated!");
                    else
                        Log.d("Guide", "Error - Guide update failed.", task2.getException());
                });
    }

    public HashMap<String, String> getMyVolunteers() {
        return myVolunteers;
    }

    public void setMyVolunteers(HashMap<String, String> myVolunteers) {
        this.myVolunteers = myVolunteers;
        // TODO: 5/24/2021 check if this update the firebase DB.
    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public void editVolunteer(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public Volunteer findVolunteer(String name) {
//        // TODO implement here
//        return null;
//    }
//
//    /**
//     * @param name
//     * @return
//     */
    public void deleteVolunteer(FirebaseUser fbUser, FirebaseFirestore db, User user ) {
        Log.d("delete", "deleteVolunteer: "+name+ "was remove");
        deleteUser toDelete = new deleteUser();
        toDelete.deleteUser(fbUser, db, user);

        // TODO implement here
    }
//
//    /**
//     * @param volunteerName
//     * @param formName
//     * @return
//     */
//    public void addAccess(String volunteerName, String formName) {
//        // TODO implement here
//    }

}