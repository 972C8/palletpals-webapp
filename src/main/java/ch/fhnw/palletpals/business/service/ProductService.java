package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
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

    @Autowired
    private NullAwareBeanUtilsBean beanUtils = new NullAwareBeanUtilsBean();

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
            //Product includes empty ProductImages only with id. Add these referenced images based on the id.
            if (product.getProductImages() != null) {
                addReferencedProductImagesWithinProduct(product);
            }
            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception("No product found.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Patch product using NullAwareBeansUtilsBean.java
     *
     * @param toBePatchedProduct
     * @param currentProduct
     * @return
     * @throws Exception
     */
    public Product patchProduct(Product toBePatchedProduct, Product currentProduct) throws Exception {
        //Only products with valid id are updated.
        if (!productRepository.findById(currentProduct.getId()).isPresent()) {
            throw new Exception("No product with ID " + currentProduct.getId() + " found.");
        }

        //Bean utils will copy non null values from toBePatchedProduct to currentProduct. Null values will be ignored.
        //This effectively means that the existing product object will be patched (updated)
        beanUtils.copyProperties(currentProduct, toBePatchedProduct);

        //Product includes empty ProductImages only with id. Add these referenced images based on the id.
        //This is done after copying of properties because toBePatchedProduct is missing the current product id, which we need
        if (currentProduct.getProductImages() != null) {
            addReferencedProductImagesWithinProduct(currentProduct);
        }

        return productRepository.save(currentProduct);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Products hold a list of referenced ProductImages.
     * For POST or PATCH Product, a list of referenced images is provided. Using this list, correct references of ProductImages are added to the Product.
     *
     * @param product the Product to add the ProductImage references to.
     * @throws Exception
     */
    private void addReferencedProductImagesWithinProduct(Product product) throws Exception {
        try {
            //Add referenced ProductImages to Product that were uploaded prior
            if (product.getProductImages() != null) {
                //Initialize list of referenced productImages
                List<ProductImage> productImages = new ArrayList<>();

                //Get all ProductImage objects by id and add them to the list
                //Add all found images (based on imageId provided through API)
                for (ProductImage productImage : product.getProductImages()) {
                    ProductImage image = productImageRepository.findProductImageById(productImage.getId());

                    //An image can only be assigned to one product. Therefore it is checked if the image was already assigned.
                    //If this image is already referenced in a product, it must be the current product that is updated.
                    //If that is not the case, the image will be referenced more than once, leading to issues.
                    if (image == null || image.getProduct() != null && !image.getProduct().getId().equals(product.getId())) {
                        throw new RuntimeException("One image should only be referenced in one product");
                    }
                    productImages.add(image);
                }
                //Override list of productImages as this is a post request. This method also handles the referencing
                product.setProductImages(productImages);
            }
        } catch (Exception e) {
            throw new Exception("Problem when adding referenced images to given product." + e);
        }
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

    /**
     * Code by: Tibor Haller
     * <p>
     * Return list of all products
     *
     * @return
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
