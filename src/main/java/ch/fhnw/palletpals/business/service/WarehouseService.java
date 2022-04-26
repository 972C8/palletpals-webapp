package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Warehouse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class WarehouseService {

    /**
     * Code by Daniel Locher
     * @param warehouse
     * @return
     */
    public Warehouse saveWarehouse(@Valid Warehouse warehouse){
        return null;
    }
}
