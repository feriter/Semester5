package ru.nsu.ccfit.GH;

public class Point {
    private double lat;
    private double lng;

    public Point() {

    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }
}
