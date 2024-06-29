package com.onesandzeros.patima.user.model;

public class User {
    private final String email;
    private final int role;
    private int user_id;
    private String fname;
    private String lname;
    private String profile_picture;

    private boolean profile_picture_changed = false;
    private int is_admin;
    private String password;
    private String old_password;
    private int archeologist_id;

    public User(String fname, String lname, String email, String password, int role) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String fname, String lname, String email, String password, int role, int archeologist_id) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.archeologist_id = archeologist_id;
    }

    public User(int user_id, String fname, String lname, String email, int role, int archeologist_id) {
        this.user_id = user_id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.role = role;
        this.archeologist_id = archeologist_id;
    }

    public int getUserId() {
        return user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profile_picture;
    }

    public void setProfile_picture_changed(boolean profile_picture_changed) {
        this.profile_picture_changed = profile_picture_changed;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public int getIsAdmin() {
        return is_admin;
    }

    public int getArcheologistId() {
        return archeologist_id;
    }
}

