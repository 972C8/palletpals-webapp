package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findWarehouseById(Long id);
}
