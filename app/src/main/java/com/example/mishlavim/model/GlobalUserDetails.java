package com.example.mishlavim.model;

public class GlobalUserDetails {

    private static final GlobalUserDetails globalInstance = new GlobalUserDetails();
    private Guide guideInstance;
    private Admin adminInstance;
    private Volunteer voluInstance;

    private GlobalUserDetails() {
    }

    public static GlobalUserDetails getGlobalInstance() {
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
}