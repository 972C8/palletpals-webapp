package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.image.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    ProductImage findProductImageById(Long imageId);
}