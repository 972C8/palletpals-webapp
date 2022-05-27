package ch.fhnw.palletpals.data.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Code by Daniel Locher
 * Either users or warehouses can have shipping addresses.
 * The address includes coordinates because they will be used to place API calls to calculate distances between addresses.
 */
@Entity
public class ShippingAddress {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String organisationName;
    private String street;
    private String premise;
    private String city;
    private String postalCode;
    private String country;
    private String lat;
    private String lon;
    @OneToOne
    private User user;
    @OneToOne
    private Warehouse warehouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
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

    public String coordinateRequest(){
        return postalCode + "%20" + city + "%20" + street + "%20";
    }

    public String distanceRequest(){ return lat +"%2C" + lon;}
}
