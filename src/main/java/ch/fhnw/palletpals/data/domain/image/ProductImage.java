package ch.fhnw.palletpals.data.domain.image;

import ch.fhnw.palletpals.data.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@DiscriminatorValue("PRODUCTIMAGE")
public class ProductImage extends AbstractImage {

    public ProductImage() {
        super();
    }

    public ProductImage(String fileName, String fileUrl, String fileType) {
        super(fileName, fileUrl, fileType);
    }

    //One product holds many images
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    //TODO: Check that: Product removes reference of deleted ProductImage
    // + All referenced ProductImages are deleted when a Product is deleted

    /**
     * Handle referential integrity constraint for 1:n relationship between Product and ProductImage
     * <p>
     * If a ProductImage is removed, the references to this ProductImage must be removed from all Products.
     * This is not required in Product as it is the owner of the relationship (as indicated by "mappedBy" in this class
     * for List<ProductImage> productImages.
     */
    @PreRemove
    private void removeImageFromProducts() {
        this.product.getProductImages().remove(this);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}