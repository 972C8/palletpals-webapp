package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Code by: Tibor Haller
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long productId);
    List<Product> findAll();
}