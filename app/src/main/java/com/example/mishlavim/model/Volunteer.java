package com.example.mishlavim.model;

/**
 *
 */
public class Volunteer extends User {
    private String myGuide;

    public Volunteer() {
    }

    public Volunteer(String userName, String userType, String userEmail, String myGuide) {
        super(userName, userType, userEmail);
        this.myGuide = myGuide;
    }

    public String getMyGuide() {
        return myGuide;
    }

    public void setMyGuide(String myGuide) {
        this.myGuide = myGuide;
    }
    //
//    private Form activeForm;
//    private ArrayList<Form> pastForms;
//
//    /**
//     * Default constructor
//     */
//    public Volunteer() {
//    }
//
//    /**
//     * Parameterized constructor
//     */
//    public Volunteer(String name, String id, String type, String email, String password) {
//        super(name, id, type, email, password);
//    }
//
//    /**
//     *
//     */
//    public Form getActiveForm() {
//        return activeForm;
//    }
//
//    /**
//     *
//     */
//    public void setActiveForm(Form activeForm) {
//        this.activeForm = activeForm;
//    }
//
//    /**
//     * @param name
//     */
//    public void fillOutActiveForm(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public Form findPastForm(String name) {
//        // TODO implement here
//        return null;
//    }
//
//    /**
//     * @param finishedForm
//     * @return
//     */
//    public void addPastForm(Form finishedForm) {
//        // TODO implement here
//    }

}