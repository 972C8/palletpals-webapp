package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.business.service.ShoppingService;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;

@Service
 public class ShippingCostService {

    @Autowired
    private ServiceProviderService serviceProviderService = new ServiceProviderService();
    @Autowired
    private DistanceService distanceService;
    @Autowired
    private PalletSpaceService palletSpaceService;
    @Autowired
    private ShoppingService shoppingService;

    public void getShippingCosts(ShoppingSession shoppingSession)throws Exception{
        try {
        //Find the nearest warehouse and get driving distance with address of current user
        shoppingSession = distanceService.setNearestWarehouse(shoppingSession);
        //Calculate palletSpace with shopping session
        shoppingSession = palletSpaceService.setPalletSpace(shoppingSession);
        //Calculate shipping costs
        shoppingSession = serviceProviderService.setCheapestShipment(shoppingSession);
        //save the shopping session with the calculated values
        shoppingService.saveShoppingSession(shoppingSession);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostPersist
    private void afterAnyUpdate(CartItem cartItem) throws Exception {
        getShippingCosts(cartItem.getShoppingSession());
    }

}
