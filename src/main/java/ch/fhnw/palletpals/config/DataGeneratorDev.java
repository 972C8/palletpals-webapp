package ch.fhnw.palletpals.config;

import ch.fhnw.palletpals.business.service.*;
import ch.fhnw.palletpals.data.domain.ServiceProvider;
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
    @Autowired
    private ServiceProviderService serviceProviderService;

    User userUser;
    Warehouse demoWarehouse;
    Warehouse demoWarehouse2;
    ServiceProvider demoServiceProvider1;

    @PostConstruct
    private void init() throws Exception {
         demoUser();

         //When using the application dev profile, demo data is generated and demo calculations are performed.
         demoWarehouse();
         demoServiceProvider();
         double drivingDistance = distanceService.nearestWarehouse(userUser.getAddress()).getDistanceToDestinationInKM();
         int palletSpace = 4;
         double price = serviceProviderService.getShippingPrice(demoServiceProvider1, palletSpace, drivingDistance);
         System.out.println("Driving Distance: "+drivingDistance+" Pallet space: "+palletSpace+" Price: "+price);
         //testDistance();
    }

    private void demoUser() throws Exception {
        userUser = new User();
        userUser.setEmail("user@user.com");
        userUser.setPassword("password");
        userUser.setUserName("user");
        ShippingAddress demoUserAddress = new ShippingAddress();
        demoUserAddress.setStreet("Zeughausstrasse 6");
        demoUserAddress.setPostalCode("8500");
        demoUserAddress.setCity("Frauenfeld");
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

    private void demoServiceProvider() throws Exception{
        demoServiceProvider1 = serviceProviderService.saveServiceProvider("{\n" +
                "    \"name\": \"Fake DHL\", \n" +
                "    \"kmArray\": \"[30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360]\",\n" +
                "    \"palletArray\": \"[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]\",\n" +
                "    \"priceMatrix\": \"[[58.65,87,114.5,137.1,160.8,181.65,201.45,220.3,238.35,255.35,271.45,286.55],[67,99.4,130.85,156.75,183.8,207.55,230.25,251.85,272.4,291.9,310.25,327.5],[75.4,111.9,147.15,176.35,206.65,233.5,259,283.35,306.45,328.4,348.95,368.4],[83.75,124.3,163.5,195.9,229.7,259.45,287.7,314.85,340.5,364.75,387.8,409.4],[92.15,136.7,179.8,215.5,252.6,285.3,316.5,346.3,374.5,401.25,426.5,450.35],[100.55,149.2,196.15,235.15,275.6,311.25,345.35,377.75,408.6,437.75,465.35,491.25],[108.95,161.55,212.5,253.9,298.6,337.15,374.15,409.3,442.6,474.25,504.05,532.15],[117.3,174.05,228.9,274.25,321.55,363.15,402.9,440.75,476.7,510.75,542.9,573.15],[125.65,186.45,245.3,293.9,344.55,389.1,431.7,472.2,510.75,547.2,581.6,614.05],[134.05,198.85,261.6,313.45,367.5,415,460.45,503.7,544.7,583.65,620.35,655.05],[142.4,211.3,277.95,332.95,390.45,441.05,489.2,535.2,578.85,620.15,659.2,695.95],[150.75,223.8,294.3,352.65,413.45,466.9,518,566.7,612.85,656.65,697.95,736.9]]\"\n" +
                "}");

    }
}
