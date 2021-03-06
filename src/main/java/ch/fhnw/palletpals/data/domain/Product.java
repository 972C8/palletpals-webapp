package ch.fhnw.palletpals.data.domain;

import ch.fhnw.palletpals.data.domain.image.ProductImage;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Code by: Tibor Haller
 */
@Entity
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "productId", unique = true, nullable = false)
    private Long id;

    @NotEmpty(message = "Please provide a name for the product")
    private String name;
    @NotEmpty(message = "Please provide details for the product")
    private String details;
    @NotEmpty(message = "Please provide a description for the product.")
    private String description;

    //Multilingual support for details and description
    private String details_de;
    private String details_fr;
    private String description_de;
    private String description_fr;

    //Float must be positive, so higher than 0 (zero is not allowed)
    @Positive(message = "Please provide a price for the product.")
    private float price;
    @Positive(message = "Please provide maxProducts for the product.")
    private float maxProducts;
    @Positive(message = "Please provide minPalletSpace for the product.")
    private float minPalletSpace;

    /**
     * Code by: Tibor Haller
     * <p>
     * One product holds many images
     * <p>
     * Bidirectional oneToMany connection. CascadeType and orphanRemoval is required to propagate changes in the parent to children.
     * <p>
     * According to https://stackoverflow.com/a/40339633, changes in children (ProductImage) can be propagated to the parent (Product) by using the @JoinColumn
     * <p>
     * According to https://stackoverflow.com/questions/49592081/jpa-detached-entity-passed-to-persist-nested-exception-is-org-hibernate-persis,
     * CascadeType.PERSIST poses problems as it tries to persist an already existing child when adding the reference in the list
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    //Fixes Hibernate issue to disallow multiple bag fetches https://hibernate.atlassian.net/browse/HHH-1718, https://stackoverflow.com/a/8309458
    @Fetch(value = FetchMode.SUBSELECT)
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

    public String getDetails_de() {
        return details_de;
    }

    public void setDetails_de(String details_de) {
        this.details_de = details_de;
    }

    public String getDetails_fr() {
        return details_fr;
    }

    public void setDetails_fr(String details_fr) {
        this.details_fr = details_fr;
    }

    public String getDescription_de() {
        return description_de;
    }

    public void setDescription_de(String description_de) {
        this.description_de = description_de;
    }

    public String getDescription_fr() {
        return description_fr;
    }

    public void setDescription_fr(String description_fr) {
        this.description_fr = description_fr;
    }
}
