package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.ShoppingService;
import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
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

    //POST new CartItem (quantity, product reference by id -> pricePerUnit is fetched within the system)
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

    //GET Shopping Cart (all CartItems + calculated shippingCost&totalCost)

    //PATCH CartItem by id (only quantity can be updated)

    //DELETE CartItem by id

}