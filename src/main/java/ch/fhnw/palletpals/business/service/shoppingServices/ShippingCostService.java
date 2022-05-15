package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingCostService {

    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private DistanceService distanceService;
    @Autowired
    private PalletSpaceService palletSpaceService;

    public ShoppingSession getShippingCosts(ShoppingSession shoppingSession)throws Exception{
        try {
        //Find the nearest warehouse and get driving distance with address of current user
        shoppingSession = distanceService.setNearestWarehouse(shoppingSession);
        //Calculate palletSpace with shopping session
        shoppingSession = palletSpaceService.setPalletSpace(shoppingSession);
        //Calculate shipping costs
        shoppingSession = serviceProviderService.setCheapestShipment(shoppingSession);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return shoppingSession;
    }

}
