package com.onesandzeros.patima.prediction.model;

import java.io.Serializable;

public class Image implements Serializable {

    private final int image_id;
    private final String input_image_path;
    private final String predicted_image_path;
    private final String created_at;

    private String tag_name;

    public Image(int image_id, String input_image_path, String predicted_image_path, String created_at) {
        this.image_id = image_id;
        this.input_image_path = input_image_path;
        this.predicted_image_path = predicted_image_path;
        this.created_at = created_at;
    }

    public int getImageId() {
        return image_id;
    }

    public String getInputImagePath() {
        return input_image_path;
    }

    public String getPredictedImagePath() {
        return predicted_image_path;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getTagName() {
        return tag_name;
    }
}
