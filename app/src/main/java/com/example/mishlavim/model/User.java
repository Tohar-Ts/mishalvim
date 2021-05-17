package com.example.mishlavim.model;

/**
 * User is the base class for all users of the app.
 * The class handles the general user data:
 * full name, firebase ID, type (Admin, Guide, Volunteer), email address and password.
 */
public class User {

    protected String name;
    protected String type;
    protected String email;

    public User() {
    }

    public User(String userName, String userType, String userEmail) {
        this.name = userName;
        this.type = userType;
        this.email = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}