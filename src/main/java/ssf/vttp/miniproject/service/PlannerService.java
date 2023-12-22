package ssf.vttp.miniproject.service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.shopping.Activity;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Recommendation;


@Service
public class PlannerService {

    @Value("${api.key}")
    private String API_KEY;
 
    @Value("${api.secret}")
    private String API_SECRET;

    List<City> cities = new LinkedList<>();

    public List<City> getAmadeusPage(String cityCodes) throws ResponseException {

        //GET https://test.api.amadeus.com/v1/reference-data/recommended-locations?cityCodes=PAR

        Amadeus amadeus = Amadeus
        .builder(API_KEY, API_SECRET)
        .build();

        com.amadeus.resources.Location[] destinations = amadeus.referenceData.recommendedLocations.get(Params
                                                        .with("cityCodes", cityCodes));
                                                        
        String payload = destinations[0].getResponse().getBody();
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonArray array = reader.readObject().getJsonArray("data");

        for(JsonValue value : array) {
            JsonObject obj = value.asJsonObject();
            String name = obj.getString("name", "");
            Float latitude = Float.valueOf(obj.getJsonObject("geoCode").get("latitude").toString());
            Float longitude = Float.valueOf(obj.getJsonObject("geoCode").get("longitude").toString());
            cities.add(new City(name,latitude,longitude));
        }
            return cities;
        }



        //how to connect the latitude and longitude from AmadeusMethod?

        public List<Recommendation> getRecommendationPage(Float latitude, Float longitude) throws ResponseException {

            
             Amadeus amadeus = Amadeus
            .builder(API_KEY, API_SECRET)
            .build();

            com.amadeus.resources.Activity[] activities = amadeus.shopping.activities.get(Params
            .with("latitude", latitude)
            .and("longitude", longitude));

            String payload = activities[0].getResponse().getBody();
            System.out.println(activities); // to test?

            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonArray array = reader.readObject().getJsonArray("data");

            List<Recommendation> reccs = new LinkedList<>();

            for(JsonValue value : array){
                JsonObject obj = value.asJsonObject();
                 String name = obj.getString("name","No Name Available");
                 String description = obj.getString("description","No description");
                 Float amount = Float.valueOf(obj.getJsonObject("price").get("amount").toString());
                 String currencyCode = obj.getJsonObject("price").getString("currencyCode","");
                 String pictures = obj.getString("pictures","No Pictures Available");
                 String bookingLink = obj.getString("bookingLink", "No Booking Link Available");
                 reccs.add(new Recommendation(name, description, amount, currencyCode, pictures, bookingLink));
            }
            return reccs;
        }


        public List<Recommendation> getCompletePage() throws ResponseException {

             List<Recommendation> reccs = new LinkedList<>();

            for (City c : cities){
                Float latitude2 = c.getLatitude();
                Float longitude2 = c.getLongitude();

               return getRecommendationPage(latitude2, longitude2);   
            } 
        return reccs; //this list is empty
        }



   




    }
    
