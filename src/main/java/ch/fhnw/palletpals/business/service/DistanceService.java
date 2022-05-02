package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.*;
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

    /**
     * Code by Daniel Locher
     * This method takes the destination address of the customer and finds the nearest warehouse and returns a coordinate
     * object which holds the distance and the reference to this warehouse
     * @param end
     * @return
     * @throws Exception
     */
    public Coordinate nearestWarehouse(ShippingAddress end)throws Exception{
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Coordinate nearest = null;
        Coordinate endCoordinate;
        double distance;
        //Get coordinates for warehouse addresses and mark them as origins
        try {
            for (Warehouse item: warehouseService.findAllWarehouses()) {
                Coordinate coordinate = getCoordinate(item.getAddress());
                coordinate.setCoordinateType(CoordinateType.ORIGIN);
                coordinates.add(coordinate);
            }
            //Get coordinate for customer address, mark it as the destination and add to array
            endCoordinate = getCoordinate(end);
            endCoordinate.setCoordinateType(CoordinateType.DESTINATION);
            coordinates.add(endCoordinate);
            //Get the distances from each warehouse to destination address
            getDistancesInKm(coordinates);
            //Get the warehouse which is nearest to the destination address
            nearest = getNearestDistance(coordinates);

            //Ensure that nearestWarehouse is never null
            if (nearest == null) {
                throw new Exception("NearestWarehouse cannot be null");
            }
        } catch (Exception e){
            //Permit server to continue running if DistanceService experiences an issue while calculating the distance
            System.out.println("DistanceService.java experienced an issue:" + e.getMessage());
        }
        return nearest;
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
    private Coordinate getCoordinate(ShippingAddress address) throws Exception{
        Coordinate coordinate;
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
            coordinate = new Coordinate(location.getString("lat"),location.getString("lng"), address.getId());
            client.close();
        } catch (Exception e){
            throw new Exception("Request for coordinates failed");
        }
        return coordinate;
    }

    /**
     * Code by Daniel Locher
     * Method to get distances form different warehouses to the client address
     * Resources:
     * https://rapidapi.com/trueway/api/trueway-matrix/
     * @param coordinates
     * @return
     * @throws Exception
     */
    private ArrayList<Coordinate> getDistancesInKm(ArrayList<Coordinate> coordinates) throws Exception{
        String originString;
        String destinationString;
        String distanceString;

        //Last element of coordinates array is always the destination
        //Assign coordinates as string to variable and remove from array
        destinationString = coordinates.get(coordinates.size() - 1).getPathString();
        coordinates.remove(coordinates.size() - 1);

        //The remaining coordinates are all origins and warehouse addresses
        //Build the path string with the remaining coordinates
        ArrayList<String> pathStrings = new ArrayList<String>();
        for(Coordinate item : coordinates){
            pathStrings.add(item.getPathString());
        }
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

        ListenableFuture<Response> responseFuture = client.executeRequest(request);
        JSONObject jsonObject = new JSONObject(responseFuture.get().getResponseBody());
        JSONArray jsonArray = jsonObject.getJSONArray("distances");
        //Assign distances in meter to corresponding coordinates and convert to km
        for (int i = 0; i<jsonArray.length(); i++) {
            JSONArray distanceArray = jsonArray.getJSONArray(i);
            distanceString = distanceArray.getString(0);
            double distance = Double.parseDouble(distanceString);
            distance /= 1000;
            coordinates.get(i).setDistanceToDestinationInKM(distance);
        }

        client.close();

        } catch (Exception e){
            throw new Exception("Request for distance failed");
        }

        return coordinates;
    }
    //TODO think of case when distance is same
    public Coordinate getNearestDistance(ArrayList<Coordinate> coordinates){
        Coordinate nearest=null;
        for(Coordinate c : coordinates){
            if (nearest==null){
                nearest = c;
            }
            if (c.getDistanceToDestinationInKM()<nearest.getDistanceToDestinationInKM()){
                nearest=c;
            }
        }
        return nearest;
    }
}
