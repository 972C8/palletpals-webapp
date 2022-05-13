package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.OrderService;
import ch.fhnw.palletpals.data.domain.order.UserOrder;
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
public class OrderEndpoint {
    @Autowired
    private OrderService orderService;

    /**
     * Code by: Tibor Haller
     * <p>
     * Order is created based on current user's shopping session
     * <p>
     * POST
     */
    @PostMapping(path = "/orders", produces = "application/json")
    public ResponseEntity<UserOrder> postOrder() {
        UserOrder order;
        try {
            //Order is created from current user's shopping session
            order = orderService.createOrderFromShoppingSession();
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{orderId}")
                .buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(location).body(order);
    }

    /**
     * Code by: Tibor Haller
     * <p>
     * GET order by id
     */
    @GetMapping(path = "/orders/{orderId}", produces = "application/json")
    public ResponseEntity<UserOrder> getOrder(@PathVariable(value = "orderId") String orderId) {
        UserOrder order;
        try {
            order = orderService.findOrderById(Long.parseLong(orderId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(order);
    }
}