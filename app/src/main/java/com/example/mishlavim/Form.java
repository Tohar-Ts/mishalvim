package com.example.mishlavim;

import java.util.*;

public class Form {

    private ArrayList<Question> questionArr;
    private String formName;
    private String formID;
    private int permission;

    // Default constructor
    public Form() {
    }

    //Constructor
    public Form(ArrayList<Question> questionArr, String formName, String formID, int permission) {
        this.questionArr = questionArr;
        this.formName = formName;
        this.formID = formID;
        this.permission = permission;
    }

    // Copy constructor
    public void Form(Form other) {
        // TODO implement here
    }

    /**
     *
     */
    public void saveForm() {
        // TODO implement here
    }

    /**
     * @return
     */
    public int addQuestion() {
        // TODO implement here
        return 0;
    }

    /**
     * @param index
     * @return
     */
    public void editQuestion(int index) {
        // TODO implement here
    }

    /**
     * @param index
     * @return
     */
    public void deleteQuestion(int index) {
        // TODO implement here
    }

    public void setQuestionArr(ArrayList<Question> questionArr) {
        this.questionArr = questionArr;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
