package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PalletSpaceService {

    public int getPalletSpace(ArrayList<CartItem> cartItems) throws Exception {
        double pallets = 0;
        double usedPallets = 0;
        int index;

        try {
            //Sort out full pallet spaces
            for (CartItem cartItem : cartItems) {
                while (cartItem.getQuantity()>=cartItem.getProduct().getMaxProducts()){
                    pallets += cartItem.getProduct().getMinPalletSpace();
                    usedPallets += cartItem.getProduct().getMinPalletSpace();
                    //TODO check with Tibor if using int instead of float makes sense
                    cartItem.setQuantity(cartItem.getQuantity()- Math.round(cartItem.getProduct().getMaxProducts()));
                }
            }

            //Sort the list of cart items according to the minimum pallet space required
            cartItems = orderItemsByMinPallet(cartItems);

            for (CartItem cartItem : cartItems){
                if (cartItem.getQuantity()>0){
                    //Find product with the biggest min pallet space
                    index = getNextProduct(cartItems);
                    pallets += cartItems.get(index).getProduct().getMinPalletSpace();
                    usedPallets += getProportion(cartItems.get(index));
                    cartItems.get(index).setQuantity(0);

                    //Find best fitting products to minimize remaining space (output array cartitems with quantity)
                    while (unusedSpace(cartItems, pallets, usedPallets)){
                        //break when full
                        for (CartItem cartItemItem : cartItems){
                            //check if has space, if not round up and add another pallet
                            if (cartItemItem.getQuantity()>0){
                                CartItem spaceItem = new CartItem();
                                spaceItem.setProduct(cartItemItem.getProduct());
                                spaceItem.setQuantity(0);
                                if (checkForAdditionalSpace(spaceItem, pallets, usedPallets)){
                                    while (spaceItem.getQuantity()<cartItemItem.getQuantity() && checkForAdditionalSpace(spaceItem, pallets, usedPallets)){
                                        spaceItem.setQuantity(spaceItem.getQuantity()+1);
                                    }
                                    cartItemItem.setQuantity(cartItemItem.getQuantity()-spaceItem.getQuantity());
                                    usedPallets += getProportion(spaceItem);
                                }
                            }
                        }
                    }
                    //round up
                    usedPallets = pallets;
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


        //Test
        for (CartItem cartItem : cartItems){
            System.out.println(cartItem.getProduct().getName() + ": " + cartItem.getQuantity());
        }
        System.out.println(pallets);
        System.out.println(usedPallets);
        return 0;
    }

    private boolean unusedSpace(ArrayList<CartItem> cartItems, double pallets, double usedPallets) throws Exception{
        boolean unusedSpace = false;
        try {
            for (CartItem cartItem : cartItems){
                if (cartItem.getQuantity()>0){
                    if ((1/cartItem.getProduct().getMaxProducts())*cartItem.getProduct().getMinPalletSpace()<= (pallets-usedPallets)){
                        unusedSpace = true;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Checking for unused space failed");
        }
        return unusedSpace;
    }

    private boolean checkForAdditionalSpace(CartItem cartItem, double pallets, double usedPallets) throws Exception{
        CartItem checkItem = new CartItem();
        boolean hasSpace = false;
        try {
            checkItem.setProduct(cartItem.getProduct());
            checkItem.setQuantity(cartItem.getQuantity()+1);
                if (getProportion(checkItem) <= (pallets - usedPallets)){
                    hasSpace = true;
                }
        } catch (Exception e) {
            throw new Exception("Checking for additional space failed");
        }
        return hasSpace;
    }

    //TODO check with Tibor to implement in class Cartitem
    private double getProportion(CartItem cartItem){
        return ((cartItem.getQuantity()/cartItem.getProduct().getMaxProducts())*cartItem.getProduct().getMinPalletSpace());
    }

    private ArrayList<CartItem> orderItemsByMinPallet(ArrayList<CartItem> cartItems) throws Exception{
        ArrayList<CartItem> orderItems = new ArrayList<CartItem>();
        CartItem nextBiggestItem = null;
        try {
            while (cartItems.size()>0){
                for (CartItem cartItem : cartItems){
                    if (nextBiggestItem == null){
                        nextBiggestItem = cartItem;
                    } else if (cartItem.getProduct().getMinPalletSpace()>nextBiggestItem.getProduct().getMinPalletSpace()){
                        nextBiggestItem = cartItem;
                    }
                }
                orderItems.add(nextBiggestItem);
                cartItems.remove(nextBiggestItem);
                nextBiggestItem = null;
            }
        } catch (Exception e) {
            throw new Exception("Ordering of cart items failed");
        }
        return orderItems;
    }

    //TODO exception if index still null
    private int getNextProduct(ArrayList<CartItem> cartItems) throws Exception{
        Integer index = null;
        try {
            for (CartItem cartItem : cartItems){
                if (cartItem.getQuantity()>0){
                    if (index == null){
                        index = cartItems.indexOf(cartItem);
                    } else {
                        if (cartItem.getProduct().getMinPalletSpace()>
                        cartItems.get(index).getProduct().getMinPalletSpace()){
                            index = cartItems.indexOf(cartItem);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Can't get next product");
        }
        return index;
    }

}
