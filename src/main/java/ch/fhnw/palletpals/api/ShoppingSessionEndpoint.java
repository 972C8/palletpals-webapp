package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.shoppingServices.ShoppingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class ShoppingSessionEndpoint {
    @Autowired
    private ShoppingSessionService shoppingSessionService;

    //TODO: ShoppingSessionEndpoint

    //POST new CartItem (quantity, product reference by id -> pricePerUnit is fetched within the system)

    //GET Shopping Cart (all CartItems + calculated shippingCost&totalCost)

    //PATCH CartItem by id (only quantity can be updated)

    //DELETE CartItem by id

}