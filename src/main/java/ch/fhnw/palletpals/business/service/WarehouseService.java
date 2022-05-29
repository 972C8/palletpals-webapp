package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.component.NullAwareBeanUtilsBean;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.Warehouse;
import ch.fhnw.palletpals.data.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private NullAwareBeanUtilsBean beanUtils = new NullAwareBeanUtilsBean();

    /**
     * Code by Daniel Locher
     * @param warehouse
     * @return
     */
    //TODO check if warehouse/address already exists relevant?
    public Warehouse saveWarehouse(@Valid Warehouse warehouse) throws Exception{
        try {
            warehouse = warehouseRepository.save(warehouse);
        } catch (Exception e){
            throw new Exception("Warehouse can't be saved");
        }
        return warehouse;
    }

    public Warehouse findWarehouseById(Long id)throws Exception{
        Warehouse warehouse = warehouseRepository.findWarehouseById(id);
        if (warehouse == null){
            throw new Exception("No warehouse found with ID: " + id);
        }
        return warehouse;
    }

    public Warehouse patchWarehouse(Warehouse toBePatchedWarehouse, Warehouse currentWarehouse) throws Exception{
        beanUtils.copyProperties(currentWarehouse, toBePatchedWarehouse);
        return warehouseRepository.save(currentWarehouse);
    }

    public List<Warehouse> findAllWarehouses(){
            return warehouseRepository.findAll();
    }

    public void deleteWarehouse(Long warehouseId){warehouseRepository.deleteById(warehouseId);}
}
