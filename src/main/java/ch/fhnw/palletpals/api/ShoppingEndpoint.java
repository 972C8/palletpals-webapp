package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ShoppingService;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;

@RestController
@RequestMapping(path = "/api")
public class ShoppingEndpoint {
    @Autowired
    private ShoppingService shoppingService;

    /**
     * Code by: Tibor Haller
     * <p>
     * POST CartItem
     */
    @PostMapping(path = "/shopping", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CartItem> postCartItem(@RequestBody CartItem cartItem) {
        try {
            cartItem = shoppingService.saveCartItem(cartItem);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
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
     * GET CartItem by id
     */
    @GetMapping(path = "/shopping/{cartItemID}", produces = "application/json")
    public ResponseEntity<CartItem> getCartItem(@PathVariable(value = "cartItemID") String cartItemId) {
        CartItem cartItem;
        try {
            cartItem = shoppingService.findCartItemById(Long.parseLong(cartItemId));
        } catch (Exception e) {
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //PATCH CartItem by id (only quantity can be updated)

    //DELETE CartItem by id

}