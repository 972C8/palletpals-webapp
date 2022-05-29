package ch.fhnw.palletpals.data.domain;

import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;

import javax.persistence.*;
import java.util.List;

/**
 * Code by Daniel Locher
 * A service provider delivers the order to the customer. In our scenario we expect multiple service providers with different price plans.
 * Service provider has an id, a name, and a price table
 * Because arrays can't be saved to relational databases (https://sebhastian.com/mysql-array/) the price plan table is saved as a json string
 * The idea behind this is to offer a method to upload a csv file of a new or updated price plan in the front
 * Out of this json string two arrays and one 2d array are built to get the price for a delivery
 * (more detailed explained in the class ServiceProviderService)
 */
@Entity
public class ServiceProvider {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String jSONString;

    @OneToMany
    @JoinColumn(name = "serviceProviderId", referencedColumnName = "id")
    private List<ShoppingSession> shoppingSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJSONString() {
        return jSONString;
    }

    public void setJSONString(String kmArray) {
        this.jSONString = kmArray;
    }



}
