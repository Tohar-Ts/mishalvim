package com.example.mishlavim.model;

import java.util.Map;

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
    Map<String, String> answers; //<questionNumber, answer>

    public AnsweredForm(){

    }

    public AnsweredForm(String templateId, Map<String, String> answers) {
        this.templateId = templateId;
        this.answers = answers;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
