package ch.fhnw.palletpals.data.domain.image;

import ch.fhnw.palletpals.data.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Code by: Tibor Haller
 */
@Entity
@DiscriminatorValue("PRODUCTIMAGE")
public class ProductImage extends AbstractImage {

    public ProductImage() {
        super();
    }

    public ProductImage(String fileName, String fileUrl, String fileType) {
        super(fileName, fileUrl, fileType);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Bidirectional relation with Product. Changes are propagated to Product, meaning that deleting an instance of ProductImage will remove the reference in Product
     */
    @ManyToOne
    @JoinColumn(name = "productID")
    @JsonIgnore
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
