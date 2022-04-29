package ch.fhnw.palletpals.data.domain;

public class Coordinate {
    private String lat;
    private String lon;
    private Long addressReference;
    private CoordinateType coordinateType;
    private double distanceToDestinationInKM;

    public Coordinate(String lat, String lon, Long addressReference) {
        this.lat = lat;
        this.lon = lon;
        this.addressReference = addressReference;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Long getAddressReference() {
        return addressReference;
    }

    public void setAddressReference(Long addressReference) {
        this.addressReference = addressReference;
    }

    public CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(CoordinateType coordinateType) {
        this.coordinateType = coordinateType;
    }

    public double getDistanceToDestinationInKM() {
        return distanceToDestinationInKM;
    }

    public void setDistanceToDestinationInKM(double distanceToDestinationInKM) {
        this.distanceToDestinationInKM = distanceToDestinationInKM;
    }

    public String getPathString() {
        String path = lat + "%2C" + lon;
        return path;
    }
}
