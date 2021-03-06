package ch.fhnw.palletpals.data.repository;
import ch.fhnw.palletpals.data.domain.order.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
