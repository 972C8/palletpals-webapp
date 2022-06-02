package ch.fhnw.palletpals.config;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.ProductService;
import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.business.service.shoppingServices.ServiceProviderService;
import ch.fhnw.palletpals.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("demo")
@Configuration
public class DemoEnvironmentGenerator {

    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ProductService productService;

    User demoAdmin;
    Warehouse demoWarehouse;
    Warehouse demoWarehouse2;
    ServiceProvider demoSp;
    Product product1;
    Product product2;
    Product product3;
    Product product4;

    @PostConstruct
    private void init() throws Exception{
        demoUser();
        demoWarehouse();
        demoServiceProvider();
        demoproducts();
    }

    private void demoUser() throws Exception {
        demoAdmin = new User();
        demoAdmin.setEmail("user@user.com");
        demoAdmin.setPassword("password");
        demoAdmin.setUserName("user");
        demoAdmin.setAccessCode("testKey69");
        ShippingAddress demoUserAddress = new ShippingAddress();
        demoUserAddress.setFirstName("Hans");
        demoUserAddress.setLastName("Peter");
        demoUserAddress.setCountry("Switzerland");
        demoUserAddress.setStreet("Zeughausstrasse 6");
        demoUserAddress.setPostalCode("8500");
        demoUserAddress.setCity("Frauenfeld");
        demoAdmin.setAddress(addressService.setCoordinates(demoUserAddress));
        userService.saveUser(demoAdmin);
        addressService.saveCustomerAddress(demoUserAddress);
    }

    private void demoWarehouse() throws Exception{
        demoWarehouse = new Warehouse();
        demoWarehouse.setName("Mutenz Campus");
        ShippingAddress demoWarehouseAddress = new ShippingAddress();
        demoWarehouseAddress.setOrganisationName("Demo Warehouse");
        demoWarehouseAddress.setStreet("Hofackerstrasse 30");
        demoWarehouseAddress.setPostalCode("4132");
        demoWarehouseAddress.setCity("Muttenz");
        demoWarehouseAddress.setCountry("CH");
        demoWarehouse.setAddress(addressService.setCoordinates(demoWarehouseAddress));
        demoWarehouse = warehouseService.saveWarehouse(demoWarehouse);

        demoWarehouse2 = new Warehouse();
        demoWarehouse2.setName("Olten Campus");
        ShippingAddress demoWarehouseAddress2 = new ShippingAddress();
        demoWarehouseAddress2.setOrganisationName("Demo Warehouse 2");
        demoWarehouseAddress2.setStreet("Von Rollstrasse 10");
        demoWarehouseAddress2.setPostalCode("4600");
        demoWarehouseAddress2.setCity("Olten");
        demoWarehouseAddress2.setCountry("CH");
        demoWarehouse2.setAddress(addressService.setCoordinates(demoWarehouseAddress2));
        demoWarehouse2 = warehouseService.saveWarehouse(demoWarehouse2);
    }

    private void demoServiceProvider() throws Exception{
        demoSp = serviceProviderService.saveServiceProvider("{\n" +
                "    \"name\": \"Fake DHL\", \n" +
                "    \"kmArray\": \"[30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360]\",\n" +
                "    \"palletArray\": \"[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]\",\n" +
                "    \"priceMatrix\": \"[[58.65,87,114.5,137.1,160.8,181.65,201.45,220.3,238.35,255.35,271.45,286.55],[67,99.4,130.85,156.75,183.8,207.55,230.25,251.85,272.4,291.9,310.25,327.5],[75.4,111.9,147.15,176.35,206.65,233.5,259,283.35,306.45,328.4,348.95,368.4],[83.75,124.3,163.5,195.9,229.7,259.45,287.7,314.85,340.5,364.75,387.8,409.4],[92.15,136.7,179.8,215.5,252.6,285.3,316.5,346.3,374.5,401.25,426.5,450.35],[100.55,149.2,196.15,235.15,275.6,311.25,345.35,377.75,408.6,437.75,465.35,491.25],[108.95,161.55,212.5,253.9,298.6,337.15,374.15,409.3,442.6,474.25,504.05,532.15],[117.3,174.05,228.9,274.25,321.55,363.15,402.9,440.75,476.7,510.75,542.9,573.15],[125.65,186.45,245.3,293.9,344.55,389.1,431.7,472.2,510.75,547.2,581.6,614.05],[134.05,198.85,261.6,313.45,367.5,415,460.45,503.7,544.7,583.65,620.35,655.05],[142.4,211.3,277.95,332.95,390.45,441.05,489.2,535.2,578.85,620.15,659.2,695.95],[150.75,223.8,294.3,352.65,413.45,466.9,518,566.7,612.85,656.65,697.95,736.9]]\"\n" +
                "}");

    }

