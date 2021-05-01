package com.example.mishlavim;

import java.util.*;

public class User {

    protected String userName;
    protected String userID;
    protected String userType;
    protected String userEmail;
    protected String userPassword;


    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * @param name  The user full name - first and last.
     * @param id    The user firebase id.
     * @param type  The user type - admin, manager or volunteer.
     * @param email The user email - used for login.
     */
    public User(String name, String id, String type, String email, String password) {
        userName = name;
        userID = id;
        userType = type;
        userEmail = email;
        userPassword = password;
    }

    public void setUserName(String newName) {
        userName = newName;
    }

    public void setUserPassword(String newPassword) {
        userPassword = newPassword;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}