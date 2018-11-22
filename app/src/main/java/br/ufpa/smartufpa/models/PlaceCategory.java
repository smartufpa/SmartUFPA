package br.ufpa.smartufpa.models;

import android.gesture.Gesture;
import android.os.Parcel;
import android.os.Parcelable;

import br.ufpa.smartufpa.R;

/**
 * Created by kaeuchoa on 13/12/2017.
 */

public class PlaceCategory implements Parcelable {

    private Categories category;
    private String name;
    private int iconID;


    public enum Categories{
        FOODPLACE, BUILDING, OTHER
    }

    public PlaceCategory(Categories category) {
        this.category = category;
        switch (category) {
            case FOODPLACE:
                this.iconID = R.drawable.ic_marker_restaurant;
                this.name = "Refeições";
                break;
            case BUILDING:
                this.iconID = R.drawable.ic_marker_auditorium;
                this.name = "Prédio";
                break;
            default:
                this.iconID =  R.drawable.ic_about;
                this.name = "Outro";
                break;
        }


    }

    public String getName() {
        return name;
    }

    public int getIconID() {
        return iconID;
    }

    public Categories getCategory() {
        return category;
    }

    protected PlaceCategory(Parcel in) {
//        category = in.readInt();
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
//        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
