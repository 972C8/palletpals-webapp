package ch.fhnw.palletpals.data.domain.order;

import ch.fhnw.palletpals.data.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Code by: Tibor Haller
 * <p>
 * Central class of the order functionality. A User can create a new Order, which holds all relevant data in the form
 * of a SNAPSHOT. For example: Future changes in Product data do not impact the snapshot of the order.
 * <p>
 * References OrderItem (ProductItem and ShippingItem) that hold relevant snapshots of the information used to calculate totalCosts.
 */
@Entity
public class UserOrder {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    //Referenced user is not returned in api requests
    @JsonIgnore
    private User user;

    private float totalCost;
    private String dateOrdered;
    private String dateDelivered;

    //One Order has many OrderItems
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    //TODO: private ServiceProvider serviceProvider

    //private AddressItem addressItem needed?
    class AddressItem {
        private String firstName;
        private String lastName;
        private String organisationName;

        //TODO: Add rest of address items
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
