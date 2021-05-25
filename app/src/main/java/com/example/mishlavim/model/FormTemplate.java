package com.example.mishlavim.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FormTemplate class represents only the questions of a form.
 * Each Question consists of prompt, type, and in case of a closed question - possible answers.
 * The volunteers individuals answers to this form will be kept in the answers map.
 * Each entry consists of volunteer Uid and his answeredForm Uid.
 */
public class FormTemplate {

    private ArrayList<Question> questionArr;
    private String formName;
    private HashMap<String, String> answers;

    /**
     * Default constructor
     */
    public FormTemplate() {
    }

    /**
     * Parameterized constructor
     */
    public FormTemplate(ArrayList<Question> questionArr, String formName, HashMap<String, String> answers) {
        this.questionArr = questionArr;
        this.formName = formName;
        this.answers = answers;
    }

    public ArrayList<Question> getQuestionArr() {
        return questionArr;
    }

    public void setQuestionArr(ArrayList<Question> questionArr) {
        this.questionArr = questionArr;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public HashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers) {
        this.answers = answers;
    }
}
