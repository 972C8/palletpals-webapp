package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Code by: Tibor Haller
 */
@Repository
public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Long> {
    ShoppingSession findByUserId(Long userId);
}