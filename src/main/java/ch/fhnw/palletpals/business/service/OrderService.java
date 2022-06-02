package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.order.AddressItem;
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
import java.util.Date;
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

    @Autowired
    private NullAwareBeanUtilsBean beanUtils = new NullAwareBeanUtilsBean();

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
            ShoppingSession shoppingSession = shoppingService.getDistinctShoppingSessionOfCurrentUser();

            if (shoppingSession == null || shoppingSession.getShoppingCart().isEmpty()) {
                throw new Exception("Current user has no valid shopping session.");
            }

            UserOrder order = new UserOrder();
            order.setUser(userService.getCurrentUser());
            order.setTotalCost(shoppingSession.getTotalCost());
            order.setDateOrdered(new Date());

            //AddressItem serves as a snapshot of the user address and is part of the new order that is created.
            AddressItem addressItem = new AddressItem();

            //The current user's address to copy into addressItem
            ShippingAddress userAddress = shoppingSession.getUser().getAddress();

            if (userAddress == null) {
                throw new Exception("User must have a shipping address");
            }

            //Bean utils will copy non null values from user's address to addressItem. Null values will be ignored.
            //This effectively means that the current address of the user will be copied into addressItem.
            beanUtils.copyProperties(addressItem, userAddress);

            //Set the address snapshot. It is not necessary to save it into the db, because it is @Embeddable and embedded into UserOrder.java
            order.setAddressItem(addressItem);

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
                productItem.setPalletSpace(cartItem.getProduct().getMinPalletSpace());

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
    private ShippingItem createShippingItemFromShoppingSession(ShoppingSession shoppingSession) throws Exception {
        try {
            ShippingItem shippingItem = new ShippingItem();

            shippingItem.setName("Shipping Cost");
            shippingItem.setShippingCost(shoppingSession.getShippingCost());

            //Store the new ShippingItem in the DB
            return shippingItemRepository.save(shippingItem);
        } catch (Exception e) {
            throw new Exception("Could not create shipping item from shopping session.");
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Find the user order by id.
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public UserOrder findOrderById(Long orderId) throws Exception {
        UserOrder order = orderRepository.findUserOrderById(orderId);
        if (order == null) {
            throw new Exception("No order with ID " + orderId + " found.");
        }
        return order;
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Return list of all orders of the current user, ordered by the id DESC, meaning that the newest orders are returned first.
     *
     * @return
     */
    public List<UserOrder> findAllUserOrdersNewestFirst() {
        return orderRepository.findAllByUserIdOrderByIdDesc(userService.getCurrentUser().getId());
    }

}
