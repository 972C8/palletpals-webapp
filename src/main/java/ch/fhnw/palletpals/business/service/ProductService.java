package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(@Valid Product product) throws Exception {
        try {
            //TODO: Add additional requirements (e.g. price not negative) before saving new product. Possibly done in Product class directly
            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception("No product found.");
        }
    }

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

    public Product findProductById(Long productId) throws Exception {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new Exception("No product with ID " + productId + " found.");
        }
        return product;
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
