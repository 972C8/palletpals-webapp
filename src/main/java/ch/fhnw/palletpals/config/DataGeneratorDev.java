package ch.fhnw.palletpals.config;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.DistanceService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("dev")
@Configuration
public class DataGeneratorDev {

    @Autowired
    private UserService userService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private DistanceService distanceService;

    User userUser;
    Warehouse demoWarehouse;

    @PostConstruct
    private void init() throws Exception {
         demoUser();
         demoWarehouse();
    }

    private void demoUser() throws Exception {
        userUser = new User();
        userUser.setEmail("user@user.com");
        userUser.setPassword("password");
        userUser.setUserName("user");
        ShippingAddress demoUserAddress = new ShippingAddress();
        demoUserAddress.setStreet("Bahnhofstrasse 6");
        demoUserAddress.setPostalCode("5210");
        demoUserAddress.setCity("Windisch");
        demoUserAddress.setStreet("Demo Street");
        userUser.setAddress(demoUserAddress);
        userService.saveUser(userUser);
        addressService.saveCustomerAddress(demoUserAddress);
    }
    private void demoWarehouse() throws Exception{
        demoWarehouse = new Warehouse();
        demoWarehouse.setName("Demo Warehouse");
        ShippingAddress demoWarehouseAddress = new ShippingAddress();
        demoWarehouseAddress.setOrganisationName("Demo Warehouse");
        demoWarehouseAddress.setStreet("Wiesentalstrasse 6a");
        demoWarehouseAddress.setPostalCode("8962");
        demoWarehouseAddress.setCity("Bergdietikon");
        demoWarehouseAddress.setCountry("CH");
        demoWarehouse.setAddress(demoWarehouseAddress);
        demoWarehouse = warehouseService.saveWarehouse(demoWarehouse);
    }
    private void testDistance() throws Exception{
        distanceService.distance(demoWarehouse.getAddress(), userUser.getAddress());
    }
}
