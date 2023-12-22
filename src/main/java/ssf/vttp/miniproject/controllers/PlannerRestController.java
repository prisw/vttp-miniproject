package ssf.vttp.miniproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.amadeus.exceptions.ResponseException;

import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Recommendation;
import ssf.vttp.miniproject.repository.PlannerRepository;
import ssf.vttp.miniproject.service.PlannerService;

@RestController
@RequestMapping
public class PlannerRestController {

   @Autowired
   PlannerService planSvc;

   @Autowired
   PlannerRepository planRepo;


    @GetMapping(path="/home.html")
    public ModelAndView getCityPage () {
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("cityCodes", planRepo.getCityCodes());
        return mav;
    }

    @GetMapping(path="/citypage")
    public ModelAndView getTopFiveCity(@RequestParam MultiValueMap<String,String> params) throws ResponseException {
      
        String citycodes = params.getFirst("cityCodes");
        List<City> cities = planSvc.getAmadeusPage(citycodes);

        if(cities.isEmpty()) {
            ModelAndView mav2 = new ModelAndView("nocity");
            return mav2;
        } else {
        ModelAndView mav = new ModelAndView("cityview");
        List<Recommendation> reccs = planSvc.getCompletePage();
        mav.addObject("reccs", reccs);
        return mav;
        
        }

       
    }

   
    //write a POST mapping to call the longtitude and latitude, to call another method to recommend tours and activities
}
