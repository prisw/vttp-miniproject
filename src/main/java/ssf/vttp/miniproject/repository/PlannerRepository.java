package ssf.vttp.miniproject.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.servlet.http.HttpSession;
import ssf.vttp.miniproject.models.Itinerary;

@Repository 
public class PlannerRepository {

    private static List<Itinerary> itineraries;
    public static final String ATTR_ITIN ="itinerary";
    public static final Integer F_DAY = 0;
    public static final Integer F_DATE = 1;
    public static final Integer F_LOCATION = 2;
    public static final Integer F_ACTIVTIY = 3;
    public static final Integer F_REMARKS = 4;

    @Autowired @Qualifier("REDIS")
    private RedisTemplate<String,Object> template;


    public static List<Itinerary> getItineraries(HttpSession session) {
        itineraries = (List<Itinerary>)session.getAttribute(ATTR_ITIN);
        if(null== itineraries){
            itineraries = new LinkedList<>();
            session.setAttribute(ATTR_ITIN, itineraries);
        } return itineraries;

      
    }

    public boolean hasItinerary(String name) {
        return template.hasKey(name);
    }

    public void addItinerary(String name, Itinerary itinerary) {
        HashOperations<String,String,Object> map = template.opsForHash();
        System.out.println("saving to redis");
        
        map.put(name,itinerary.getId(), itinerary);
        System.out.println("done");
    }

    public List<Itinerary> getItineraries(String name) {
        HashOperations<String,String,Object> map = template.opsForHash();
        List<Itinerary> itineraries = new LinkedList<>();

        
        Set keyset= map.keys(name);
        for(Object o: keyset){
            Itinerary itinerary = (Itinerary) map.get(name,o.toString());
            itineraries.add(itinerary);
        }
        return itineraries;
    }


    public Itinerary getOneItinerary(String name, String id){
        HashOperations<String, String, Object> map = template.opsForHash();
        return (Itinerary)map.get(name, id);
    }

    public List<Itinerary> getAllItineraries (String name) {
        HashOperations<String,Object,Object> map = template.opsForHash();
        Set<Object> keyset = map.keys(name);

        List<Itinerary> itineraries = new LinkedList<>();
        for (Object o: keyset){
            Itinerary itinerary = (Itinerary) map.get(name, o.toString());
            itineraries.add(itinerary);
        } return itineraries;
    }

    public void deleteItinerary(String name, String id) {
        HashOperations<String,Object,Object> map = template.opsForHash();
        map.delete(name, id);
    }


    public void updateItinerary(Itinerary itinerary, String name) {
        HashOperations<String,Object,Object> map = template.opsForHash();

        List<Itinerary> itineraries = new LinkedList<>();
        map.put(name,itinerary.getId(), itinerary);
    }


    public List<Itinerary> allItineraries(String name){
        HashOperations<String,Object,Object> map = template.opsForHash();

        List<Itinerary> itineraries = new LinkedList<>();

        List<Object> rawList = map.values(name);
        for(Object o : rawList){
            Itinerary itinerary = (Itinerary)o;
            itineraries.add(itinerary);
        }
        return itineraries;
    }



