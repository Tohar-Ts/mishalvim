package com.example.mishlavim;

import android.content.res.Resources;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Validation {
    private final EditText emailEditText;
    private final EditText passwordEditText;
    private final EditText userNameEditText;
    private final ProgressBar loadingProgressBar;
    private final Resources r;

    public Validation(EditText emailEditText, EditText passwordEditText, EditText userNameEditText, ProgressBar loadingProgressBar, Resources r) {
        this.emailEditText = emailEditText;
        this.passwordEditText = passwordEditText;
        this.userNameEditText = userNameEditText;
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
        if (!isPasswordValid(password)) {
            setError(passwordEditText, R.string.invalid_password);
            return true;
        }
        if (userNameEditText != null) {
            String userName = userNameEditText.getText().toString();
            if (!isUserNameValid(userName)) {
                setError(userNameEditText, R.string.invalid_userName);
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
        return password != null && password.trim().length() > passwordLength;
    }

    private boolean isUserNameValid(String userName) {
        if (userName == null) {
            return false;
        }
        return userName.contains(" ");
    }

    private void setError(EditText onEditText, Integer errorMsg) {
        onEditText.setError(r.getString(errorMsg));
        onEditText.requestFocus();
        loadingProgressBar.setVisibility(View.GONE);
    }
}
