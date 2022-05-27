package ch.fhnw.palletpals.data.domain;

import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;

import javax.persistence.*;
import java.util.List;

/**
 * Code by Daniel Locher
 */
@Entity
public class Warehouse {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private ShippingAddress address;

    @OneToMany
    @JoinColumn(name = "warehouseId", referencedColumnName = "id")
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

    public ShippingAddress getAddress() {
        return address;
    }

    public void setAddress(ShippingAddress address) {
        this.address = address;
    }
}
