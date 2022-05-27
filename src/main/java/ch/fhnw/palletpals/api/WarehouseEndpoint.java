package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseEndpoint {
    @Autowired
    private AddressService addressService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            addressService.saveWarehouseAddress(warehouseAddress);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{warehouseId}")
                .buildAndExpand(warehouse.getId()).toUri();

        return ResponseEntity.created(location).body(warehouse);
    }

    /**
     * Cody written by Daniel Locher & copied form Tibor Haller
     * @param warehousePatch
     * @param warehouseId
     * @return
     */
    @PatchMapping(path = "/{warehouseId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Warehouse> patchWarehouse(@RequestBody Map<String, String> warehousePatch, @PathVariable(value = "warehouseId") String warehouseId){
        Warehouse patchedWarehouse;
        try {
            Warehouse currentWarehouse = warehouseService.findWarehouseById(Long.parseLong(warehouseId));

            Warehouse toBePatchedWarehouse = objectMapper.convertValue(warehousePatch, Warehouse.class);

            patchedWarehouse = warehouseService.patchWarehouse(toBePatchedWarehouse, currentWarehouse);
        } catch (Exception e){
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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }


}
