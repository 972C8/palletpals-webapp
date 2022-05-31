package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.business.service.AddressService;
import ch.fhnw.palletpals.business.service.WarehouseService;
import ch.fhnw.palletpals.data.domain.*;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DistanceService {

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    AddressService addressService;

    /**
     * Code by Daniel Locher
     * This method takes the destination address of the customer and finds the nearest warehouse and returns a coordinate
     * object which holds the distance and the reference to this warehouse
     * @param shoppingSession
     * @return
     * @throws Exception
     */
    public ShoppingSession setNearestWarehouse(ShoppingSession shoppingSession)throws Exception{
        try {
            //Get the distances from each warehouse to destination address and assign values to shopping session object
            shoppingSession = getDistancesInKm(shoppingSession);
        } catch (Exception e){
            //Permit server to continue running if DistanceService experiences an issue while calculating the distance
            throw new Exception("DistanceService.java experienced an issue:" + e.getMessage());
        }
        return shoppingSession;
    }

    /**
     * Code by Daniel Locher
     * Method to get distances form different warehouses to the client address
     * Resources:
     * https://rapidapi.com/trueway/api/trueway-matrix/
     * @param shoppingSession
     * @return
     * @throws Exception
     */
    private ShoppingSession getDistancesInKm(ShoppingSession shoppingSession) throws Exception{
        String originString;
        String destinationString;
        String distanceString;
        Float shotestDistance = null;
        Integer warehouseIndex = null;

        //Assign destination coordinates from shopping session as string to variable
        destinationString = shoppingSession.getUser().getAddress().distanceRequest();

        //Build the origin string with all warehouses
        //First put all Strings separately in the Array
        ArrayList<String> pathStrings = new ArrayList<String>();
        ArrayList<Warehouse> allWarehouses = (ArrayList) warehouseService.findAllWarehouses();
        for(Warehouse item : allWarehouses){
            pathStrings.add(item.getAddress().distanceRequest());
        }
        //Then combine all strings into one and separate them with ;
        originString = String.join(";", pathStrings);

        //Sending the request to API to get distances between orginis and destination
        try {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        Request request = new RequestBuilder(HttpConstants.Methods.GET)
                .setUrl("https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins="
                        + originString + "&destinations=" + destinationString)
                .setHeader("X-RapidAPI-Host", "trueway-matrix.p.rapidapi.com")
                .setHeader("X-RapidAPI-Key", "a3ed38eadamsh9968ee50089fa4cp1c5264jsn6093f98e35ce")
                .build();
        //Assign respons from Api to a jsonArray so we can loop over the results
        ListenableFuture<Response> responseFuture = client.executeRequest(request);
        JSONObject jsonObject = new JSONObject(responseFuture.get().getResponseBody());
        JSONArray jsonArray = jsonObject.getJSONArray("distances");
        //Assign distances in meter to corresponding coordinates and convert to km
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONArray distanceArray = jsonArray.getJSONArray(i);
            distanceString = distanceArray.getString(0);
            Float distance = Float.parseFloat(distanceString);
            distance /= 1000;
            if (shotestDistance==null ||distance<shotestDistance){
                shotestDistance = distance;
                warehouseIndex = i;
            }
        }
        client.close();
        if (shotestDistance!=null&&warehouseIndex!=null){
            shoppingSession.setDrivingDistance(shotestDistance);
            shoppingSession.setNearestWarehouse(allWarehouses.get(warehouseIndex).getId());
        } else {
            throw new Exception("Shortest distance is null");
        }
        } catch (Exception e){
            throw new Exception("Api request for distance failed");
        }

        return shoppingSession;
    }
}
