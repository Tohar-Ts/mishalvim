package com.myapplications.mishlavim.login;

import android.content.res.Resources;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.myapplications.mishlavim.R;

public class Validation {
    private final EditText emailEditText;
    private final EditText userNameEditText;
    private final EditText passwordEditText;
    private final EditText verifyPasswordEditText;
    private final ProgressBar loadingProgressBar;
    private final Resources r;


    public Validation(EditText emailEditText, EditText userNameEditText, EditText passwordEditText, EditText verifyPasswordEditText, ProgressBar loadingProgressBar, Resources r) {
        this.emailEditText = emailEditText;
        this.userNameEditText = userNameEditText;
        this.passwordEditText = passwordEditText;
        this.verifyPasswordEditText = verifyPasswordEditText;
        this.loadingProgressBar = loadingProgressBar;
        this.r = r;
    }

    public boolean validateInput() {

        String password = passwordEditText.getText().toString().trim();
       if(!isEmailValid()){
           return true;
       }
        if (userNameEditText != null) {
            if (!isUserNameValid()) {
                return true;
            }
        }

        if (!isPasswordValid()) {
            return true;
        }
        if (verifyPasswordEditText != null) {

            String verifyPassword = verifyPasswordEditText.getText().toString().trim();
            if(verifyPassword.isEmpty()){
                setError(verifyPasswordEditText, R.string.empty_verifyPassword);
                return true;
            }

            if (!isVerifyPasswordValid(password, verifyPassword)) {
                return true;
            }
        }
        return false;
    }


    private boolean isEmailValid() {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            setError(emailEditText,R.string.empty_email);
            return false;
        }
        else if (email.contains("@")) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                setError(emailEditText, R.string.invalid_email);
                return false;
            }
            else{
                return true;
            }
        }
        else{
            setError(emailEditText,R.string.invalid_email);
            return false;
        }

    }


    private boolean isPasswordValid() {
        String password = passwordEditText.getText().toString().trim();

        if(password.isEmpty()){
                setError(passwordEditText, R.string.empty_password);
                return false;
            }
        int passwordLength = 5;
       if(password != null && password.trim().length() > passwordLength){
           return true;
       }
       else{
           setError(passwordEditText, R.string.invalid_password);
           return false;
       }


    }
    private boolean isUserNameValid() {
        String fullName = userNameEditText.getText().toString();
        if (fullName.isEmpty()) {
            setError(userNameEditText, R.string.invalid_userName);
            return false;
        }
       else if (!fullName.contains(" ")){
            setError(userNameEditText, R.string.invalid_userName);
            return false;
        }
       else {
           return true;
        }

    }


    private boolean isVerifyPasswordValid(String password,String verifyPassword) {
        if(verifyPassword.isEmpty()){
            setError(verifyPasswordEditText, R.string.empty_verifyPassword);
            return false;
        }
        else if(!password.equals(verifyPassword)){
            setError(verifyPasswordEditText, R.string.invalid_verifyPasswordSame);
            return false;
        }
        else{
            return true;
        }
    }

    private void setError(EditText onEditText, Integer errorMsg) {
        onEditText.setError(r.getString(errorMsg));
        onEditText.requestFocus();
        loadingProgressBar.setVisibility(View.GONE);
    }
}
