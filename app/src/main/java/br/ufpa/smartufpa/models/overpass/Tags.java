
package br.ufpa.smartufpa.models.overpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("short_name")
    @Expose
    private String shortName;

    @SerializedName("loc_name")
    @Expose
    private String locName;

    @SerializedName("shop")
    @Expose
    private String shop;


    @SerializedName("amenity")
    @Expose
    /*
     Amenity reúne os tipos: food_court, restaurant, library, toilets, exhibition centre
     */
    private String amenity;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("website")
    @Expose
    private String website;


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
        return String.format("[name=%s,short_name=%s,shop=%s,amenity=%s,loc_name=%s,description=%s]",
                name,shortName,shop,amenity,locName,description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite(){ return this.website;  }
}
