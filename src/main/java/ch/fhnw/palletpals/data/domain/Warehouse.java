package ch.fhnw.palletpals.data.domain;

import javax.persistence.*;

@Entity
public class Warehouse {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private ShippingAddress address;
}
