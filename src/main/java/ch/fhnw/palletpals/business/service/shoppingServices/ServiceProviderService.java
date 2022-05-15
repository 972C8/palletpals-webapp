package ch.fhnw.palletpals.business.service.shoppingServices;

import ch.fhnw.palletpals.data.domain.ServiceProvider;
import ch.fhnw.palletpals.data.domain.shopping.ShoppingSession;
import ch.fhnw.palletpals.data.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class ServiceProviderService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    /**
     * Code by Daniel Locher
     * @param serviceProviderString
     * @return
     * @throws Exception
     */
    public ServiceProvider saveServiceProvider(String serviceProviderString) throws Exception{
        ServiceProvider serviceProvider = new ServiceProvider();
        try {
            JSONObject jsonObject = new JSONObject(serviceProviderString);
            serviceProvider.setName(jsonObject.getString("name"));
            serviceProvider.setJSONString(serviceProviderString);
            serviceProvider = serviceProviderRepository.save(serviceProvider);
        } catch (Exception e){
            throw new Exception("Service Provider can't be saved");
        }

        return serviceProvider;
    }

    public ShoppingSession setCheapestShipment(ShoppingSession shoppingSession) throws Exception {
        List<ServiceProvider> allServiceProviders;
        Float shippingCost = null;
        try {
            allServiceProviders = serviceProviderRepository.findAll();
            int count = 0;
            for (ServiceProvider serviceProvider: allServiceProviders){
                shippingCost = getShippingPrice(serviceProvider, shoppingSession.getPalletSpace(), shoppingSession.getDrivingDistance());
                if (count == 0 || shippingCost<shoppingSession.getShippingCost()){
                    shoppingSession.setServiceProvider(serviceProvider.getId());
                    shoppingSession.setShippingCost(shippingCost);
                }
                count ++;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return shoppingSession;
    }

    /**
     * Code by Daniel Locher
     * Method to get shipping price from price table of a defined service provider
     * @param serviceProvider
     * @param palletSpace
     * @param km
     * @return
     * @throws Exception
     */
    private Float getShippingPrice(ServiceProvider serviceProvider, int palletSpace, double km)throws Exception {
        ArrayList<Integer> kmArray;
        ArrayList<Integer> palletArray;
        ArrayList<ArrayList<Float>> priceMatrix;
        int kmIndex = 0;
        int palletIndex = 0;
        //build price table
        try{
            kmArray = buildKmArray(serviceProvider);
            palletArray = buildPalletArray(serviceProvider);
            priceMatrix = buildPriceMatrix(serviceProvider);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        //get index of km of pallet
        while (km>kmArray.get(kmIndex)){
            kmIndex++;
        }
        while (palletSpace>palletArray.get(palletIndex)){
            palletIndex++;
        }
        return priceMatrix.get(kmIndex).get(palletIndex);

    }

    /**
     * Code by Daniel Locher
     * Because spring entities can't hold containers the table is saved as a json string
     * This methods builds the table out of the json string
     * @param serviceProvider
     * @return
     * @throws Exception
     */
    private ArrayList<Integer> buildKmArray(ServiceProvider serviceProvider)throws Exception{
        ArrayList<Integer> kmArray = new ArrayList<Integer>();
        try{
            JSONObject jsonObject = new JSONObject(serviceProvider.getJSONString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("kmArray"));
            for(int i = 0; i<jsonArray.length(); i++){
                String string = jsonArray.getString(i);
                kmArray.add(Integer.parseInt(string));
            }
        } catch (Exception e){
            throw new Exception("Can't build KM array");
        }
        return kmArray;
    }

    /**
     * Code by Daniel Locher
     * Because spring entities can't hold containers the table is saved as a json string
     * This methods builds the table out of the json string
     * @param serviceProvider
     * @return
     * @throws Exception
     */
    private ArrayList<Integer> buildPalletArray(ServiceProvider serviceProvider)throws Exception{
        ArrayList<Integer> palletArray = new ArrayList<Integer>();
        try{
            JSONObject jsonObject = new JSONObject(serviceProvider.getJSONString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("palletArray"));
            for(int i = 0; i<jsonArray.length(); i++){
                String string = jsonArray.getString(i);
                palletArray.add(Integer.parseInt(string));
            }
        } catch (Exception e){
            throw new Exception("Can't build Pallet array");
        }
        return palletArray;
    }

    /**
     * Code by Daniel Locher
     * Because spring entities can't hold containers the table is saved as a json string
     * This methods builds the table out of the json string
     * @param serviceProvider
     * @return
     * @throws Exception
     */
    private ArrayList<ArrayList<Float>> buildPriceMatrix(ServiceProvider serviceProvider)throws Exception{
        ArrayList<ArrayList<Float>> priceMatrix = new ArrayList<ArrayList<Float>>();
        try{
            JSONObject jsonObject = new JSONObject(serviceProvider.getJSONString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("priceMatrix"));
            for(int i = 0; i<jsonArray.length(); i++){
                JSONArray priceArray = jsonArray.getJSONArray(i);
                ArrayList<Float> prices = new ArrayList<>();
                for(int index = 0; index<priceArray.length(); index++){
                    String string = priceArray.getString(index);
                    prices.add(Float.parseFloat(string));
                }
                priceMatrix.add(prices);
            }
        } catch (Exception e){
            throw new Exception("Can't build Pallet array");
        }
        return priceMatrix;
    }


    public List<ServiceProvider> getServiceProviders(){
        return serviceProviderRepository.findAll();
    }

}
