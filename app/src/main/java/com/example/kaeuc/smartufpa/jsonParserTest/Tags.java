
package com.example.kaeuc.smartufpa.jsonParserTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("short_name")
    @Expose
    private String shortName;

    @SerializedName("shop")
    @Expose
    private String shop;


    @SerializedName("amenity")
    @Expose
    /*
     Amenity reúne os tipos: food_court, restaurant, library, toilets, exhibition centre
     */
    private String amenity;

    @SerializedName("loc_name")
    @Expose
    private String locName;

    @SerializedName("description")
    @Expose
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    @Override
    public String toString() {
        return "name: " + this.name + " short_name: " + this.shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
