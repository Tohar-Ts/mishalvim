package com.example.mishlavim.volunteerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;
import com.example.mishlavim.model.FirebaseStrings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class VolunteerMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStrings firebaseDefinitions;
    private String UserName;
    TextView welcomeText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseDefinitions = new FirebaseStrings();
        welcome_userName();

    }
    public void welcome_userName(){
        FirebaseUser user = mAuth.getCurrentUser();
        String usersCollection = firebaseDefinitions.usersStr();
        String userId = user.getUid();
        welcomeText = (TextView) findViewById(R.id.WelcomeUser);
        db.collection(usersCollection)
                .document(userId)
                .get()
                .addOnCompleteListener(VolunteerMainActivity.this, doc -> {
                    if (doc.isSuccessful())
                        welcomeText.setText(" ברוך הבא " + doc.getResult().getData().get("name").toString() +"!"+" ");
                    else
                        welcomeText.setText("ברוך הבא!\t" );
                });
        welcomeText.setTextSize(30);
    }


    public void fillOutFormActivity(View v) {
        Intent i = new Intent(this, fillOutFormActivity.class);
        startActivity(i);
    }
    public void viewOldFormActivity(View v){
        Intent i = new Intent(this, viewOldFormActivity.class);
        startActivity(i);
    }

}