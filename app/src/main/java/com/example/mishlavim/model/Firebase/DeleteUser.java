package com.example.mishlavim.model.Firebase;
import android.util.Log;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.User;
import com.example.mishlavim.model.Volunteer;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class DeleteUser {


    public void deleteUser(FirebaseUser fbUser, FirebaseFirestore db, User user){

        CollectionReference reference = db.collection(FirebaseStrings.usersStr());

        // user is GUIDE
        if (user.getType().equals(FirebaseStrings.guideStr())) {
            Guide guide = (Guide) user;
            // TODO: 5/24/2021 make sure no volunteer to this guide before delete
            if (!guide.getMyVolunteers().isEmpty()) {
                Log.d("delete guide", "deleteUser: Volunteers list is not empty! ");
                return;
            }
        }
        //delete volunteer
        else {
            Volunteer volunteer = (Volunteer) user;
            String myGuide = volunteer.getMyGuide();

            Query query = reference.whereEqualTo("type", FirebaseStrings.guideStr())
                    .whereEqualTo("name", myGuide);

           String id = reference.document(query.toString()).get().getResult().getId();
            Map <String, String> toDelete = new HashMap<>();
            toDelete.put("id", fbUser.getUid());


            if (fbUser != null){
                fbUser.delete()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                                Log.d("deletFBuser", "deleteUser: fbUser was delete successfully");
                        });

            }
        }
    }


}
