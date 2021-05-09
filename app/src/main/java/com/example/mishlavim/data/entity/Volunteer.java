package com.example.mishlavim.data.entity;

import java.util.ArrayList;

/**
 *
 */
public class Volunteer extends User {

    private Form activeForm;
    private ArrayList<Form> pastForms;

    /**
     * Default constructor
     */
    public Volunteer() {
    }

    /**
     * Parameterized constructor
     */
    public Volunteer(String name, String id, String type, String email, String password) {
        super(name, id, type, email, password);
    }

    /**
     *
     */
    public Form getActiveForm() {
        return activeForm;
    }

    /**
     *
     */
    public void setActiveForm(Form activeForm) {
        this.activeForm = activeForm;
    }

    /**
     * @param name
     */
    public void fillOutActiveForm(String name) {
        // TODO implement here
    }

    /**
     * @param name
     * @return
     */
    public Form findPastForm(String name) {
        // TODO implement here
        return null;
    }

    /**
     * @param finishedForm
     * @return
     */
    public void addPastForm(Form finishedForm) {
        // TODO implement here
    }

}