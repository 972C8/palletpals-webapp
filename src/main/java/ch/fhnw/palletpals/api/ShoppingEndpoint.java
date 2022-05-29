package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.OrderService;
import ch.fhnw.palletpals.business.service.ShoppingService;
import ch.fhnw.palletpals.data.domain.order.UserOrder;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class ShoppingEndpoint {
    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private OrderService orderService;

    //Used by patchCartItem method to map objects
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(ShoppingEndpoint.class);

    /**
     * Code by: Tibor Haller
     * <p>
     * POST CartItem
     */
    @PostMapping(path = "/shopping", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartItem> postCartItem(@RequestBody CartItem cartItem) {
        try {
            cartItem = shoppingService.saveCartItem(cartItem);
            logger.info("Cart item saved with id: " + cartItem.getId());
            ShoppingSession shoppingSession = shoppingService.saveShoppingSessionWithCosts();
            logger.info("Shopping session with shipping costs saved with id: " + shoppingSession.getId());
        } catch (ConstraintViolationException e) {
            logger.error("Error while saving new cart item: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            logger.error("Error while saving new cart item: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{cartItem}")
                .buildAndExpand(cartItem.getId()).toUri();

        return ResponseEntity.created(location).body(cartItem);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * GET order by id
     */
    @PostMapping(path = "/shopping/{orderId}", produces = "application/json")
    public ResponseEntity<ShoppingSession> addPastOrderToShoppingSession(@PathVariable(value = "orderId") String orderId) {
        try {
            UserOrder pastOrder = orderService.findOrderById(Long.parseLong(orderId));

            ShoppingSession shoppingSession = shoppingService.addPastOrderToShoppingSession(pastOrder);

            return ResponseEntity.ok(shoppingSession);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * GET CartItem by id
     */
    @GetMapping(path = "/shopping/{cartItemId}", produces = "application/json")
    public ResponseEntity<CartItem> getCartItem(@PathVariable(value = "cartItemId") String cartItemId) {
        CartItem cartItem;
        try {
            cartItem = shoppingService.findCartItemById(Long.parseLong(cartItemId));
        } catch (Exception e) {
            logger.error("Error while getting cart item with id " + cartItemId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(cartItem);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Returns the ShoppingSession of the current user
     */
    @GetMapping(path = "/shopping", produces = "application/json")
    public ResponseEntity<ShoppingSession> getShoppingSession() {
        try {
            //Returns (if already exists) or creates the current user's shopping session
            ShoppingSession shoppingSession = shoppingService.getDistinctShoppingSessionOfCurrentUser();
            return ResponseEntity.ok(shoppingSession);
        } catch (Exception e) {
            logger.error("Error while getting shopping session of current user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * Patch cartItem
     * <p>
     * Roughly based on the idea of objectMapper and NullAwareBeanUtilsBean.java (https://stackoverflow.com/a/45205844)
     *
     * @param cartItemPatch provided by user.
     * @param cartItemId    to update.
     * @return Product
     */
    @PatchMapping(path = "/shopping/{cartItemId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartItem> patchCartItem(@RequestBody Map<String, Object> cartItemPatch, @PathVariable(value = "cartItemId") String cartItemId) {
        CartItem patchedCartItem;
        try {
            CartItem currentCartItem = shoppingService.findCartItemById(Long.parseLong(cartItemId));

            //The provided patch (as Map<String, String>) is converted into a Product object.
            CartItem toBePatchedCartItem = objectMapper.convertValue(cartItemPatch, CartItem.class);

            //Set variables to null so that they are ignored when patching, meaning that the current values are persisted.
            toBePatchedCartItem.setPricePerUnit(0.0f);
            toBePatchedCartItem.setShoppingSession(null);

            //The current product is patched (updated) using the provided patch
            patchedCartItem = shoppingService.patchCartItem(toBePatchedCartItem, currentCartItem);
            logger.info("Cart item patched with id " + patchedCartItem.getId());
            ShoppingSession shoppingSession = shoppingService.saveShoppingSessionWithCosts();
            logger.info("Shopping session updated with id: " + shoppingSession.getId());
        } catch (Exception e) {
            logger.error("Error while patching cart item with id " + cartItemId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(patchedCartItem);
    }

    /**
     * Code by: Tibor Haller
     *
     * @param cartItemId
     * @return
     */
    @DeleteMapping(path = "/shopping/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable(value = "cartItemId") String cartItemId) {
        try {
            shoppingService.deleteCartItemById(Long.parseLong(cartItemId));
            logger.info("Cart item deleted with id: " + cartItemId);
            ShoppingSession shoppingSession = shoppingService.saveShoppingSessionWithCosts();
            logger.info("Shopping session updated with id: " + shoppingSession.getId());
        } catch (Exception e) {
            logger.error("Error while deleting cart item with id " + cartItemId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

}