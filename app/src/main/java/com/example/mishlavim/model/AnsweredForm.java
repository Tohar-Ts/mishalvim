package com.example.mishlavim.model;

import java.util.Map;

public class AnsweredForm {
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
