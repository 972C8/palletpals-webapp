package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.repository.CartItemRepository;
import ch.fhnw.palletpals.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class ShoppingService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Code by: Tibor Haller
     *
     * @param cartItem the new CartItem to be created
     * @return the saved CartItem
     * @throws Exception
     */
    public CartItem saveCartItem(@Valid CartItem cartItem) throws Exception {
        try {
            if (cartItem.getProduct() == null) {
                throw new Exception("CartItem requires reference to Product.");
            }
            //Add necessary Product reference to CartItem
            addReferencedProductWithinCartItem(cartItem);

            //TODO: Add constructor functionality to CartItem that ensures correct PricePerUnit and Product reference
            //Override PricePerUnit to default. This value cannot be set using the API as it is done internally
            cartItem.setPricePerUnit(0.0f);

            return cartItemRepository.save(cartItem);
        } catch (Exception e) {
            throw new Exception("No cartItem found.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * A CartItem references a Product
     * For POST or PATCH CartItem, an id of the referenced Product is provided. Using this id, correct reference of Product is added to the CartItem.
     *
     * @param cartItem the CartItem to add the Product reference to.
     * @throws Exception
     */
    private void addReferencedProductWithinCartItem(CartItem cartItem) throws Exception {
        try {
            //Add referenced ProductImages to Product that were uploaded prior
            if (cartItem.getProduct() != null) {
                Product product = productRepository.findProductById(cartItem.getProduct().getId());

                //An image can only be assigned to one product. Therefore it is checked if the image was already assigned.
                //If this image is already referenced in a product, it must be the current product that is updated.
                //If that is not the case, the image will be referenced more than once, leading to issues.
                if (product == null || cartItem.getProduct() != null && !cartItem.getProduct().getId().equals(product.getId())) {
                    throw new RuntimeException("One product should be referenced in one CartItem");
                }
                //Override referenced Product in CartItem
                cartItem.setProduct(product);
            }
        } catch (Exception e) {
            throw new Exception("Problem when adding referenced Product to given CartItem." + e);
        }
    }

    /**
     * Code by: Tibor Haller
     *
     * @param cartItemId
     * @return
     * @throws Exception
     */
    public CartItem findCartItemById(Long cartItemId) throws Exception {

        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        if (cartItem == null) {
            throw new Exception("No product with ID " + cartItem + " found.");
        }

        return cartItem;
    }
}
