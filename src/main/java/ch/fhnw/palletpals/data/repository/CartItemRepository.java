package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.shopping.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Code by: Tibor Haller
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findCartItemById(Long cartItemId);
    CartItem findCartItemByShoppingSessionIdAndProductId(Long shoppingId, Long productId);
}