package ru.nsu.ccfit.OTM;

public class OTMResponse {
    private String type;
    private Feature[] features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Feature[] getFeatures() {
        return features;
    }

    public void setFeatures(Feature[] features) {
        this.features = features;
    }

    public OTMResponse() {

    }
}
