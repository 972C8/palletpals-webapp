package ch.fhnw.palletpals.data.repository;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import ch.fhnw.palletpals.data.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<ShippingAddress, Long> {
    ShippingAddress findShippingAddressById(Long id);
}
