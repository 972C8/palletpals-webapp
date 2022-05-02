package ch.fhnw.palletpals.data.domain.shopping;

import ch.fhnw.palletpals.data.domain.Product;

import javax.persistence.*;


/**
 * Code by: Tibor Haller
 *
 * CartItem is used to display the shopping cart (with calculations).
 *
 * One User has one ShoppingSession, which consists of one CartItem per product in the shopping cart.
 */
@Entity
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name = "cartID", unique = true, nullable = false)
    private Long id;

    private int quantity;
    private float pricePerUnit;

    //Uni-directional relationship. This means that Product does not know about which CartItems reference it.
    @ManyToOne
    private Product product;
}
