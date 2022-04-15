package ch.fhnw.palletpals.data.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    //TODO in system spec as String why?
    private int postalCode;
    private String country;
    @OneToOne
    private User user;
    @OneToOne
    private Warehouse warehouse;
}
