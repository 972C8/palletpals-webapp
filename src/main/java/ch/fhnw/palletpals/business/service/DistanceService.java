package ch.fhnw.palletpals.business.service;

import ch.fhnw.palletpals.data.domain.Coordinate;
import ch.fhnw.palletpals.data.domain.ShippingAddress;
import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpConstants;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistanceService {

    public double distance(ShippingAddress start, ShippingAddress end)throws Exception{
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        double distance;
        try {
            coordinates.add(getCoordinate(start));
            coordinates.add(getCoordinate(end));
            distance = getDistanceInKm(coordinates);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return distance;
    }
    //https://www.baeldung.com/async-http-client
    public Coordinate getCoordinate(ShippingAddress address) throws Exception{
        Coordinate coordinate;
        try {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Request request = new RequestBuilder(HttpConstants.Methods.GET)
                    .setUrl("https://trueway-geocoding.p.rapidapi.com/Geocode?address="+address.coordinateRequest()+"6&language=en")
                    .setHeader("X-RapidAPI-Host", "trueway-geocoding.p.rapidapi.com")
                    .setHeader("X-RapidAPI-Key", "a3ed38eadamsh9968ee50089fa4cp1c5264jsn6093f98e35ce")
                    .build();

            ListenableFuture<Response> responseFuture = client.executeRequest(request);
            JSONObject jsonObject = new JSONObject(responseFuture.get().getResponseBody());
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject location = jsonArray.getJSONObject(0).getJSONObject("location");
            coordinate = new Coordinate(location.getString("lat"),location.getString("lng") );
            client.close();
        } catch (Exception e){
            throw new Exception("Request for coordinates failed");
        }
        return coordinate;
    }

    public double getDistanceInKm(ArrayList<Coordinate> coordinates) throws Exception{
        String distanceString;
        double distance;
        try {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        Request request = new RequestBuilder(HttpConstants.Methods.GET)
                .setUrl("https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins=47.274169%2C%208.166035&destinations=47.38936%2C%208.393617")
                .setHeader("X-RapidAPI-Host", "trueway-matrix.p.rapidapi.com")
                .setHeader("X-RapidAPI-Key", "a3ed38eadamsh9968ee50089fa4cp1c5264jsn6093f98e35ce")
                .build();

        ListenableFuture<Response> responseFuture = client.executeRequest(request);
        JSONObject jsonObject = new JSONObject(responseFuture.get().getResponseBody());
        JSONArray jsonArray = jsonObject.getJSONArray("distances");
        JSONArray distanceArray = jsonArray.getJSONArray(0);
        distanceString = distanceArray.getString(0);

        client.close();

        } catch (Exception e){
            throw new Exception("Request for distance failed");
        }
        distance = Double.parseDouble(distanceString);
        distance = distance / 1000;
        return distance;
    }
}
