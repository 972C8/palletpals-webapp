package ch.fhnw.palletpals.data.repository;

import ch.fhnw.palletpals.data.domain.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    ServiceProvider findServiceProviderById(Long id);
}
