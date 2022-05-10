package ch.fhnw.palletpals.api;

import ch.fhnw.palletpals.business.service.shoppingServices.ServiceProviderService;
import ch.fhnw.palletpals.data.domain.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/serviceprovider")
public class ServiceProviderEndpoint {

    @Autowired
    private ServiceProviderService serviceProviderService;

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
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.ok(serviceProvider);
    }
    //TODO make requests rather Put than Patch as discussed with Tibor
}
