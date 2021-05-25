package com.example.mishlavim.model;

/**
 * Question class represents one question data - prompt and type.
 * It has two children - 'openQuestion' class and 'radioQuestion' class.
 */
public class Question {

    private String prompt;
    private int index;
    private String type;

    public Question() {
    }

    public Question(String prompt, String type) {
        this.prompt = prompt;
        this.type = type;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}