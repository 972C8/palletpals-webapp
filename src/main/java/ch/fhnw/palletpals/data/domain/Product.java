package ch.fhnw.palletpals.data.domain;

import ch.fhnw.palletpals.data.domain.image.ProductImage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please provide a name for the product")
    private String name;
    @NotEmpty(message = "Please provide details for the product")
    private String details;
    @NotEmpty(message = "Please provide a description for the product.")
    private String description;

    //Float must be positive, so higher than 0 (zero is not allowed)
    @Positive(message = "Please provide a price for the product.")
    private float price;
    @Positive(message = "Please provide maxProducts for the product.")
    private float maxProducts;
    @Positive(message = "Please provide minPalletSpace for the product.")
    private float minPalletSpace;

    //One product holds many images
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImage> productImages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMaxProducts() {
        return maxProducts;
    }

    public void setMaxProducts(float maxProducts) {
        this.maxProducts = maxProducts;
    }

    public float getMinPalletSpace() {
        return minPalletSpace;
    }

    public void setMinPalletSpace(float minPalletSpace) {
        this.minPalletSpace = minPalletSpace;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
}
