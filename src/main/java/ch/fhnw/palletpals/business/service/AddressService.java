package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.domain.Warehouse;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;

    public ShippingAddress saveCustomerAddress(@Valid ShippingAddress address) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception("Address couldn't be assigned to user");
        }
        return address;
    }

    /**
     * Code by Daniel Locher
     * @param address
     * @return
     * @throws Exception
     */

    public ShippingAddress saveWarehouseAddress(@Valid ShippingAddress address) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception("Address couldn't be assigned to warehouse");
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

    public void deleteAddressByUser(User user) throws Exception{
        try {
            addressRepository.deleteById(addressRepository.findByUser(user).getId());
        } catch (Exception e){
            throw new Exception("User address can't be deleted");
        }
    }

    //TODO only for testing
    public List<ShippingAddress> getAllAddresses(){
        return addressRepository.findAll();
    }
}
