package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.image.ProductImage;
import ch.fhnw.palletpals.data.repository.ProductImageRepository;
import ch.fhnw.palletpals.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    /**
     * Code by: Tibor Haller
     * <p>
     * Save BucketItem and assign referenced objects based on provided id in JSON using a proxy
     * <p>
     * Proxy logic adapted from:
     * https://github.com/AnghelLeonard/Hibernate-SpringBoot/tree/master/HibernateSpringBootPopulatingChildViaProxy
     *
     * @param product
     * @return
     * @throws Exception
     */
    public Product saveProduct(@Valid Product product) throws Exception {
        try {
            //TODO: Add additional requirements (e.g. price not negative) before saving new product. Possibly done in Product class directly

            //Add referenced ProductImages to Product that were uploaded prior
            if (product.getProductImages() != null) {
                //Initialize list of referenced productImages
                List<ProductImage> productImages = new ArrayList<>();

                //Get all ProductImage objects by id and add them to the list
                for (ProductImage productImage : product.getProductImages()) {
                    ProductImage image = productImageRepository.findProductImageById(productImage.getId());

                    //Add all found images (based on imageId provided through API)
                    //An image can only be assigned to one product. Therefore it is checked if the image was already assigned.
                    if (image == null || image.getProduct() != null) {
                        throw new RuntimeException("Invalid imageId provided: " + image.getId());
                    }
                    productImages.add(image);
                }
                //Override list of productImages as this is a post request. This method also handles the referencing
                product.setProductImages(productImages);
            }

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception("No product found.");
        }
    }

    /**
     * Code by: Tibor Haller
     *
     * @param product
     * @return
     * @throws Exception
     */
    //TODO: Use PATCH instead of PUT!
    public Product updateProduct(@Valid Product product) throws Exception {
        //Check if product with given id is already present
        //Only products with valid id are updated.
        if (!productRepository.findById(product.getId()).isPresent()) {
            throw new Exception("No product with ID " + product.getId() + " found.");
        }
        //Call regular save method
        return saveProduct(product);
    }

    /**
     * Code by: Tibor Haller
     *
     * @param productId
     * @return
     * @throws Exception
     */
    public Product findProductById(Long productId) throws Exception {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new Exception("No product with ID " + productId + " found.");
        }
        return product;
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Recursively deletes all referenced images. For more information, check Product.java, specifically the "private List<ProductImage> productImages".
     *
     * @param productId
     */
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
