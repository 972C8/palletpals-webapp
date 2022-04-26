package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
public class WarehouseEndpoint {
    @Autowired
    private AddressService addressService;
    @Autowired
    private WarehouseService warehouseService;

    /**
     * Code by Daniel Locher
     * @param warehouse
     * @return
     */
    @PostMapping(value = "/warehouse", produces = "application/json")
    public ResponseEntity<Warehouse> postWarehouse(@RequestBody Warehouse warehouse){
        try {
            ShippingAddress warehouseAddress = warehouse.getAddress();
            warehouse.setAddress(null);
            Warehouse savedWarehouse = warehouseService.saveWarehouse(warehouse);
            addressService.saveWarehouseAddress(warehouseAddress, savedWarehouse);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
