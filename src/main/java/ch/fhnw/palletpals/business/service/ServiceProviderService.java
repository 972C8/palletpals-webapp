package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.ServiceProvider;
import ch.fhnw.palletpals.data.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class ServiceProviderService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    public ServiceProvider saveServiceProvider(@Valid ServiceProvider serviceProvider) throws Exception{
        try {
            serviceProvider = serviceProviderRepository.save(serviceProvider);
        } catch (Exception e){
            throw new Exception("Service Provider can't be saved");
        }
        return serviceProvider;
    }

}
