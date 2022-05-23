package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostPersist;

@Service
public class ShippingCostListener {

    @Autowired
    private ShippingCostService shippingCostService;


}
