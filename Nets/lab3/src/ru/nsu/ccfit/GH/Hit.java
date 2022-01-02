package ru.nsu.ccfit.GH;

public class Hit {
    private Point point;
    private double[] extent;
    private String name;
    private String country;
    private String countrycode;
    private String city;
    private String state;
    private Integer postcode;
    private Integer osm_id;
    private String osm_type;
    private String osm_key;
    private String osm_value;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double[] getExtent() {
        return extent;
    }

    public void setExtent(double[] extent) {
        this.extent = extent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public Integer getOsm_id() {
        return osm_id;
    }

    public void setOsm_id(Integer osm_id) {
        this.osm_id = osm_id;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public void setOsm_type(String osm_type) {
        this.osm_type = osm_type;
    }

    public String getOsm_key() {
        return osm_key;
    }

    public void setOsm_key(String osm_key) {
        this.osm_key = osm_key;
    }

    public String getOsm_value() {
        return osm_value;
    }

    public void setOsm_value(String osm_value) {
        this.osm_value = osm_value;
    }

    public Hit() {

    }
}

