package ch.fhnw.palletpals.data.domain.order;

import ch.fhnw.palletpals.data.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @Column(name = "orderId", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    //Referenced user is not returned in api requests
    @JsonIgnore
    private User user;

    private float totalCost;
    private String dateOrdered;
    private String dateDelivered;

    /**
     * Code by: Tibor Haller
     * <p>
     * One order holds many productItems
     * <p>
     * Bidirectional oneToMany connection. CascadeType and orphanRemoval is required to propagate changes in the parent to children.
     * <p>
     * According to https://stackoverflow.com/a/40339633, changes in children (ProductImage) can be propagated to the parent (Product) by using the @JoinColumn
     * <p>
     * According to https://stackoverflow.com/questions/49592081/jpa-detached-entity-passed-to-persist-nested-exception-is-org-hibernate-persis,
     * CascadeType.PERSIST poses problems as it tries to persist an already existing child when adding the reference in the list
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "productItems_id", referencedColumnName = "orderId")
    //Fixes Hibernate issue to disallow multiple bag fetches https://hibernate.atlassian.net/browse/HHH-1718, https://stackoverflow.com/a/8309458
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ProductItem> productItems;

    /**
     * Code by: Tibor Haller
     * <p>
     * One Order has one ShippingItem
     * <p>
     * Bidirectional oneToMany connection. CascadeType and orphanRemoval is required to propagate changes in the parent to children.
     * <p>
     */
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    private ShippingItem shippingItem;

    /**
     * Code by: Tibor Haller
     * <p>
     * The embeddable inner class AddressItem is embedded into the UserOrder entity and stored in the DB as part of it.
     * <p>
     * AddressItem serves as a snapshot of the user address when creating a new order.
     */
    @Embedded
    private AddressItem addressItem;

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

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public ShippingItem getShippingItem() {
        return shippingItem;
    }

    public void setShippingItem(ShippingItem shippingItem) {
        this.shippingItem = shippingItem;
    }

    public AddressItem getAddressItem() {
        return addressItem;
    }

    public void setAddressItem(AddressItem addressItem) {
        this.addressItem = addressItem;
    }
}
