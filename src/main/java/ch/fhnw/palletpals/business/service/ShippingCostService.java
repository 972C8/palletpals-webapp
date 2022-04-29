package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingCostService {

    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private DistanceService distanceService;

    //TODO change palletspace to shopping cart as soon as class exists
    public double getShippingCosts(User currentUser, int testPalletSpace)throws Exception{
        double distance;
        int palletSpace;

        //Find nearest warehouse and get driving distance with address of current user

        //Calculate palletSpace with shoppingCart (shopping session)

        //Calculate shipping costs

        return 0;
    }

}
