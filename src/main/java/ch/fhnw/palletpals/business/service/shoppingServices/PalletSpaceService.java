package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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
                    //Assign pallet spaces to products in order of their min pallet space required
                    pallets += cartItem.getProduct().getMinPalletSpace();
                    usedPallets += getProportion(cartItem);
                    cartItem.setQuantity(0);

                    //As long as there is space to fill in other products in the remaining pallet space the while loop gets executed
                    while (unusedSpace(cartItems, pallets, usedPallets)){
                        //Looping over remaining products to find one which fits best into the remaining space
                        for (CartItem cartItemItem : cartItems){
                            //With the spaceItem we check if there is space available for this product and for how many quantities
                            if (cartItemItem.getQuantity()>0){
                                CartItem spaceItem = new CartItem();
                                spaceItem.setProduct(cartItemItem.getProduct());
                                spaceItem.setQuantity(0);
                                //As long there is space for another item, the while loop increases quantity
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
                    //When there is no product left which fits into the remaining space, used pallet is set equal to
                    //overall pallets used and the loop starts over
                    usedPallets = pallets;
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        //Round to one decimal because sometimes we get large decimals
        //https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
        int scale = (int) Math.pow(10, 1);
        pallets = (double) Math.round(pallets*scale) /scale;

        //TODO remove as soon as tested
        //Test
        for (CartItem cartItem : cartItems){
            System.out.println(cartItem.getProduct().getName() + ": " + cartItem.getQuantity());
        }
        System.out.println(pallets);
        System.out.println(usedPallets);

        //Round pallets up to the next full integer & return it
        return (int) Math.ceil(pallets);
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
