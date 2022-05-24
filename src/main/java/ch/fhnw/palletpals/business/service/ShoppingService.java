package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.domain.order.ProductItem;
import ch.fhnw.palletpals.data.domain.order.UserOrder;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import ch.fhnw.palletpals.data.repository.CartItemRepository;
import ch.fhnw.palletpals.data.repository.ProductRepository;
import ch.fhnw.palletpals.data.repository.ShoppingSessionRepository;
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

    @Autowired
    private ShoppingSessionRepository shoppingSessionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NullAwareBeanUtilsBean beanUtils = new NullAwareBeanUtilsBean();

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

            ShoppingSession currentShoppingSession = getDistinctShoppingSessionOfCurrentUser();
            if (currentShoppingSession == null) {
                throw new Exception("Problem finding or creating shopping session for current user.");
            }

            //If a CartItem with reference to given Product already exists, the CartItem's quantity is increased rather than creating a new object.
            CartItem cartItemWithGivenProductId = cartItemRepository.findCartItemByShoppingSessionIdAndProductId(currentShoppingSession.getId(), cartItem.getProduct().getId());

            //Simply add the given quantity to the existing cartItem, rather than creating a second, duplicate CartItem.
            if (cartItemWithGivenProductId != null) {
                int quantityToAdd = cartItem.getQuantity();
                cartItemWithGivenProductId.addQuantity(quantityToAdd);

                //Return the updated cartItem rather than creating a new one
                return cartItemRepository.save(cartItemWithGivenProductId);
            }

            //Add reference to shoppingSession in CartItem. Due to bi-directional mapping, the reference is also added in shoppingSession
            cartItem.setShoppingSession(currentShoppingSession);

            //Add necessary Product reference to CartItem
            addReferencedProductWithinCartItem(cartItem);

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
            throw new Exception("No product with given ID found.");
        }

        return cartItem;
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Patch product using NullAwareBeansUtilsBean.java
     *
     * @param toBePatchedCartItem
     * @param currentCartItem
     * @return
     * @throws Exception
     */
    public CartItem patchCartItem(CartItem toBePatchedCartItem, CartItem currentCartItem) throws Exception {
        //Only cartItems with valid id are updated.
        if (!cartItemRepository.findById(currentCartItem.getId()).isPresent()) {
            throw new Exception("No cartItem with ID " + currentCartItem.getId() + " found.");
        }

        //Bean utils will copy non null values from toBePatchedCartItem to currentCartItem. Null values will be ignored.
        //This effectively means that the existing CartItem object will be patched (updated)
        beanUtils.copyProperties(currentCartItem, toBePatchedCartItem);

        return cartItemRepository.save(currentCartItem);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Deletes the CartItem using the id.
     *
     * @param cartItemId is to be deleted
     */
    public void deleteCartItemById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Returns the current user's existing shopping session or creates a new shopping session for the current user if it doesn't exist yet.
     * <p>
     * Each user can have one shopping session.
     *
     * @return the current user's shopping session
     */
    public ShoppingSession getDistinctShoppingSessionOfCurrentUser() throws Exception {
        try {
            User currentUser = userService.getCurrentUser();

            ShoppingSession shoppingSession = shoppingSessionRepository.findByUserId(currentUser.getId());

            //Create new shoppingSession for the current user if not exists yet
            if (shoppingSession == null) {
                shoppingSessionRepository.save(new ShoppingSession(currentUser));
            }

            //Retrieve the new shoppingSession after system created it and automatically added required configurations
            shoppingSession = shoppingSessionRepository.findByUserId(currentUser.getId());

            return shoppingSession;
        } catch (Exception e) {
            throw new Exception("Problem finding or creating shopping session for current user.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Returns the current user's shopping session
     *
     * @return ShoppingSession or null if no shopping session exists yet.
     */
    public ShoppingSession getShoppingSessionOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return shoppingSessionRepository.findByUserId(currentUser.getId());
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Reset the current user's shopping session. Used after successful order submission
     */
    public void resetShoppingSessionOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        shoppingSessionRepository.delete(currentUser.getShoppingSession());
    }


    /**
     * Code by: Tibor Haller
     * <p>
     * Update the current user's shopping session using products ordered in a past order. The past order is added to the existing shopping session rather than being replaced.
     *
     * @param pastOrder to update the shopping session from
     * @return the updated shopping session
     */
    public ShoppingSession addPastOrderToShoppingSession(UserOrder pastOrder) throws Exception {
        try {
            //For each productItem in the past order, create a CartItem that is saved into the db with a reference to the current user's shopping session
            for (ProductItem productItem : pastOrder.getProductItems()) {
                CartItem cartItem = new CartItem();

                //Only product reference and quantity are relevant because the rest is fetched automatically by the system using the current data
                cartItem.setProduct(productItem.getProduct());
                cartItem.setQuantity(productItem.getQuantity());

                //Save the cartItem into the db with reference to the user's shopping session
                saveCartItem(cartItem);
            }

            return getDistinctShoppingSessionOfCurrentUser();
        } catch (Exception e) {
            throw new Exception("Could not reorder past order");
        }
    }
}
