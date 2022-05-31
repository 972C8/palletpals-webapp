package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.order.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Code by: Tibor Haller
 */
@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    UserOrder findUserOrderById(Long orderId);
    List<UserOrder> findAllByOrderByIdDesc();
}