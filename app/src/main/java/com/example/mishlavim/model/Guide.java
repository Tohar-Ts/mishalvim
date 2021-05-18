package com.example.mishlavim.model;

/**
 * Guide class represents an guide user.
 */
public class Guide {
    private String userName;
    private String newUserType;
    private String email;
    public Guide(String userName, String newUserType, String email) {
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


//    public Guide(String userName, String newUserType, String email) {
//    }
////
//    private ArrayList<Volunteer> myVolunteers;
//
//    /**
//     * Default constructor
//     */
//    public Guide() {
//    }
//
//    /**
//     * Parameterized constructor
//     */
//    public Guide(String name, String id, String type, String email, String password) {
//        super(name, id, type, email, password);
//    }
//
//    /**
//     * @param newVolunteer
//     * @return
//     */
//    public void addVolunteer(Volunteer newVolunteer) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public void editVolunteer(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public Volunteer findVolunteer(String name) {
//        // TODO implement here
//        return null;
//    }
//
//    /**
//     * @param name
//     * @return
//     */
//    public void deleteVolunteer(String name) {
//        // TODO implement here
//    }
//
//    /**
//     * @param volunteerName
//     * @param formName
//     * @return
//     */
//    public void addAccess(String volunteerName, String formName) {
//        // TODO implement here
//    }

}