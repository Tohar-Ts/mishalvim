package com.example.mishlavim.model;

/**
 * Admin class represents an admin user.
 * There is only one admin in this app.
 * Admin have a list of all the guides, and he can manage all guides users.
 * Admin also have a list of all the forms templates and he is the one to manage them.
 */
public class Admin {
    private String userName;
    private String newUserType;
    private String email;
    public Admin(String userName, String newUserType, String email) {
        this.userName = userName;
        this.newUserType = newUserType;
        this.email = email;
    }
    public String getUserName(){return userName;}
    public String getNewUserType(){return newUserType;}
    public String getEmail(){return email;}
//    public String getMyGuide(){return myGuide;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewUserType(String newUserType) {
        this.newUserType = newUserType;
    }
    public void setEmail(String email) {
        this.email = email;
    }
//    private ArrayList<Guide> guideList;
//    private ArrayList<Form> formsTemplates;
//
//    /**
//     * Default constructor
//     */
//    public Admin() {
//        super();
//    }
//
//    /**
//     * Parameterized constructor
//     *
//     * @param name  The user full name - first and last.
//     * @param id    The user firebase id.
//     * @param type  The user type - admin, manager or volunteer.
//     * @param email The user email - used for login.
//     */
//    public Admin(String name, String id, String type, String email, String password) {
//        super(name, id, type, email, password);
//        guideList = new ArrayList<Guide>();
//        formsTemplates = new ArrayList<Form>();
//    }
//
//    /**
//     * @param newGuide
//     */
//    public void addGuide(Guide newGuide) {
//        guideList.add(newGuide);
//    }
//
//    /**
//     * @param name
//     */
//    public void editGuide(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public Guide findGuide(String name) {
//        // TODO implement here
//        return null;
//    }
//
//    /**
//     * @param name
//     */
//    public void deleteGuide(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param newForm
//     */
//    public void addForm(Form newForm) {
//        // TODO implement here
//    }
//
//    /**
//     * @param toEdit
//     */
//    public void editForm(Form toEdit) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public Form findForm(String name) {
//        // TODO implement here
//        return null;
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public void deleteForm(String name) {
//        // TODO implement here
//    }

}
