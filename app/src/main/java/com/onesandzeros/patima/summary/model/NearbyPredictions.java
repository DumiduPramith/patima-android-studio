package com.onesandzeros.patima.summary.model;

import java.io.Serializable;

public class NearbyPredictions implements Serializable {

    private int image_id;
    private String input_image_path;
    private String predicted_image_path;
    private int uploader_id;
    private String created_at;

    public int getImage_id() {
        return image_id;
    }

    public String getInput_image_path() {
        return input_image_path;
    }

    public String getPredicted_image_path() {
        return predicted_image_path;
    }

    public int getUploader_id() {
        return uploader_id;
    }

    public String getCreated_at() {
        return created_at;
    }
}
