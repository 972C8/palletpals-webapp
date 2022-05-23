package ch.fhnw.palletpals.data.domain.order;

import javax.persistence.Embeddable;

/**
 * Code by: Tibor Haller
 * <p>
 * Embeddable class AddressItem, which serves as a snapshot of the user's address when the user creates a new order.
 * <p>
 * This embeddable class does not require a primary key as it is stored (embedded) as part of UserOrder.java
 * <p>
 * More information: https://www.objectdb.com/java/jpa/entity/types and https://www.callicoder.com/hibernate-spring-boot-jpa-embeddable-demo/
 */
@Embeddable
public class AddressItem {
    //TODO: Handle empty variables
    private String firstName;
    private String lastName;
    private String organisationName;
    private String street;
    private String premise;
    private String city;
    private String postalCode;
    private String country;

    public AddressItem() {

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
}
