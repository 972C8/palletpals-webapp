package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseEndpoint {
    @Autowired
    private AddressService addressService;
    @Autowired
    private WarehouseService warehouseService;

    Logger logger = LoggerFactory.getLogger(WarehouseEndpoint.class);

    /**
     * Code by Daniel Locher
     * @param warehouse
     * @return
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Warehouse> postWarehouse(@RequestBody Warehouse warehouse){
        try {
            warehouse.setAddress(addressService.setCoordinates(warehouse.getAddress()));
            ShippingAddress warehouseAddress = warehouse.getAddress();
            warehouse = warehouseService.saveWarehouse(warehouse);
            logger.info("New warehouse saved with id: " + warehouse.getId());
            addressService.saveWarehouseAddress(warehouseAddress);
            logger.info("New warehouse address saved with id: " + warehouseAddress.getId());
        } catch (Exception e){
            logger.error("Error while saving new warehouse: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{warehouseId}")
                .buildAndExpand(warehouse.getId()).toUri();

        return ResponseEntity.created(location).body(warehouse);
    }

    /**
     * Cody written by Daniel Locher & copied form Tibor Haller
     * @param
     * @param warehouseId
     * @return
     */
    @PatchMapping(path = "/{warehouseId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Warehouse> patchWarehouse(@RequestBody Warehouse toBePatchedWarehouse, @PathVariable(value = "warehouseId") String warehouseId){
        Warehouse patchedWarehouse;
        ShippingAddress patchedAddress;
        try {
            Warehouse currentWarehouse = warehouseService.findWarehouseById(Long.parseLong(warehouseId));
            patchedAddress = addressService.patchAddress(toBePatchedWarehouse.getAddress(), currentWarehouse.getAddress());
            logger.info("Address updated with id: " + patchedAddress.getId());
            toBePatchedWarehouse.setAddress(patchedAddress);
            patchedWarehouse = warehouseService.patchWarehouse(toBePatchedWarehouse, currentWarehouse);
            logger.info("Warehouse updated with id: " + patchedWarehouse.getId());
        } catch (Exception e){
            logger.error("Error while updating warehouse: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(patchedWarehouse);
    }

    /**
     * Cody by Daniel Locher
     * @return
     */
    @GetMapping(path = "/all", produces = "application/json")
    public List<Warehouse> getAllWarehouses(){
        return warehouseService.findAllWarehouses();
    }

    /**
     * Code by Daniel Locher
     * @param warehouseId
     * @return
     */
    @GetMapping(path = "/{warehouseId}")
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable(value = "warehouseId")String warehouseId){
        Warehouse warehouse;
        try {
            warehouse = warehouseService.findWarehouseById(Long.parseLong(warehouseId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(warehouse);
    }

    /**
     * Code by Daniel Locher
     * @param warehouseId
     * @return
     */
    @DeleteMapping(path = "/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable(value = "warehouseId") String warehouseId) {
        try {
            warehouseService.deleteWarehouse(Long.parseLong(warehouseId));
            logger.info("Warehouse deleted with id: " + warehouseId);
        } catch (Exception e) {
            logger.error("Error while deleting warehouse: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }


}
