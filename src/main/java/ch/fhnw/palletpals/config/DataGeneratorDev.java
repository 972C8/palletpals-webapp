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
    Warehouse demoWarehouse2;

    @PostConstruct
    private void init() throws Exception {
         demoUser();
         demoWarehouse();
         testDistance();
    }

    private void demoUser() throws Exception {
        userUser = new User();
        userUser.setEmail("user@user.com");
        userUser.setPassword("password");
        userUser.setUserName("user");
        ShippingAddress demoUserAddress = new ShippingAddress();
        demoUserAddress.setStreet("Frohsinnstrasse 3");
        demoUserAddress.setPostalCode("5430");
        demoUserAddress.setCity("Wettingen");
        userUser.setAddress(demoUserAddress);
        userService.saveUser(userUser);
        addressService.saveCustomerAddress(demoUserAddress);
    }
    private void demoWarehouse() throws Exception{
        demoWarehouse = new Warehouse();
        demoWarehouse.setName("Brugg Campus");
        ShippingAddress demoWarehouseAddress = new ShippingAddress();
        demoWarehouseAddress.setOrganisationName("Demo Warehouse");
        demoWarehouseAddress.setStreet("Bahnhofstrasse 6");
        demoWarehouseAddress.setPostalCode("5210");
        demoWarehouseAddress.setCity("Windisch");
        demoWarehouseAddress.setCountry("CH");
        demoWarehouse.setAddress(demoWarehouseAddress);
        demoWarehouse = warehouseService.saveWarehouse(demoWarehouse);

        demoWarehouse2 = new Warehouse();
        demoWarehouse2.setName("Olten Campus");
        ShippingAddress demoWarehouseAddress2 = new ShippingAddress();
        demoWarehouseAddress2.setOrganisationName("Demo Warehouse 2");
        demoWarehouseAddress2.setStreet("Von Rollstrasse 10");
        demoWarehouseAddress2.setPostalCode("4600");
        demoWarehouseAddress2.setCity("Olten");
        demoWarehouseAddress2.setCountry("CH");
        demoWarehouse2.setAddress(demoWarehouseAddress2);
        demoWarehouse2 = warehouseService.saveWarehouse(demoWarehouse2);
    }
    private void testDistance() throws Exception{
        System.out.println(distanceService.nearestWarehouse(userUser.getAddress()));
    }
}
