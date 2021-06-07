package com.example.mishlavim.model;

import android.util.Log;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

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

    /**
     * Empty constructor.
     */
    public Guide() {
    }

    /**
     * Parameterized constructor.
     */
    public Guide(String name, String type, String email, HashMap<String, String> myVolunteers) {
        super(name, type, email);
        this.myVolunteers = myVolunteers;
    }

    /**
     * Adding a volunteer to the 'myVolunteers' map of the guide.
     * Given the guide id, the function set his data.
     *
     * @param guideUid - the guide Uid to update
     * @param voluID  - the volunteer Uid
     * @param voluName   - Volunteer name
     */
    public static void addVolunteer(String guideUid, String voluID, String voluName) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Guide class", "Volunteer successfully updated!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Guide class", "Error - Guide update failed.");return null;};

        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), guideUid, FirebaseStrings.myGuideStr(), voluID,
                voluName,onSuccess,onFailure);
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
    }


}
