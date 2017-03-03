
package com.example.kaeuc.smartufpa.jsonParserTest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Osm3s {

    @SerializedName("timestamp_osm_base")
    @Expose
    private String timestampOsmBase;
    @SerializedName("timestamp_areas_base")
    @Expose
    private String timestampAreasBase;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    public String getTimestampOsmBase() {
        return timestampOsmBase;
    }

    public void setTimestampOsmBase(String timestampOsmBase) {
        this.timestampOsmBase = timestampOsmBase;
    }

    public String getTimestampAreasBase() {
        return timestampAreasBase;
    }

    public void setTimestampAreasBase(String timestampAreasBase) {
        this.timestampAreasBase = timestampAreasBase;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}