     public Map<String, String> getCityCodes() {

        Map<String,String> cityCodes = new HashMap<>();
        
        //France
        cityCodes.put("CDG", "Paris - Charles de Gaulle Airport, France");
        cityCodes.put("NCE", "Nice, France");
        cityCodes.put("LYS", "Lyon, France");
        cityCodes.put("MRS", "Marseille, France");
        cityCodes.put("BOD", "Bordeaux, France");
        cityCodes.put("TLS", "Toulouse, France");
        cityCodes.put("NTE", "Nantes, France");
        cityCodes.put("SXB", "Strasbourg, France");
        cityCodes.put("MPL", "Montpellier, France");
        cityCodes.put("LIL", "Lille, France");

        //UK
        cityCodes.put("LHR", "London Heathrow, United Kingdom");
        cityCodes.put("MAN", "Manchester, United Kingdom");
        cityCodes.put("BHX", "Birmingham, United Kingdom");
        cityCodes.put("GLA", "Glasgow, United Kingdom");
        cityCodes.put("EDI", "Edinburgh, United Kingdom");
        cityCodes.put("LPL", "Liverpool, United Kingdom");
        cityCodes.put("BRS", "Bristol, United Kingdom");
        cityCodes.put("NCL", "Newcastle, United Kingdom");
        cityCodes.put("BFS", "Belfast, United Kingdom");
        cityCodes.put("CWL", "Cardiff, United Kingdom");

        //Germany
        cityCodes.put("FRA", "Frankfurt, Germany");
        cityCodes.put("MUC", "Munich, Germany");
        cityCodes.put("HAM", "Hamburg, Germany");
        cityCodes.put("DUS", "Düsseldorf, Germany");
        cityCodes.put("CGN", "Cologne, Germany");
        cityCodes.put("STR", "Stuttgart, Germany");
        cityCodes.put("LEJ", "Leipzig, Germany");
        cityCodes.put("NUE", "Nuremberg, Germany");
        cityCodes.put("HAJ", "Hanover, Germany");
        // cityCodes.put("TXL", "Berlin Tegel, Germany");

        //Italy
        cityCodes.put("FCO", "Rome Fiumicino, Italy");
        cityCodes.put("MXP", "Milan Malpensa, Italy");
        cityCodes.put("VCE", "Venice, Italy");
        cityCodes.put("FLR", "Florence, Italy");
        cityCodes.put("NAP", "Naples, Italy");
        cityCodes.put("BLQ", "Bologna, Italy");
        cityCodes.put("TRN", "Turin, Italy");
        cityCodes.put("PMO", "Palermo, Italy");
        cityCodes.put("CTA", "Catania, Italy");
        cityCodes.put("GOA", "Genoa, Italy");


        //Spain
        cityCodes.put("MAD", "Madrid Barajas, Spain");
        cityCodes.put("BCN", "Barcelona El Prat, Spain");
        cityCodes.put("VLC", "Valencia, Spain");
        cityCodes.put("SVQ", "Seville, Spain");
        cityCodes.put("ZAZ", "Zaragoza, Spain");
        cityCodes.put("AGP", "Malaga, Spain");
        cityCodes.put("BIO", "Bilbao, Spain");
        cityCodes.put("ALC", "Alicante, Spain");
        cityCodes.put("PMI", "Palma de Mallorca, Spain");
        cityCodes.put("GRX", "Granada, Spain");

        //Netherlands
        cityCodes.put("AMS", "Amsterdam Schiphol, Netherlands");
        cityCodes.put("RTM", "Rotterdam The Hague, Netherlands");
        cityCodes.put("UTC", "Utrecht, Netherlands");
        cityCodes.put("HAG", "The Hague, Netherlands");
        cityCodes.put("EIN", "Eindhoven, Netherlands");
        cityCodes.put("GRQ", "Groningen, Netherlands");
        cityCodes.put("MST", "Maastricht Aachen, Netherlands");
        cityCodes.put("QAR", "Arnhem, Netherlands");
        cityCodes.put("TIU", "Tilburg, Netherlands");
        cityCodes.put("HRL", "Haarlem, Netherlands");

        //USA
        cityCodes.put("JFK", "New York John F. Kennedy, USA");
        cityCodes.put("LAX", "Los Angeles, USA");
        cityCodes.put("ORD", "Chicago O'Hare, USA");
        cityCodes.put("MIA", "Miami, USA");
        cityCodes.put("SFO", "San Francisco, USA");
        cityCodes.put("LAS", "Las Vegas, USA");
        cityCodes.put("MCO", "Orlando, USA");
        cityCodes.put("ATL", "Atlanta, USA");
        cityCodes.put("SEA", "Seattle, USA");
        cityCodes.put("DFW", "Dallas/Fort Worth, USA");

        //Argentina
        cityCodes.put("EZE", "Buenos Aires Ezeiza, Argentina");
        cityCodes.put("COR", "Cordoba, Argentina");
        cityCodes.put("ROS", "Rosario, Argentina");
        cityCodes.put("MDZ", "Mendoza, Argentina");
        cityCodes.put("SLA", "Salta, Argentina");
        cityCodes.put("TUC", "Tucuman, Argentina");
        cityCodes.put("MDQ", "Mar del Plata, Argentina");
        cityCodes.put("NQN", "Neuquen, Argentina");
        cityCodes.put("BHI", "Bahia Blanca, Argentina");
        cityCodes.put("UAQ", "San Juan, Argentina");

        //Mexico
        cityCodes.put("MEX", "Mexico City Benito Juárez, Mexico");
        cityCodes.put("CUN", "Cancun, Mexico");
        cityCodes.put("GDL", "Guadalajara, Mexico");
        cityCodes.put("MTY", "Monterrey, Mexico");

        //Saudi Arabia
        cityCodes.put("RUH", "Riyadh King Khalid, Saudi Arabia");
        cityCodes.put("JED", "Jeddah King Abdulaziz, Saudi Arabia");
        cityCodes.put("DMM", "Dammam King Fahd, Saudi Arabia");
        cityCodes.put("MED", "Medina Prince Mohammad bin Abdulaziz, Saudi Arabia");

        return cityCodes;
 }

          public Stream<Entry<String, String>> SortedCityCodes(Map<String,String> cityCodes) {
            Stream<Entry<String, String>> sortedMap = cityCodes.entrySet()
             .stream()
             .sorted(Map.Entry.comparingByValue());

            
            return sortedMap;
  }
    
}
