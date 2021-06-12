package com.example.mishlavim.model;

import android.util.Log;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;

import java.util.HashMap;
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
     */
    public static void addVolunteer(String guideUid, String voluID, String voluName) {
        Function<Void, Void> onSuccess = unused -> {Log.d("Guide class", "Volunteer successfully updated!");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Guide class", "Error - Guide update failed.");return null;};

        FirestoreMethods.updateMapKey(FirebaseStrings.usersStr(), guideUid, FirebaseStrings.myVolunteerStr(),voluName ,
                voluID ,onSuccess,onFailure);
    }

    /**
     * For the given guide, deletes the volunteer from 'myVolunteer' map.
     */
    public static void deleteVolunteer(String guideUid, String voluName) {
        Log.d("Guide class", "trying to delete a volunteer named "+voluName + "from this guide map: "+guideUid);

        Function<Void, Void> onSuccess = unused -> {Log.d("Guide class", "Volunteer successfully deleted from the map.");return null;};
        Function<Void, Void> onFailure = unused ->  {Log.d("Guide class", "Error - Volunteer delete from the map failed.");return null;};

        FirestoreMethods.deleteMapKey(FirebaseStrings.usersStr(), guideUid, FirebaseStrings.myVolunteerStr(), voluName
                                        ,onSuccess,onFailure);
    }

    public HashMap<String, String> getMyVolunteers() {
        return myVolunteers;
    }

    public void setMyVolunteers(HashMap<String, String> myVolunteers) {
        this.myVolunteers = myVolunteers;
    }


}
