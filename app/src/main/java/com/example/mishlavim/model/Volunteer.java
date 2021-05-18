package com.example.mishlavim.model;

/**
 *
 */
public class Volunteer {
    private String userName;
    private String newUserType;
    private String email;
    private String myGuide;

    public Volunteer(String userName, String newUserType, String email, String myGuide) {
//       super(userName,newUserType,email);
        this.userName = userName;
        this.newUserType = newUserType;
        this.email = email;
        this.myGuide = myGuide;
    }
    public String getUserName(){return userName;}
    public String getNewUserType(){return newUserType;}
    public String getEmail(){return email;}
public String getMyGuide(){return myGuide;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewUserType(String newUserType) {
        this.newUserType = newUserType;
    }
    public void setEmail(String email) {
        this.email = email;
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