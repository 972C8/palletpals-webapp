package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ProductService;
import ch.fhnw.palletpals.data.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class ProductEndpoint {
    @Autowired
    private ProductService productService;

    Logger logger = LoggerFactory.getLogger(ProductEndpoint.class);

    //Used by patchProduct method to map objects
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Code by: Tibor Haller
     * <p>
     * POST
     */
    @PostMapping(path = "/products", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> postProduct(@RequestBody Product product) {
        try {
            product = productService.saveProduct(product);
            logger.info("Product was created with id: " + product.getId());
        } catch (ConstraintViolationException e) {
            logger.error("Error while creating product: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            logger.error("Error while creating product: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{productId}")
                .buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(location).body(product);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * GET product by id
     */
    @GetMapping(path = "/products/{productId}", produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "productId") String productId) {
        Product product;
        try {
            product = productService.findProductById(Long.parseLong(productId));
        } catch (Exception e) {
            logger.error("Error while getting product with id: " + productId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(product);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Patch product
     *
     * Roughly based on the idea of objectMapper and NullAwareBeanUtilsBean.java (https://stackoverflow.com/a/45205844)
     *
     * @param productPatch   provided by user.
     * @param productId to update.
     * @return Product
     */
    @PatchMapping(path = "/products/{productId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> patchProduct(@RequestBody Map<String, Object> productPatch, @PathVariable(value = "productId") String productId) {
        Product patchedProduct;
        try {
            Product currentProduct = productService.findProductById(Long.parseLong(productId));

            //The provided patch (as Map<String, String>) is converted into a Product object.
            Product toBePatchedProduct = objectMapper.convertValue(productPatch, Product.class);

            //The current product is patched (updated) using the provided patch
            patchedProduct = productService.patchProduct(toBePatchedProduct, currentProduct);
            logger.info("Product has been patched with id: " + patchedProduct.getId());
        } catch (Exception e) {
            logger.error("Error while patching product with id: " + productId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(patchedProduct);
    }

    /**
     * Code by: Tibor Haller
     *
     * @param productId
     * @return
     */
    @DeleteMapping(path = "/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "productId") String productId) {
        try {
            productService.deleteProduct(Long.parseLong(productId));
            logger.info("Product deleted with id: " + productId);
        } catch (Exception e) {
            logger.error("Error while deleting product with id: " + productId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Returns List<Product> of products assigned to the given avatar
     */
    @GetMapping(path = "/products", produces = "application/json")
    public List<Product> getProductItems() {
        return productService.findAllProducts();
    }
}