
package br.ufpa.smartufpa.models.overpass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverpassJsonModel {

    @SerializedName("version")
    @Expose
    private Double version;
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
