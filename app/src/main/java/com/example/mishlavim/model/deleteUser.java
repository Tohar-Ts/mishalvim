package com.example.mishlavim.model;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.Validation;
import com.example.mishlavim.dialogs.*;
import com.example.mishlavim.model.GlobalUserDetails;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.UserTypes;
import com.example.mishlavim.model.Volunteer;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class deleteUser {


    public void deleteUser(FirebaseUser fbUser, FirebaseFirestore db, User user){

        CollectionReference reference = db.collection(UserTypes.getUSER_COLLECTION());

        // user is GUIDE
        if (user.getType().equals(UserTypes.getGUIDE())){
            Guide guide = (Guide) user;
            // TODO: 5/24/2021 make sure no volunteer to this guide before delete
            if(!guide.getMyVolunteers().isEmpty()){
                Log.d("delete guide", "deleteUser: Volunteers list is not empty! ");
                return;
            }
        }
        //delete volunteer
        else{
            Volunteer volunteer = (Volunteer) user;
            String myGuide = volunteer.getMyGuide();

            Query query = reference.whereEqualTo("type",UserTypes.getGUIDE())
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
