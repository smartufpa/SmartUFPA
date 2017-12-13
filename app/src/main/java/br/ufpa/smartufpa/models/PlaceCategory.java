package br.ufpa.smartufpa.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import br.ufpa.smartufpa.R;

/**
 * Created by kaeuchoa on 13/12/2017.
 */

public class PlaceCategory {

    private int id;
    private String name;
    private Drawable icon;

    public PlaceCategory(int id, Context parentContext) {
        this.id = id;

        // Sets the image for the card
        // TODO: get names from string.xml
        switch (id){
            case 0:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_auditorium);
                this.name = "Departamento";
                break;
            case 1:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_restroom);
                this.name = "Banheiro";
                break;
            case 2:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_library);
                this.name = "Biblioteca";
                break;
            case 3:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_restaurant);
                this.name = "Alimentação";
                break;
            case 4:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_marker_xerox);
                this.name = "Xerox";
                break;
            default:
                this.icon = ContextCompat.getDrawable(parentContext, R.drawable.ic_about);
                this.name = "Outro";
                break;
        }


    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
}
