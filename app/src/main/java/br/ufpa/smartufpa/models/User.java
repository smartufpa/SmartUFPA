package br.ufpa.smartufpa.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("GUIID")
    private String GUIID;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.GUIID = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGUIID() {
        return GUIID;
    }

    public void setGUIID(String GUIID) {
        this.GUIID = GUIID;
    }
}
