package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.Warehouse;
import ch.fhnw.palletpals.data.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    /**
     * Code by Daniel Locher
     * @param warehouse
     * @return
     */
    //TODO check if warehouse/address already exists relevant?
    public Warehouse saveWarehouse(@Valid Warehouse warehouse) throws Exception{
        try {
            return warehouseRepository.save(warehouse);
        } catch (Exception e){
            throw new Exception("Warehouse can't be saved");
        }
    }
}