    private void demoproducts() throws Exception{
        product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Product 1 is good, reliable, and big. Recommended for construction workers that search for high quality.");
        product1.setDescription_de("Produkt 1 ist gut, verlässlich und gross. Für Bauarbeiter, die nach hoher Qualität verlangen.");
        product1.setDescription_fr("Le produit est bon, fiable et grand. Recommandé aux travailleurs de la construction qui recherchent une qualité élevée.");
        product1.setDetails("Height: 100, Width: 150, Lenght: 50, Power: 300W");
        product1.setDetails_de("Höhe: 100, Breite: 150, Länge: 50, Leistung: 300W");
        product1.setDescription_fr("Hauteur: 100, Largeur: 150, Longueur: 50, Puissance: 300W");
        product1.setPrice((float) 149.95);
        product1.setMaxProducts(25);
        product1.setMinPalletSpace((float) 1.2);
        product1 = productService.saveProduct(product1);

        product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Product 2 is the most advanced of his generation. The enhanced computing power enables more processing and your business will expand!");
        product2.setDescription_de("ProduKt 2 ist das fortgeschrittenste seiner Art. Mit der verbesserten Rechenleistung wird Ihr Geschäft durch die Decke gehen!");
        product2.setDescription_fr("Le produit 2 est le plus avancé de sa génération. La puissance de calcul accrue permet un traitement plus important et votre activité va se développer!");
        product2.setDetails("Height: 80, Width: 130, Lenght: 70, Power: 500W");
        product2.setDetails_de("Höhe: 80, Breite: 130, Länge: 70, Leistung: 500W");
        product2.setDescription_fr("Hauteur: 80, Largeur: 130, Longueur: 70, Puissance: 500W");
        product2.setPrice((float) 249.95);
        product2.setMaxProducts(10);
        product2.setMinPalletSpace((float) 2);
        product2 = productService.saveProduct(product2);

        product3 = new Product();
        product3.setName("Product 3");
        product3.setDescription("Product 3 is our most successful product and clients all over the world experience perfect results with product 3.");
        product3.setDescription_de("Produkt 3 ist unser erfolgreichstes Produkt auf das Kunden auf der ganzen Welt vertrauen.");
        product3.setDescription_fr("Le produit 3 est notre produit le plus réussi et les clients du monde entier obtiennent des résultats parfaits avec le produit 3.");
        product3.setDetails("Height: 50, Width: 90, Lenght: 50, Power: 200W");
        product3.setDetails_de("Höhe: 50, Breite: 90, Länge: 50, Leistung: 200W");
        product3.setDescription_fr("Hauteur: 50, Largeur: 90, Longueur: 50, Puissance: 200W");
        product3.setPrice((float) 84.95);
        product3.setMaxProducts(15);
        product3.setMinPalletSpace((float) 2.5);
        product3 = productService.saveProduct(product3);

        product4 = new Product();
        product4.setName("Product 4");
        product4.setDescription("For aesthetic people we recommend product 4. With its neat design it can be placed almost everywhere.");
        product4.setDescription_de("Für unsere ästhetischen Kunden empfehlen wir Produkt 4, welches mit seinem schlichten Design überzeugt.");
        product4.setDescription_fr("Pour les personnes esthétiques, nous recommandons le produit 4. Avec son design soigné, il peut être placé presque partout.");
        product4.setDetails("Height: 40, Width: 40, Lenght: 40, Power: 250W");
        product4.setDetails_de("Höhe: 40, Breite: 40, Länge: 40, Leistung: 250W");
        product4.setDescription_fr("Hauteur: 40, Largeur: 40, Longueur: 40, Puissance: 250W");
        product4.setPrice((float) 199.95);
        product4.setMaxProducts(100);
        product4.setMinPalletSpace((float) 0.8);
        product4 = productService.saveProduct(product4);
    }
}
