package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ProductService;
import ch.fhnw.palletpals.data.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ProductEndpoint {
    @Autowired
    private ProductService productService;

    @PostMapping(path = "/products", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> postProduct(@RequestBody Product product) {
        try {
            product = productService.saveProduct(product);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{productId}")
                .buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(location).body(product);
    }

    /*
    Find product by id and current avatar
     */
    @GetMapping(path = "/products/{productId}", produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable(value = "productId") String productId) {
        Product product;
        try {
            product = productService.findProductById(Long.parseLong(productId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(product);
    }

    //TODO: Use PATCH instead of PUT!
    @PutMapping(path = "/products/{productId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> putProduct(@RequestBody Product product, @PathVariable(value = "productId") String productId) {
        try {
            product.setId(Long.parseLong(productId));
            product = productService.updateProduct(product);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(product);
    }

    @DeleteMapping(path = "/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "productId") String productId) {
        try {
            productService.deleteProduct(Long.parseLong(productId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    /*
        Returns List<Product> of products assigned to the given avatar
     */
    @GetMapping(path = "/products", produces = "application/json")
    public List<Product> getProductItems() {
        return productService.findAllProducts();
    }
}