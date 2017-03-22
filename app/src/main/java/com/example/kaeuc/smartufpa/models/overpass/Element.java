
package com.example.kaeuc.smartufpa.models.overpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("tags")
    @Expose
    private Tags tags;
    @SerializedName("center")
    @Expose
    private Center center;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() { return lat; }

    public Double getLon() {
        return lon;
    }

    public Tags getTags() {
        return tags;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public boolean isCenterEmpty() { return this.center == null; }


}
