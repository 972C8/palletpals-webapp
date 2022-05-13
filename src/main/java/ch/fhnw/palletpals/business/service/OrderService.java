package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.order.ProductItem;
import ch.fhnw.palletpals.data.domain.order.ShippingItem;
import ch.fhnw.palletpals.data.domain.order.UserOrder;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import ch.fhnw.palletpals.data.repository.OrderRepository;
import ch.fhnw.palletpals.data.repository.ProductItemRepository;
import ch.fhnw.palletpals.data.repository.ShippingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ShippingItemRepository shippingItemRepository;

    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private UserService userService;

    /**
     * Code by: Tibor Haller
     * <p>
     * Creates an order from the current user's ShoppingSession
     *
     * @return
     * @throws Exception
     */
    public UserOrder createOrderFromShoppingSession() throws Exception {
        try {
            ShoppingSession shoppingSession = shoppingService.getShoppingSessionOfCurrentUser();

            if (shoppingSession == null || shoppingSession.getShoppingCart().isEmpty()) {
                throw new Exception("Current user has no valid shopping session.");
            }

            UserOrder order = new UserOrder();
            order.setUser(userService.getCurrentUser());
            order.setTotalCost(shoppingSession.getTotalCost());
            //TODO: order.setDateOrdered();

            //Create relevant objects (ProductItem and ShippingItem) and add them as a reference to the new Order

            //For each CartItem in the shopping cart of the shopping session, create a ProductItem
            List<ProductItem> productItems = createProductItemsFromShoppingCart(shoppingSession.getShoppingCart());
            //Add ProductItem references to new Order
            order.setProductItems(productItems);

            //Create one ShippingItem from the given ShoppingSession
            ShippingItem shippingItem = createShippingItemFromShoppingSession(shoppingSession);
            //Add ShippingItem reference to new Order
            order.setShippingItem(shippingItem);

            return orderRepository.save(order);
        } catch (Exception e) {
            throw new Exception("No product found.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * For each CartItem in the shopping cart of the ShoppingSession, create a product item and save it in the DB.
     *
     * @param shoppingCart
     */
    private List<ProductItem> createProductItemsFromShoppingCart(List<CartItem> shoppingCart) throws Exception {
        try {

            List<ProductItem> productItems = new ArrayList<>();
            //For each CartItem in the ShoppingSession, create a ProductItem
            for (CartItem cartItem : shoppingCart) {
                ProductItem productItem = new ProductItem();

                productItem.setName(cartItem.getProduct().getName());
                productItem.setQuantity(cartItem.getQuantity());
                productItem.setPricePerUnit(cartItem.getPricePerUnit());
                productItem.setProduct(cartItem.getProduct());
                //TODO: setPalletSpace()

                if (productItem.getQuantity() == 0.0 || productItem.getPricePerUnit() == 0.0) {
                    throw new Exception("ProductItems cannot be empty");
                }
                //Store the new productItem in the database and add it to the list to return
                productItems.add(productItemRepository.save(productItem));
            }
            return productItems;
        } catch (Exception e) {
            throw new Exception("Issue creating product items from the shopping cart.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Create one ShippingItem from the given ShoppingSession and save it in the DB
     *
     * @param shoppingSession
     */
    private ShippingItem createShippingItemFromShoppingSession(ShoppingSession shoppingSession) {
        ShippingItem shippingItem = new ShippingItem();

        //TODO: Define another name?
        shippingItem.setName("Shipping Cost");
        shippingItem.setShippingCost(shoppingSession.getShippingCost());

        //Store the new ShippingItem in the DB
        shippingItemRepository.save(shippingItem);

        return shippingItem;
    }


}
