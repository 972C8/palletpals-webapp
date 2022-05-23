package ch.fhnw.palletpals.data.domain.shopping;

import ch.fhnw.palletpals.business.service.shoppingServices.ShippingCostService;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.domain.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

/**
 * Code by: Tibor Haller
 * <p>
 * One user has one ShoppingSession to display shopping cart (with calculations).
 */
@Entity
public class ShoppingSession {

    @Id
    @GeneratedValue
    @Column(name = "shoppingId", unique = true, nullable = false)
    private Long id;

    //TODO: Should be updated automatically based on new shoppingCart
    private float shippingCost;
    private float totalCost;
    private float drivingDistance;
    private int palletSpace;

    @Column(name = "warehouseId")
    private Long nearestWarehouse;

    @Column(name = "serviceProviderId")
    private Long serviceProvider;

    //TODO: Timestamps not used yet
    private String createdAt;
    private String modifiedAt;

    @OneToOne
    @JsonIgnore
    private User user;

    /**
     * Code by: Tibor Haller
     *
     * Bi-directional OneToMany relationship. CascadeType and orphanRemoval is required to propagate changes in the parent to children.
     *
     * Removal of ShoppingSession is propagated to CartItem, meaning that the child objects are removed as well.
     *
     * According to https://stackoverflow.com/questions/49592081/jpa-detached-entity-passed-to-persist-nested-exception-is-org-hibernate-persis,
     * CascadeType.PERSIST poses problems as it tries to persist an already existing child when adding the reference in the list
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "shoppingId", referencedColumnName = "shoppingId")
    //Fixes Hibernate issue to disallow multiple bag fetches https://hibernate.atlassian.net/browse/HHH-1718, https://stackoverflow.com/a/8309458
    @Fetch(value = FetchMode.SUBSELECT)
    private List<CartItem> shoppingCart;

    public ShoppingSession(User user) {
        this.user = user;
    }

    public ShoppingSession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<CartItem> shoppingCart) throws Exception{this.shoppingCart = shoppingCart;}

    public float getDrivingDistance() {
        return drivingDistance;
    }

    public void setDrivingDistance(float drivingDistance) {
        this.drivingDistance = drivingDistance;
    }

    public int getPalletSpace() {
        return palletSpace;
    }

    public void setPalletSpace(int palletSpace) {
        this.palletSpace = palletSpace;
    }

    public Long getNearestWarehouse() {
        return nearestWarehouse;
    }

    public void setNearestWarehouse(Long nearestWarehouse) {
        this.nearestWarehouse = nearestWarehouse;
    }

    public Long getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(Long serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

}

