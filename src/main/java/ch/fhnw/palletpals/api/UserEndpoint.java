package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserEndpoint {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Code by Daniel Locher
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/profile", produces = "application/json")
    public ResponseEntity<User> getProfile()throws Exception{
        User user;
        try {
            user = userService.getCurrentUser();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Code by Daniel Locher
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody User user) {
        try {
            user.setAddress(addressService.setCoordinates(user.getAddress()));
            ShippingAddress address = user.getAddress();
            User currentUser = userService.saveUser(user);
            address = addressService.saveCustomerAddress(address);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Code written by Daniel Locher & copied from Tibor Haller
     * @param
     * @return
     */
    @PatchMapping(path = "/profile", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> patchProfile(@RequestBody User toBePatchedUser){
        User patchedUser;
        ShippingAddress patchedAddress;
        try {
            User currentUser = userService.getCurrentUser();
            patchedAddress = addressService.patchAddress(toBePatchedUser.getAddress(), currentUser.getAddress());
            toBePatchedUser.setAddress(patchedAddress);
            patchedUser = userService.patchUser(toBePatchedUser);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(patchedUser);
    }

    /**
     * Code by Daniel Locher
     * @return
     */
    @DeleteMapping(path = "/profile")
    public ResponseEntity<Void> deleteProfile(){
        try {
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
