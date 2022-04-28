package ch.fhnw.palletpals.data.domain.order;

import ch.fhnw.palletpals.data.domain.Product;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Code by: Tibor Haller
 * <p>
 * Serves as a snapshot of the Product when bought by the user.
 * If product data is changed later on, this does not affect the snapshot data taken when the product was bought.
 */
@Entity
@DiscriminatorValue("PRODUCTITEM")
public class ProductItem extends OrderItem {

    private float pricePerUnit;
    private int quantity;
    private float palletSpace;

    //Uni-directional relationship. This means that Product does not know about which ProductItems reference it.
    @ManyToOne
    private Product product;


    public ProductItem() {
        super();
    }

    public ProductItem(String name, Order order) {
        super(name, order);
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
}
