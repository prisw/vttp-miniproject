package ssf.vttp.miniproject.service;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Itinerary;
import ssf.vttp.miniproject.models.Recommendation;
import ssf.vttp.miniproject.repository.PlannerRepository;


@Service
public class PlannerService extends Exception{

    private Logger logger = Logger.getLogger(PlannerService.class.getName());

    @Autowired
    PlannerRepository planRepo;

    @Autowired
    Amadeus amadeus;


    public List<City> getAmadeusPage(String cityCodes) throws ResponseException, CustomResponseException {
         List<City> cities = new LinkedList<>();


        //GET https://test.api.amadeus.com/v1/reference-data/recommended-locations?cityCodes=PAR

        try {

        com.amadeus.resources.Location[] destinations = amadeus.referenceData.recommendedLocations.get(Params
                                                        .with("cityCodes", cityCodes));
                                                        
        String payload = destinations[0].getResponse().getBody();

        // logger.log(Level.INFO, "Amadeus API response payload: {0}", payload);

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonArray array = reader.readObject().getJsonArray("data");

        for(JsonValue value : array) {
            JsonObject obj = value.asJsonObject();
            String name = obj.getString("name", "");
            Float latitude = Float.valueOf(obj.getJsonObject("geoCode").get("latitude").toString());
            Float longitude = Float.valueOf(obj.getJsonObject("geoCode").get("longitude").toString());
            City city = new City(name,latitude,longitude,null);
            city.setRecommendations(this.getRecommendationPage(city));
            cities.add(city);
            System.out.println(city);
            Thread.sleep(100);
        }

        logger.log(Level.INFO, "Retrieved {0} cities from Amadeus API", cities.size());
        return cities;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while fetching Amadeus data", e);
            throw new CustomResponseException("Error fetching Amadeus data. Please try again later.");
    }
}



        //how to connect the latitude and longitude from AmadeusMethod?

        private List<Recommendation> getRecommendationPage(City city) throws ResponseException {

           try { 
            logger.log(Level.INFO, "Fetching recommendations for latitude: {0}, longitude: {1}", new Object[]{city.getLatitude(), city.getLongitude()});

            com.amadeus.resources.Activity[] activities = amadeus.shopping.activities.get(Params
            .with("latitude", city.getLatitude())
            .and("longitude", city.getLongitude()));

            List<Recommendation> reccs = new LinkedList<>();


            if(activities !=null && activities.length > 0){
                String payload = activities[0].getResponse().getBody();
                JsonReader reader = Json.createReader(new StringReader(payload));
                JsonArray array = reader.readObject().getJsonArray("data");
                

                // logger.log(Level.INFO, "Amadeus API response payload: {0}", payload);
                logger.log(Level.INFO, "Size of reccs before adding recommendations: {0}", reccs.size());
                
            for(JsonValue value : array){
                
                logger.log(Level.INFO, "Processing recommendation for latitude: {0}, longitude: {1}", new Object[]{city.getLatitude(), city.getLongitude()});


                JsonObject obj = value.asJsonObject();
                 String name = obj.getString("name","No Name Available");
                 String description = obj.getString("description","No description");
                 Float amount = Float.valueOf(obj.getJsonObject("price").getString("amount","NaN"));
                 String currencyCode = obj.getJsonObject("price").getString("currencyCode","NaN");
                 String pictures = obj.getString("pictures","No Pictures Available");
                 String bookingLink = obj.getString("bookingLink", "No Booking Link Available");
                 reccs.add(new Recommendation(name, description, amount, currencyCode, pictures, bookingLink));
                
                
                 logger.log(Level.INFO, "Size of reccs after adding recommendation: {0}", reccs.size());
            }
 }
            return reccs;

        } catch (ResponseException e) {
            logger.log(Level.SEVERE, "Error fetching Amadeus recommendations", e);
            throw e;
        }
    }



    //     public List<Recommendation> getCompletePage(City city) throws ResponseException {

    //          List<Recommendation> reccs = new LinkedList<>();
    //         Set<Recommendation> uniqueReccs = new HashSet<>();


    //         for (City c : cities){
    //             Float latitude2 = c.getLatitude();
    //             Float longitude2 = c.getLongitude();
    //             List<Recommendation> allReccs = getRecommendationPage(latitude2, longitude2);
    //             reccs.addAll(allReccs);

    //             //for deubugging
    //             System.out.println("City: " + c.getName() + " - Latitude: " + latitude2 + " - Longitude: " + longitude2);
    //             System.out.println("Recommendations for " + c.getName() + ": " + allReccs);


    //             for (Recommendation rec: allReccs){
    //                 if(uniqueReccs.add(rec)) {
    //                     reccs.add(rec);
    //                 } else {
    //                     System.out.println("Duplicate Recommendation: " + rec);;
    //                 }
    //             }
    //     }
    //     System.out.println("Final Size of Recommendations: " + reccs.size());
    //     return reccs; //this list is empty
    // }





    //FOR ITINERARY LIST
    public List<Itinerary> getItineraries(String name) {
        if (planRepo.hasItinerary(name))
        return planRepo.getItineraries(name);
        return new LinkedList<>();
    }


    public void save(String name, Itinerary itinerary) {
        //planRepo.deleteItinerary(name);
        System.out.println("service save");
        planRepo.addItinerary(name, itinerary);
    }

    public String generateId() {

        Random random = new Random();
        Integer id = random.nextInt(10000);
        return id.toString();
    }
    public class CustomResponseException extends Exception {

        public CustomResponseException(String message) {
            super(message);
            
        }
}


}