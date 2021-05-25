package com.example.mishlavim.model;

import java.util.HashMap;

/**
 * Volunteer class represents the volunteer user data.
 * Every volunteer extends three fields from User class: name, type, email.
 * Volunteer also have his own field:
 * 'myGuide' - his guide name.
 * 'answeredForms' - an map consists of <Uid, name> for all the forms the volunteer answered.
 * 'openForms' - an map consists of <Uid, name> for all the open forms the volunteer can still edit and answer.
 * Volunteer class provide functions to handle the volunteer data - inserting/deleting/searching a form
 * from 'answeredForms' and 'openForms' maps.
 */
public class Volunteer extends User {
    private String myGuide;
    private HashMap<String, AnsweredForm> finishedForms;
    private HashMap<String, AnsweredForm> openForms;

    /**
     * Empty constructor.
     */
    public Volunteer() {
    }

    /**
     * Parameterized constructor.
     */
    public Volunteer(String name, String type, String email, String myGuide, HashMap<String, AnsweredForm> answeredForms, HashMap<String, AnsweredForm> openForms) {
        super(name, type, email);
        this.myGuide = myGuide;
        this.finishedForms = answeredForms;
        this.openForms = openForms;
    }

    /**
     * Adding a open form to the 'openForms' map of a volunteer.
     * Updating the change in the database.
     *
     * @param voluId - the volunteer Uid
     * @param formId - the form Uid
     * @param form   - AnsweredForm object containing the form data
     */
    public static void addOpenForm(String voluId, String formId, AnsweredForm form) {
        /* TODO implement here */
    }

    /**
     * For the given volunteer, finds the open form id from 'openForms' map given his name,
     * Then returns a Form object with the finished form data.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     * @return AnsweredForm object with the form data from the map
     */
    public static AnsweredForm findOpenForm(String voluId, String formName) {
        /* TODO implement here */
        return null;
    }

    /**
     * For the given volunteer, deletes a open form from 'openForms' map.
     * Updating the change in the database.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     */
    public static void deleteOpenForm(String voluId, String formName) {
        // TODO implement here
    }

    /**
     * For the given volunteer, adding a open form to the 'finishedForms' map.
     * Updating the change in the database.
     *
     * @param voluId - the volunteer Uid
     * @param formId - the form Uid
     * @param form   - AnsweredForm object containing the form data
     */
    public static void addFinishedForm(String voluId, String formId, AnsweredForm form) {
        /* TODO implement here */
    }

    /**
     * For the given volunteer, finds the finished form id from 'finishedForms' map given his name,
     * Then returns a Form object with the finished form data.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     * @return AnsweredForm object with the form data from the map
     */
    public static AnsweredForm findFinishedForm(String voluId, String formName) {
        /* TODO implement here */
        return null;
    }

    /**
     * For the given volunteer, deletes a finished form from 'finishedForms' map.
     * Updating the change in the database.
     *
     * @param voluId   - the volunteer Uid
     * @param formName - form name to find
     */
    public static void deleteFinishedForm(String voluId, String formName) {
        /* TODO implement here */
    }

    public String getMyGuide() {
        return myGuide;
    }

    public void setMyGuide(String myGuide) {
        this.myGuide = myGuide;
    }

    public HashMap<String, AnsweredForm> getFinishedForms() {
        return finishedForms;
    }

    public void setFinishedForms(HashMap<String, AnsweredForm> finishedForms) {
        this.finishedForms = finishedForms;
    }

    public HashMap<String, AnsweredForm> getOpenForms() {
        return openForms;
    }

    public void setOpenForms(HashMap<String, AnsweredForm> openForms) {
        this.openForms = openForms;
    }
}