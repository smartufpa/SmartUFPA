package com.example.kaeuc.osmapp;

/**
 * Created by kaeuc on 9/29/2016.
 */

public class Local {
    private double latitude;
    private double longitude;
    private String nome;

    public Local(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Local(double latitude, double longitude, String nome) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public String getNome() {
        return nome;
    }


}
