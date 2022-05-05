package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Product;
import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PalletSpaceService {

    public int getPalletSpace(ArrayList<CartItem> cartItems) throws Exception {
        double pallets = 0;
        double usedPallets = 0;

        for (CartItem cartItem : cartItems) {
            while (cartItem.getQuantity()>=cartItem.getProduct().getMaxProducts()){
                pallets += cartItem.getProduct().getMinPalletSpace();
                usedPallets += cartItem.getProduct().getMinPalletSpace();
                //TODO check with Tibor if using int instead of float makes sense
                cartItem.setQuantity(cartItem.getQuantity()- Math.round(cartItem.getProduct().getMaxProducts()));
            }
        }

        while (productsLeft(cartItems)){
            while (unusedSpace(cartItems, pallets, usedPallets)){
            for (CartItem cartItem : cartItems){
                if (cartItem.getQuantity()>0){
                    if (checkforSpace(cartItem, pallets, usedPallets)){
                        usedPallets += (cartItem.getProduct().getMinPalletSpace()*(cartItem.getQuantity()/ cartItem.getProduct().getMinPalletSpace()));
                        cartItem.setQuantity(0);
                    }
                }
            }
            }
            for (CartItem cartItem : cartItems){
                if (cartItem.getQuantity()>0){
                    pallets += cartItem.getProduct().getMinPalletSpace();
                    usedPallets += getUsedSpace(cartItem);
                    cartItem.setQuantity(0);
                    break;
                }
            }
        }

        //Test
        for (CartItem cartItem : cartItems){
            System.out.println(cartItem.getProduct().getName() + ": " + cartItem.getQuantity());
        }
        System.out.println(pallets);
        System.out.println(usedPallets);
        return 0;
    }

    private boolean productsLeft(ArrayList<CartItem> cartItems) {
        boolean productsLeft = false;
        for (CartItem cartItem : cartItems){
            if (cartItem.getQuantity()>0){
                productsLeft = true;
            }
        }
        return productsLeft;
    }

    private boolean unusedSpace(ArrayList<CartItem> cartItems, double pallets, double usedPallets){
        boolean unusedSpace = false;
        for (CartItem cartItem : cartItems){
            if (checkforSpace(cartItem, pallets, usedPallets)){
                unusedSpace = true;
            }
        }
        return unusedSpace;
    }

    private boolean checkforSpace(CartItem cartItem, double pallets, double usedPallets){
        boolean hasSpace = false;
            if (cartItem.getProduct().getMinPalletSpace()<= (pallets - usedPallets)){
                hasSpace = true;
            }
        return hasSpace;
    }

    private double getUsedSpace(CartItem cartItem){
        return ((cartItem.getQuantity()/cartItem.getProduct().getMaxProducts())*cartItem.getProduct().getMinPalletSpace());
    }
}
