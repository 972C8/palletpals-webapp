/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.controller;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @PostMapping("/user/register")
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

    @GetMapping("/profile")
    public @ResponseBody
    User getProfile() {
        return userService.getCurrentUser();
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> putProfile(@RequestBody User user) {
        try {
            user.setId(userService.getCurrentUser().getId());
            userService.saveUser(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/validate", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Void> init() {
        return ResponseEntity.ok().build();
    }
}