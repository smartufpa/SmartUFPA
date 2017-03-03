
package com.example.kaeuc.smartufpa.jsonParserTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shop")
    @Expose
    private String shop;
    @SerializedName("building")
    @Expose
    private String building;

    @SerializedName("short_name")
    @Expose
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
