package ch.fhnw.palletpals.data.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Code by: Tibor Haller
 */
@Entity
@DiscriminatorValue("SHIPPINGITEM")
public class ShippingItem extends OrderItem {

    //TODO: ShippingCost should be positive
    //@Positive(message = "Please provide shipping costs")
    private float shippingCost;

    @OneToOne(mappedBy = "shippingItem")
    //Referenced user is not returned in api requests
    @JsonIgnore
    private UserOrder order;

    public ShippingItem() {
        super();
    }

    public ShippingItem(UserOrder order) {
        super();
        this.order = order;
    }

    public ShippingItem(String name, UserOrder order) {
        super(name);
        this.order = order;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public UserOrder getOrder() {
        return order;
    }

    public void setOrder(UserOrder order) {
        this.order = order;
    }
}
