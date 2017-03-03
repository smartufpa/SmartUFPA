
package com.example.kaeuc.smartufpa.jsonParserTest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teste {

    @SerializedName("version")
    @Expose
    private Double version;
    @SerializedName("generator")
    @Expose
    private String generator;
    @SerializedName("osm3s")
    @Expose
    private Osm3s osm3s;
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Osm3s getOsm3s() {
        return osm3s;
    }

    public void setOsm3s(Osm3s osm3s) {
        this.osm3s = osm3s;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
