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

    /**
     * Code by Daniel Locher
     * @return address
     * @throws Exception
     */

    @GetMapping(path = "/address", produces = "application/json")
    public ResponseEntity<ShippingAddress> getCustomerAddress()throws Exception{
        try {
            ShippingAddress address;
            address = addressService.getAddressByUserId(userService.getCurrentUser().getId());
            return ResponseEntity.ok(address);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
