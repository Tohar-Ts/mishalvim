package com.myapplications.mishlavim.model.Firebase;

/**
 * FirebaseStrings class represents firebase collection names, user types, user fields..
 * Common strings that are used to get and identify data from the firebase.
 */
public class FirebaseStrings {

    public FirebaseStrings() {
    }

    //user types
    public static String adminStr() {
        return "admin";
    }

    public static String guideStr() {
        return "guide";
    }

    public static String volunteerStr() {
        return "volunteer";
    }

    //all users fields
    public static String emailStr() {
        return "email";
    }

    public static String nameStr() {
        return "name";
    }

    public static String typeStr() {
        return "type";
    }

    //volunteer fields
    public static String finishedFormsStr(){ return "finishedForms";}

    public static String finishedTemplatesStr(){ return "myFinishedTemplate";}
    public static String openFormUidStr() {
        return "openFormId";
    }

    public static String hasOpenFormStr() {
        return "hasOpenForm";
    }

    public static String myGuideIdStr() {
        return "myGuideId";
    }

    public static String openFormNameStr() {
        return "openFormName";
    }

    //guide fields
    public static String myVolunteerStr() {
        return "myVolunteers";
    }

    //admin fields
    public static String guideListStr() {
        return "guideList";
    }

    public static String myAdminIdStr() {
        return "myAdminId";
    }

    //form template fields
    public static String questionsMapStr(){return "questionsMap"; }

    public static String formNameStr(){return "formName"; }

    //answered form fields

    public static String finishedCanEditStr() {
        return "finishedButCanEdit";
    }

    public static String isOpenStr() {
        return "isOpenForm";
    }

    public static String templateNameStr() {
        return "templateName";
    }

    public static String templateIdStr() {
        return "templateId";
    }

    public static String answersStr() {
        return "answers";
    }

    //collections
    public static String usersStr() {
        return "users";
    }

    public static String formsTemplatesStr() {
        return "formsTemplates";
    }

    public static String answeredFormsStr() {
        return "answeredForms";
    }

    public static String isOpenFormStr() {
        return "isOpenForm";
    }

    public static String finishedButCanEditStr() {
        return "finishedButCanEdit";
    }

    //??
    public static String emptyString(){return  "";}

}
