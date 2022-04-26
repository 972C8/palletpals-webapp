package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    public ShippingAddress saveAddress(@Valid ShippingAddress address) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            User currentUser = userService.getCurrentUser();
            //Assign current user to address
            address.setUser(currentUser);
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception("Address couldn't be saved");
        }
        return address;
    }

    public ShippingAddress getAddressByUserId(Long userId) throws Exception{
        try {
            return addressRepository.findByUser(userService.getUserById(userId));
        } catch (Exception e){
            throw new Exception("Address not found");
        }
    }
}
