package com.example.kaeuc.smartufpa.classes;

/**
 * Created by kaeuc on 1/12/2017.
 */

public class Xerox extends Place {

    private double coloredPrice;
    private double bwPrice;
    private final String openingTime = "08:00";
    private final String closingTime = "21:00";


    public Xerox(long id, double latitude, double longitude, String name, double coloredPrice, double bwPrice) {
        super(id, latitude, longitude, name);
        this.coloredPrice = coloredPrice;
        this.bwPrice = bwPrice;
    }

    public double getColoredPrice() {
        return coloredPrice;
    }

    public double getBwPrice() {
        return bwPrice;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }


    public void setColoredPrice(double coloredPrice) {
        this.coloredPrice = coloredPrice;
    }

    public void setBwPrice(double bwPrice) {
        this.bwPrice = bwPrice;
    }
}
