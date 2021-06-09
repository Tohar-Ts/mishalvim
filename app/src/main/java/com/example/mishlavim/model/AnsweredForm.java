package com.example.mishlavim.model;

import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;

import java.util.HashMap;
import java.util.function.Function;

public class AnsweredForm {

    boolean finishedButCanEdit;      //if this flag is turned on- this is a finished form the the guide opened to edit after it was send
    boolean isOpenForm;              //if this flag is turned on- this is a the open form that will show up in the main circle.
    String templateName;
    String templateId;
    HashMap<String, String> answers; //<questionNumber, answer>

    public AnsweredForm(){

    }

    public AnsweredForm(boolean finishedButCanEdit, boolean isOpenForm, String templateName, String templateId, HashMap<String, String> answers) {
        this.finishedButCanEdit = finishedButCanEdit;
        this.isOpenForm = isOpenForm;
        this.templateName = templateName;
        this.templateId = templateId;
        this.answers = answers;
    }

    //setters and getters
    public boolean getFinishedButCanEdit() {
        return finishedButCanEdit;
    }

    public void setFinishedButCanEdit(boolean finishedButCanEdit) {
        this.finishedButCanEdit = finishedButCanEdit;
    }

    public boolean getIsOpenForm() {
        return isOpenForm;
    }

    public void setIsOpenForm(boolean openForm) {
        isOpenForm = openForm;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
