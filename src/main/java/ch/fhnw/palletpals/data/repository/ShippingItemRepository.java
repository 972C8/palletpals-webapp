package ch.fhnw.palletpals.data.repository;
import ch.fhnw.palletpals.data.domain.order.ShippingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingItemRepository extends JpaRepository<ShippingItem, Long> {
}
