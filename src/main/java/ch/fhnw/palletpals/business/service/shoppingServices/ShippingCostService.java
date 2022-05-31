package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.shopping.CartItem;
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

    /**
     * Code by Daniel Locher
     * This method takes a shopping session, calculates the driving distance to the nearest warehouse, calculates the pallet space, and selects the service provider with the cheapest rate
     * The returned object contains shipping information (costs, distance, pallet space) it is been linked to the nearest warehouse and the selected service provider
     * @param shoppingSession
     * @return shoppingSession
     * @throws Exception
     */
    public ShoppingSession getShippingCosts(ShoppingSession shoppingSession)throws Exception{
        try {
        //Find the nearest warehouse and get driving distance with address of current user
        shoppingSession = distanceService.setNearestWarehouse(shoppingSession);
        //Calculate palletSpace with shopping session
        shoppingSession = palletSpaceService.setPalletSpace(shoppingSession);
        //Calculate shipping costs
        shoppingSession = serviceProviderService.setCheapestShipment(shoppingSession);
        //Calculate total cost
        shoppingSession = calculateTotalCost(shoppingSession);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return shoppingSession;
    }

    private ShoppingSession calculateTotalCost(ShoppingSession shoppingSession) {
        float totalCost = 0;
        for (CartItem cartItem: shoppingSession.getShoppingCart()){
            totalCost+=cartItem.getQuantity()*cartItem.getPricePerUnit();
        }
        totalCost+=shoppingSession.getShippingCost();
        shoppingSession.setTotalCost(totalCost);
        return shoppingSession;
    }

}
