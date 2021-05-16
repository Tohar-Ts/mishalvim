package com.example.mishlavim.model;

/**
 *
 */
public class Question {

    private String prompt;
    private String answer;
    private int type;
    private String[] options;

    /**
     * Default constructor
     */
    public Question() {
    }

    /**
     *
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     *
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     *
     */
    public String getAnswer() {
        return answer;
    }

    /**
     *
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     *
     */
    public int getType() {
        return type;
    }

    /**
     *
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     *
     */
    public String[] getOptions() {
        return options;
    }

    /**
     *
     */
    public void setOptions(String[] options) {
        this.options = options;
    }
}