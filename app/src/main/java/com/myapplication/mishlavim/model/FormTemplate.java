package com.myapplication.mishlavim.model;

import java.util.HashMap;

/**
 * FormTemplate class represents only the questions of a form.
 * Each Question consists of prompt, type, and in case of a closed question - possible answers.
 * The volunteers individuals answers to this form will be kept in the answers map.
 * Each entry consists of volunteer Uid and his answeredForm Uid.
 */
public class FormTemplate {

    private HashMap<String, String> questionsMap; //<question, index>
    private String formName;

    /**
     * Default constructor
     */
    public FormTemplate() {
    }

    /**
     * Parameterized constructor
     */
    public FormTemplate(HashMap<String, String> questionArr, String formName) {
        this.questionsMap = questionArr;
        this.formName = formName;
    }

    public HashMap<String, String> getQuestionsMap() {
        return questionsMap;
    }

    public void setQuestionsMap(HashMap<String, String> questionsMap) {
        this.questionsMap = questionsMap;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }


}
