package br.ufpa.smartufpa.models.smartufpa;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

import java.util.Locale;

import br.ufpa.smartufpa.models.smartufpa.interfaces.PlaceRating;
import br.ufpa.smartufpa.utils.Constants;

/**
 * Stable Commit (20/09)
 * @author kaeuchoa
 */

public class Place_deprecated implements Parcelable,PlaceRating {

    private String description;
    private long id;
    private GeoPoint latLong;
    private String locName;
    private String name;
    private String shortName;

    public Place_deprecated(Long id, double latitude, double longitude, String name, String shortName,
                 String locName, String description) {
        this.id = id;
        this.latLong = new GeoPoint(latitude,longitude);
        this.shortName = shortName;
        this.locName = locName;
        this.description = description;
        this.name = name;
    }
    public Place_deprecated(Long id, double latitude, double longitude, String name) {
        this.id = id;
        this.latLong = new GeoPoint(latitude,longitude);
        this.name = name;
        this.shortName = Constants.NO_SHORT_NAME;
        this.locName = Constants.NO_LOCAL_NAME;
        this.description = Constants.NO_DESCRIPTION;
    }

    protected Place_deprecated(Parcel in) {
        description = in.readString();
        id = in.readLong();
        locName = in.readString();
        name = in.readString();
        shortName = in.readString();
    }

    public double getLatitude() {
        return this.latLong.getLatitude();
    }


    public double getLongitude() {
        return this.latLong.getLongitude();
    }

    public String getLocName() {
        return locName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GeoPoint getGeoPoint(){return  this.latLong;}

    public void setLatitude(double latitude) { this.latLong.setLatitude(latitude);  }

    public void setLongitude(double longitude) { this.latLong.setLongitude(longitude);}

    @Override
    public String toString() {
        return String.format(Locale.US,"[id=%s,lat=%f,lon=%f,name=%s,short_name=%s,loc_name=%s,description=%s]",
                id,latLong.getLatitude(),latLong.getLongitude(),name,shortName,locName,description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeLong(id);
        dest.writeString(locName);
        dest.writeString(name);
        dest.writeString(shortName);
    }
    public static final Creator<POI> CREATOR = new Creator<POI>() {
        @Override
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        @Override
        public POI[] newArray(int size) {
            return new POI[size];
        }
    };

    @Override
    public void rate() {

    }
}
