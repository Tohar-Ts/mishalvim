package com.example.mishlavim.model.Firebase;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mishlavim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.Function;
/**
 * Authentication Methods class manages connecting to firebase auth and provides functions for:
 * login, logout, check i f user is already logged in, and add a new user.
 *
 * How to use:
 * First, declare the on success and on failed function in your class if needed.
 * Then, call the wanted method.
 *  For example:
 * AuthenticationMethods.signIn(email, password, this::onSuccess, this::onFailure);
 * Notice:
 * Void is different from void.
 * If the function needs to accept/return a void object, declare it this way:
 * private Void onUpdateFailure(Void unused){
 *     return null;
 * }
 */
public class AuthenticationMethods {

    //TODO - add comments

    public static boolean isSignIn(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public static void signIn(String email, String password, Function<String, Void> successFunc, Function<Void, Void> failedFunc) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        successFunc.apply(fbUser.getUid());
                        Log.d("Authentication Methods:", "Login ended successfully");
                    } else {
                        Log.d("Authentication Methods:", "Login failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void addNewUser(String email, String password, Function<String, Void> successFunc, Function<Void, Void> failedFunc){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String newUserUid = mAuth.getCurrentUser().getUid();
                        Log.d("Authentication Methods:", "createUserWithEmail:success");
                        successFunc.apply(newUserUid);
                    } else {
                        Log.d("Authentication Methods:", "createUserWithEmail:failure", task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static String getCurrentUserID(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getUid();
    }

    public static void updateAuthEmail(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Authentication Methods:", "User email address updated.");
                        }
                        else{
                            Log.d("Authentication Methods:", "User email updated is failed.");
                        }
                    }
                });
    }

    public static void updateAuthPassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Authentication Methods:", "User password updated.");
                        } else {
                            Log.d("Authentication Methods:", "User password updated is failed.");
                        }
                    }
                });

    }
    //TODO - delete user

}
