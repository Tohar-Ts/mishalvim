package com.example.mishlavim.model;

import java.util.HashMap;

public class AnsweredForm {
    /**
     *if this flag is turned on- this is a finished form the the guide opened to edit after it was send
     */
    boolean canEdit;
    /**
     * if this flag is turned on- this is a the open form that will show up in the main circle.
     * Need to be only one at a time!
     */
    boolean onWork;

    String templateId;
    HashMap<String, String> answers; //<questionNumber, answer>

    public AnsweredForm(){

    }

    public AnsweredForm(boolean canEdit, boolean onWork, String templateId, HashMap<String, String> answers) {
        this.canEdit = canEdit;
        this.onWork = onWork;
        this.templateId = templateId;
        this.answers = answers;
    }

    public boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean getOnWork() {
        return onWork;
    }

    public void setOnWork(boolean onWork) {
        this.onWork = onWork;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public HashMap<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, String> answers) {
        this.answers = answers;
    }
}
