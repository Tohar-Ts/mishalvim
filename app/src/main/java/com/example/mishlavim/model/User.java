package com.example.mishlavim.model;

/**
 * User is the base class for all users of the app.
 * The class represent the general user data:
 * full name, type ("admin", "guide", "volunteer"), and email address.
 */
public class User {


    protected String name;
    protected String type;
    protected String email;

    public User() {
    }

    public User(String name, String type, String email) {
        this.name = name;
        this.type = type;
        this.email = email;
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