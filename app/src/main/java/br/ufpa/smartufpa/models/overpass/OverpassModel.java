
package br.ufpa.smartufpa.models.overpass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverpassModel {

    @SerializedName("elements")
    @Expose
    private List<Element> elements;

    public List<Element> getElements() {
        return elements;
    }

    public boolean isEmpty(){ return getElements().isEmpty();}



}
