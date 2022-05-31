package ch.fhnw.palletpals.data.domain.order;

import ch.fhnw.palletpals.data.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Positive;

/**
 * Code by: Tibor Haller
 * <p>
 * Serves as a snapshot of the Product when bought by the user.
 * If product data is changed later on, this does not affect the snapshot data taken when the product was bought.
 */
@Entity
@DiscriminatorValue("PRODUCTITEM")
public class ProductItem extends OrderItem {

    //Float must be positive, so higher than 0 (zero is not allowed)
    @Positive(message = "Please provide a price")
    private float pricePerUnit;
    @Positive(message = "Please provide a quantity")
    private int quantity;
    @Positive(message = "Please provide minPalletSpace for the product.")
    private float palletSpace;

    /**
     * Code by: Tibor Haller
     * <p>
     * Bidirectional relation with UserOrder. Changes are propagated to UserOrder, meaning that deleting an instance of ProductItem will remove the reference in UserOrder
     */
    @ManyToOne
    @JoinColumn(name = "orderId")
    //Referenced user is not returned in api requests
    @JsonIgnore
    private UserOrder order;

    //Uni-directional relationship. This means that Product does not know about which ProductItems reference it.
    @ManyToOne
    @JsonIgnore
    private Product product;

    public ProductItem() {
        super();
    }

    public ProductItem(UserOrder order) {
        super();
        this.order = order;
    }

    public ProductItem(String name, UserOrder order) {
        super(name);
        this.order = order;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPalletSpace() {
        return palletSpace;
    }

    public void setPalletSpace(float palletSpace) {
        this.palletSpace = palletSpace;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserOrder getOrder() {
        return order;
    }

    public void setOrder(UserOrder order) {
        this.order = order;
    }
}
