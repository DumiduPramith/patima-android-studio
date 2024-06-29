package com.onesandzeros.patima.feedback.model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private final int rating;
    private final String question1;
    private final String question2;
    private final String question3;
    private final String feedback;
    private final int image_id;

    private String input_image_path;
    private String predicted_image_path;


    public Feedback(int rating, String question1, String question2, String question3, String feedback, int pred_id) {
        this.rating = rating;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.feedback = feedback;
        this.image_id = pred_id;
    }

    public int getRating() {
        return rating;
    }

    public String getQuestion1() {
        return question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getPredId() {
        return image_id;
    }

    public String getInput_img() {
        return input_image_path;
    }

    public String getPredicted_img() {
        return predicted_image_path;
    }
}
