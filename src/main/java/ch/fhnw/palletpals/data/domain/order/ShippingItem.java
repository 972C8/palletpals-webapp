package ch.fhnw.palletpals.data.domain.order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Code by: Tibor Haller
 */
@Entity
@DiscriminatorValue("SHIPPINGITEM")
public class ShippingItem extends OrderItem {

    private float shippingCost;

    public ShippingItem() {
        super();
    }

    public ShippingItem(String name, UserOrder order) {
        super(name, order);
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }
}
