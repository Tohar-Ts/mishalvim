package com.example.mishlavim;

import android.content.res.Resources;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!isEmailValid(email)) {
            setError(emailEditText, R.string.invalid_email);
            return true;
        }
        if (userNameEditText != null) {
            String userName = userNameEditText.getText().toString();
            if (!isUserNameValid(userName)) {
                setError(userNameEditText, R.string.invalid_userName);
                return true;
            }
        }
        if (!isPasswordValid(password)) {
            setError(passwordEditText, R.string.invalid_password);
            return true;
        }
        if (verifyPasswordEditText != null) {
            String verifyPassword = verifyPasswordEditText.getText().toString().trim();
            if(verifyPassword.isEmpty()){
                setError(verifyPasswordEditText, R.string.invalid_verifyPassword);
                return true;
            }
            if (!isVerifyPasswordValid(password, verifyPassword)) {
                setError(verifyPasswordEditText, R.string.invalid_verifyPasswordSame);
                return true;
            }
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else
            return false;
    }

    private boolean isPasswordValid(String password) {
        int passwordLength = 5;
        return (password != null && password.trim().length() > passwordLength);


    }
    private boolean isUserNameValid(String userName) {
        if (userName == null) {
            return false;
        }
        return userName.contains(" ");
    }


    private boolean isVerifyPasswordValid(String password,String verifyPassword) {
        return (password.equals(verifyPassword));
    }

    private void setError(EditText onEditText, Integer errorMsg) {
        onEditText.setError(r.getString(errorMsg));
        onEditText.requestFocus();
        loadingProgressBar.setVisibility(View.GONE);
    }
}
