package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Code by: Tibor Haller
 */
@Repository
public interface ShoppingSessionRepository extends JpaRepository<Product, Long> {
}