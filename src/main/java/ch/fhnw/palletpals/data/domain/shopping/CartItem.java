package ch.fhnw.palletpals.data.domain.shopping;

import ch.fhnw.palletpals.data.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Positive;


/**
 * Code by: Tibor Haller
 * <p>
 * CartItem is used to display the shopping cart (with calculations).
 * <p>
 * One User has one ShoppingSession, which consists of one CartItem per product in the shopping cart.
 */
@Entity
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cartID", unique = true, nullable = false)
    private Long id;

    //Int must be positive, so higher than 0 (zero is not allowed)
    @Positive(message = "Please provide a price for the product.")
    private int quantity;

    //Value is set internally, rather than through the API
    private float pricePerUnit;

    /**
     * Code by: Tibor Haller
     * <p>
     * Bidirectional relation with ShoppingSession. Changes are propagated to ShoppingSession, meaning that deleting an instance of CartItem will remove the reference in ShoppingSession.
     */
    @ManyToOne
    @JoinColumn(name = "shoppingId")
    @JsonIgnore
    private ShoppingSession shoppingSession;

    //Uni-directional relationship. This means that Product does not know about which CartItems reference it.
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;

        //TODO: Ensure that it works when updating CartItem
        //When the referenced product changes, ensure that correct, new pricePerUnit is taken from referenced product
        setPricePerUnit(this.product.getPrice());
    }

    public ShoppingSession getShoppingSession() {
        return shoppingSession;
    }

    public void setShoppingSession(ShoppingSession shoppingSession) {
        this.shoppingSession = shoppingSession;
    }
}
