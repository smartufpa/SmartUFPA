package br.ufpa.smartufpa.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import br.ufpa.smartufpa.R;

/**
 * Created by kaeuchoa on 13/12/2017.
 */

public class PlaceCategory implements Parcelable{

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

    public int getId() {
        return id;
    }

    protected PlaceCategory(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<PlaceCategory> CREATOR = new Creator<PlaceCategory>() {
        @Override
        public PlaceCategory createFromParcel(Parcel in) {
            return new PlaceCategory(in);
        }

        @Override
        public PlaceCategory[] newArray(int size) {
            return new PlaceCategory[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
