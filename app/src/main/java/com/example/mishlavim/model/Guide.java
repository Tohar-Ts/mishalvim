package com.example.mishlavim.model;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Guide class represents the guide user data.
 * Every guide extends three fields from User class: name, type, email.
 * Guide also have his own field:
 * 'myVolunteers' - an map consists of <Uid, name> for all the volunteers under the guide.
 * 'formsTemplates' - a list of all the forms templates <name, Uid>.
 * Guide class provide functions to handle the guide data - inserting/deleting/searching a volunteer
 * from 'myVolunteers' map.
 */
public class Guide extends User {

    private HashMap<String, String> myVolunteers;
    private HashMap<String, String> formsTemplates;

    /**
     * Empty constructor.
     */
    public Guide() {
    }

    /**
     * Parameterized constructor.
     */
    public Guide(String name, String type, String email, HashMap<String, String> myVolunteers, HashMap<String, String> formsTemplates) {
        super(name, type, email);
        this.myVolunteers = myVolunteers;
        this.formsTemplates = formsTemplates;
    }

    /**
     * Adding a volunteer to the 'myVolunteers' map of the guide.
     * Given only the guide name from a Volunteer object,
     * First the function finds the guide in the database,
     * then call 'addVolunteerByGuideId' function to set his data.
     *
     * @param voluID - the volunteer Uid
     * @param volu   - Volunteer object with the volunteer data. including his guide name.
     */
    public static void addVolunteerByGuideName(String voluID, Volunteer volu) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(FirebaseStrings.usersStr());
        String guideName = volu.getMyGuide();

        //getting the given guide id
        usersRef.whereEqualTo(FirebaseStrings.nameStr(), guideName)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (Objects.requireNonNull(task.getResult()).isEmpty())
                            Log.d("Guide class", "Guide document returned empty.");
                        else {

                            //succeed at getting the guide id by his name
                            AtomicReference<String> guideId = new AtomicReference<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                guideId.set(document.getId());

                            //updating the guide list
                            addVolunteerByGuideId(guideId.get(), voluID, volu);
                        }

                    } else
                        Log.d("Guide class", "cannot find guide in fireBase");
                });
    }

    /**
     * Adding a volunteer to the 'myVolunteers' map of the guide.
     * Given the guide id, the function set his data.
     *
     * @param guideId - the guide Uid to update
     * @param voluID  - the volunteer Uid
     * @param volu    - Volunteer object with the volunteer data
     */
    public static void addVolunteerByGuideId(String guideId, String voluID, Volunteer volu) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String voluRef = FirebaseStrings.myVolunteerStr() + "." + volu.getName();

        //updating the guide list
        db.collection(FirebaseStrings.usersStr())
                .document(guideId)
                .update(voluRef, voluID)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful())
                        Log.d("Guide", "Guide successfully updated!");
                    else
                        Log.d("Guide", "Error - Guide update failed.", task2.getException());
                });
    }

    /**
     * For the given guide, Finds the volunteer id from 'myVolunteer' map given his name,
     * Then gets the volunteer data from the database,
     * And finally init and returns a Volunteer object with the volunteer data.
     *
     * @param guideId  - guide Uid
     * @param voluName - volunteer name to find
     * @return Volunteer object with the volunteer data from the database
     */
    public static Volunteer findVolunteer(String guideId, String voluName) {
        // TODO implement here
        return null;
    }

    /**
     * For the given guide, deletes the volunteer from 'myVolunteer' map.
     *
     * @param guideId  - guide Uid
     * @param voluName - volunteer name to find
     */
    public static void deleteVolunteer(String guideId, String voluName) {
        /* TODO implement here */
//        Log.d("delete", "deleteVolunteer: " + name + "was remove");
//        deleteUser toDelete = new deleteUser();
//        toDelete.deleteUser(fbUser, db, user);
    }

    public HashMap<String, String> getMyVolunteers() {
        return myVolunteers;
    }

    public void setMyVolunteers(HashMap<String, String> myVolunteers) {
        this.myVolunteers = myVolunteers;
        // TODO: 5/24/2021 check if this update the firebase DB.
    }

}
