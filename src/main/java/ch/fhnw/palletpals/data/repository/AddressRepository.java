package ch.fhnw.palletpals.data.repository;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<ShippingAddress, Long> {
    ShippingAddress findByUser(User user);
}
