package com.example.mishlavim.model;

import java.util.ArrayList;

/**
 * Guide class represents an guide user.
 */
public class Guide extends User {

    private ArrayList<Volunteer> myVolunteers;

    /**
     * Default constructor
     */
    public Guide() {
    }

    /**
     * Parameterized constructor
     */
    public Guide(String name, String id, String type, String email, String password) {
        super(name, id, type, email, password);
    }

    /**
     * @param newVolunteer
     * @return
     */
    public void addVolunteer(Volunteer newVolunteer) {
        // TODO implement here
    }

    /**
     * @param name
     * @return
     */
    public void editVolunteer(String name) {
        // TODO implement here
    }

    /**
     * @param name
     * @return
     */
    public Volunteer findVolunteer(String name) {
        // TODO implement here
        return null;
    }

    /**
     * @param name
     * @return
     */
    public void deleteVolunteer(String name) {
        // TODO implement here
    }

    /**
     * @param volunteerName
     * @param formName
     * @return
     */
    public void addAccess(String volunteerName, String formName) {
        // TODO implement here
    }

}