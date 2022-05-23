package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.ShippingAddress;
import ch.fhnw.palletpals.data.repository.AddressRepository;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;

    /**
     * Code by Daniel Locher
     * @param address
     * @return
     * @throws Exception
     */
    public ShippingAddress saveCustomerAddress(@Valid ShippingAddress address) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception("Address couldn't be assigned to user");
        }
        return address;
    }

    /**
     * Code by Daniel Locher
     * @param address
     * @return
     * @throws Exception
     */
    public ShippingAddress saveWarehouseAddress(@Valid ShippingAddress address) throws Exception {
        //Logic to store referenced objects by provided id from JSON
        try {
            address = addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception("Address couldn't be assigned to warehouse");
        }
        return address;
    }

    /**
     * Code by Daniel Locher
     * Method to get coordinates out of full text addresses
     * Resources:
     * https://www.baeldung.com/async-http-client
     * https://rapidapi.com/trueway/api/trueway-geocoding/
     * @param address
     * @return
     * @throws Exception
     */
    public ShippingAddress setCoordinates(ShippingAddress address) throws Exception {
        //Send the request to api to get coordinate of full text address
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Request request = new RequestBuilder(HttpConstants.Methods.GET)
                    .setUrl("https://trueway-geocoding.p.rapidapi.com/Geocode?address="+address.coordinateRequest()+"6&language=en")
                    .setHeader("X-RapidAPI-Host", "trueway-geocoding.p.rapidapi.com")
                    .setHeader("X-RapidAPI-Key", "a3ed38eadamsh9968ee50089fa4cp1c5264jsn6093f98e35ce")
                    .build();

            ListenableFuture<Response> responseFuture = client.executeRequest(request);
            //Get values out of JSON and assign to coordinate object
            JSONObject jsonObject = new JSONObject(responseFuture.get().getResponseBody());
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject location = jsonArray.getJSONObject(0).getJSONObject("location");
            address.setLat(location.getString("lat"));
            address.setLon(location.getString("lng"));
            client.close();
        } catch (Exception e) {
            throw new Exception("Setting Coordinates failed");
        }
        return address;
    }

}
