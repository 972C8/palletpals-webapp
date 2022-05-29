package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.shoppingServices.ServiceProviderService;
import ch.fhnw.palletpals.data.domain.ServiceProvider;
import ch.fhnw.palletpals.data.domain.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/serviceprovider")
public class ServiceProviderEndpoint {

    @Autowired
    private ServiceProviderService serviceProviderService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(ServiceProviderEndpoint.class);

    /**
     * Code by Daniel Locher
     * @param serviceProviderString
     * @return
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ServiceProvider> postServiceProvider(@RequestBody String serviceProviderString){
        ServiceProvider serviceProvider;
        try {
            serviceProvider = serviceProviderService.saveServiceProvider(serviceProviderString);
            logger.info("Service provider saved with id: " + serviceProvider.getId());
        }catch (Exception e){
            logger.error("Error while saving service provider: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{serviceProviderId}")
                .buildAndExpand(serviceProvider.getId()).toUri();

        return ResponseEntity.created(location).body(serviceProvider);
    }
    @PatchMapping(path="/{serviceProviderId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ServiceProvider> patchServiceProvider(@RequestBody Map<String, String> serviceProviderPatch, @PathVariable(value = "serviceProviderId") String serviceProviderId){
        ServiceProvider patchedServiceProvider;
        try{
            ServiceProvider currentServiceProvider = serviceProviderService.findServiceProviderById(Long.parseLong(serviceProviderId));

            ServiceProvider toBePatchedServiceProvider = objectMapper.convertValue(serviceProviderPatch, ServiceProvider.class);

            patchedServiceProvider = serviceProviderService.patchServiceProvider(toBePatchedServiceProvider, currentServiceProvider);
            logger.info("Service provider patched with id: " + patchedServiceProvider.getId());
        } catch (Exception e){
            logger.error("Error while patching service provider with id " + serviceProviderId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(patchedServiceProvider);
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<ServiceProvider> getAllServiceProviders(){return serviceProviderService.findAllServiceProviders();}

    @GetMapping(path = "/{serviceProverId}")
    public ResponseEntity<ServiceProvider> getWarehouse(@PathVariable(value = "serviceProverId")String serviceProviderId){
        ServiceProvider serviceProvider;
        try {
            serviceProvider = serviceProviderService.findServiceProviderById(Long.parseLong(serviceProviderId));
        } catch (Exception e) {
            logger.error("Error while getting service provider with id: " + serviceProviderId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(serviceProvider);
    }

    @DeleteMapping(path = "/{serviceProviderId}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable(value = "serviceProviderId") String serviceProviderId) {
        try {
            serviceProviderService.deleteServiceProvider(Long.parseLong(serviceProviderId));
            logger.info("Service provider deleted with id: " + serviceProviderId);
        } catch (Exception e) {
            logger.error("Error while deleting service provider with id " + serviceProviderId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }
}
