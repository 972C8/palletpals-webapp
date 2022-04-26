package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserEndpoint {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    /**
     * Code by Daniel Locher
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/profile", produces = "application/json")
    public ResponseEntity<User> getProfile()throws Exception{
        try {
            return ResponseEntity.ok(userService.getCurrentUser());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody User user) {
        try {
            ShippingAddress address = user.getAddress();
            user.setAddress(null);
            User currentUser = userService.saveUser(user);
            addressService.saveCustomerAddress(address, currentUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/profile")
    public ResponseEntity<Void> deleteProfile(){
        try {
            addressService.deleteAddressByUser(userService.getCurrentUser());
            userService.deleteUser(userService.getCurrentUser().getId());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Void> init() {
        return ResponseEntity.ok().build();
    }
}
