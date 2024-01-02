package ssf.vttp.miniproject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.amadeus.exceptions.ResponseException;

import jakarta.servlet.http.HttpSession;
import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Itinerary;
import ssf.vttp.miniproject.repository.PlannerRepository;
import ssf.vttp.miniproject.service.PlannerService;
import ssf.vttp.miniproject.service.PlannerService.CustomResponseException;

@RestController
@RequestMapping
public class PlannerRestController {

    private Logger logger = Logger.getLogger(PlannerRestController.class.getName());


   @Autowired
   PlannerService planSvc;

   @Autowired
   PlannerRepository planRepo;

   List<Itinerary> itinerary = new ArrayList<>();


   @GetMapping(path="/home.html")
   public ModelAndView getCityPage (HttpSession session) {
        logger.info("Request for API");
        ModelAndView mav = new ModelAndView("home");

        String name = (String) session.getAttribute("name");
        if(name != null){
             mav.addObject("name", name);
        }

       Map<String,String> cityCodes = planRepo.getCityCodes();
       Stream<Entry<String,String>> sortedCity =planRepo.SortedCityCodes(cityCodes);
       System.out.printf("SortedCity:%s",sortedCity.toString());
       mav.addObject("cityCodes", sortedCity);
       return mav;
   }

   @GetMapping(path="/reccpage")
   public ModelAndView getTopCity(@RequestParam String cityCodes, HttpSession session) throws ResponseException, CustomResponseException {
        ModelAndView mav = new ModelAndView("cityview");
        String name = session.getAttribute("name").toString();

    try {
        List<City> cities = planSvc.getAmadeusPage(cityCodes);
        logger.info("Amadeus response: " + cities);
    
        // List<Recommendation> reccs = planSvc.getCompletePage(cities);
        
        mav.addObject("name", name);
        mav.addObject("cities",cities);
        // mav.addObject("reccs", reccs);
        // mav.addObject("itineraries", itinerary);
        System.out.println("API is running now");
        System.out.println("------------"+cities.get(0).getName());

    } catch (Exception e) {
        e.printStackTrace();
    }
        return mav;
    }


    @GetMapping(path="/toplanner")
    public ModelAndView toPlannerPage(HttpSession session, Model model){
        String name = session.getAttribute("name").toString();
        System.out.println("Redirecting to planner page for user: "+ name );
        // return "redirect:/planner?name="+name;
        return new ModelAndView("redirect:/planner?name="+name);
    }
}
