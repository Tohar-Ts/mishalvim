package com.example.mishlavim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FormTemplate class represents only the questions of a form.
 * Each Question consists of prompt, type, and in case of a closed question - possible answers.
 * The volunteers individuals answers to this form will be kept in the answers map.
 * Each entry consists of volunteer Uid and his answeredForm Uid.
 */
public class FormTemplate {

    private HashMap<String, String> questionArr; //<question, index>
    private String formName;
    private HashMap<String, String> answers; // <answeresUid, VoluName>

    /**
     * Default constructor
     */
    public FormTemplate() {
    }

    /**
     * Parameterized constructor
     */
    public FormTemplate(HashMap<String, String> questionArr, String formName, HashMap<String, String> answers) {
        this.questionArr = questionArr;
        this.formName = formName;
        this.answers = answers;
    }

    public HashMap<String, String> getQuestionArr() {
        return questionArr;
    }

    public void setQuestionArr(HashMap<String, String> questionArr) {
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
