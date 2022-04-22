/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.Validator;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    Validator validator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(@Valid User user) throws Exception {
        if (user.getId() == null) {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                throw new Exception("Email address " + user.getEmail() + " already assigned another user.");
            }
        } else if (userRepository.findByEmailAndIdNot(user.getEmail(), user.getId()) != null) {
            throw new Exception("Email address " + user.getEmail() + " already assigned another user.");
        }
        //TODO define process for setting UserType
        user.setRole(UserType.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(user.getUsername());
    }
}
