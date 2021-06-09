package com.example.mishlavim.model;

/**
 * Global class represents global variables that can be used anywhere in the app.
 * Global consists of the currently logged in user data from the database.
 * The first thing that happen after the user logged in, is keeping his data in the global relevant field by his type.
 * Global has 3 user fields, only one will have data after login:
 * 'guideInstance' - if a guide logged in, this field will be init with a Guide object consists of all the guide data.
 * 'adminInstance' - if an admin logged in, this field will be init with a Admin object consists of all the admin data.
 * 'voluInstance' - if a volunteer logged in, this field will be init with a Volunteer object consists of all the volunteer data.
 * And lastly, the class keep the 'type' field that can be used to identify the type of the logged in user.
 * How to use:
 * 1. First, get the globalInstance:
 * Global globalInstance = Global.getGlobalInstance();
 * 2. Then, one can access all the users -
 * Guide guide = globalInstance.getGuide();
 * guide.getName();
 * and so on..
 */
public class Global {

    private static final Global globalInstance = new Global();

    private Guide guideInstance;
    private Admin adminInstance;
    private Volunteer voluInstance;
    private String type;
    private String uid;

    private Global() {
    }

    public static Global getGlobalInstance() {
        return globalInstance;
    }

    public Guide getGuideInstance() {
        return guideInstance;
    }

    public void setGuideInstance(Guide guideInstance) {
        this.guideInstance = guideInstance;
    }

    public Admin getAdminInstance() {
        return adminInstance;
    }

    public void setAdminInstance(Admin adminInstance) {
        this.adminInstance = adminInstance;
    }

    public Volunteer getVoluInstance() {
        return voluInstance;
    }

    public void setVoluInstance(Volunteer voluInstance) {
        this.voluInstance = voluInstance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}