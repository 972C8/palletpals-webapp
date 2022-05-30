package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(UserEndpoint.class);

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
            logger.error("Error while getting current user");
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
            logger.info("User saved with id: " + currentUser.getId());
            address = addressService.saveCustomerAddress(address);
            logger.info("Address saved with id: " + address.getId());
        } catch (Exception e) {
            logger.error("Error while saving new user: " + e.getMessage());
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
    public ResponseEntity<User> patchProfile(@RequestBody Map<String, Object> userPatch){
        User patchedUser;
        ShippingAddress patchedAddress;
        try {
            User toBePatchedUser = objectMapper.convertValue(userPatch, User.class);
            User currentUser = userService.getCurrentUser();
            if (toBePatchedUser.getAddress()!=null){
                patchedAddress = addressService.patchAddress(toBePatchedUser.getAddress(), currentUser.getAddress());
                logger.info("Address patched with id: " + patchedAddress.getId());
                toBePatchedUser.setAddress(patchedAddress);
            }
            patchedUser = userService.patchUser(toBePatchedUser);
            logger.info("User patched with id: " + patchedUser.getId());
        } catch (Exception e){
            logger.error("Error while patching current user: " + e.getMessage());
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
            logger.info("Current user deleted");
        } catch (Exception e){
            logger.error("Error while deleting user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Void> init() {
        return ResponseEntity.ok().build();
    }
}
