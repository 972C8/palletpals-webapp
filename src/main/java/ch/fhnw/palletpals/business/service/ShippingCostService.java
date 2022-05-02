package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Coordinate;
import ch.fhnw.palletpals.data.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingCostService {

    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private DistanceService distanceService;


    //TODO change input
    public double getShippingCosts(User currentUser)throws Exception{
        Coordinate shortestRoute;
        int palletSpace;

        //Find nearest warehouse and get driving distance with address of current user
        shortestRoute = distanceService.nearestWarehouse(currentUser.getAddress());
        //Calculate palletSpace with shoppingCart (shopping session)
        palletSpace = 4;
        //Calculate shipping costs

        return 0;
    }

}
