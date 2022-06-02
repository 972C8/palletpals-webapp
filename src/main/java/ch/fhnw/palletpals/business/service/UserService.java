/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.component.AdminKey;
import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
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
    @Autowired
    private NullAwareBeanUtilsBean beanUtils = new NullAwareBeanUtilsBean();

    public User saveUser(@Valid User user) throws Exception {
        if (user.getId() == null) {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                throw new Exception("Email address " + user.getEmail() + " already assigned another user.");
            }
        } else if (userRepository.findByEmailAndIdNot(user.getEmail(), user.getId()) != null) {
            throw new Exception("Email address " + user.getEmail() + " already assigned another user.");
        }
        if (user.getRole()==null){
            if (user.getAccessCode()==null){
                user.setRole(UserType.USER);
            } else if (user.getAccessCode().equals(AdminKey.adminKey)){
                user.setRole(UserType.Admin);
            } else {
                user.setRole(UserType.USER);
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(Long id)throws Exception{
        try {
            return userRepository.findUserById(id);
        } catch (Exception e){
            throw new Exception("No user found with id: " + id);
        }
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(user.getUsername());
    }

    public User patchUser(User toBePatchedUser) throws Exception{
        User currentUser = getCurrentUser();
        beanUtils.copyProperties(currentUser, toBePatchedUser);
        return userRepository.save(currentUser);
    }

    /**
     * Code by Daniel Locher
     * To change the password a user needs to provide the current password and the new password.
     * If the provided current password matches the current saved password, the new password is saved.
     * @param newPassword
     * @param oldPassword
     * @return
     * @throws Exception
     */
    public User patchPassword(String newPassword, String oldPassword) throws Exception{
        User currenUser = getCurrentUser();
        if (passwordEncoder.matches(oldPassword, currenUser.getPassword())){
            currenUser.setPassword(newPassword);
            return saveUser(currenUser);
        } else {
            throw new Exception("Password does not match");
        }
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
