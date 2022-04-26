package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class AddressEndpoint {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @GetMapping(path = "/address", produces = "application/json")
    public ResponseEntity<ShippingAddress> getCustomerAddress()throws Exception{
        ShippingAddress address;
        try {
            address = addressService.getAddressByUserId(userService.getCurrentUser().getId());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(address);
    }

    @PostMapping(path = "/address", produces = "application/json")
    public ResponseEntity<ShippingAddress> postClientAddress(@RequestBody ShippingAddress address) throws Exception{
        try {
            address = addressService.saveAddress(address);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{itemId}")
                .buildAndExpand(address.getId()).toUri();

        return ResponseEntity.created(location).body(address);
    }
}
