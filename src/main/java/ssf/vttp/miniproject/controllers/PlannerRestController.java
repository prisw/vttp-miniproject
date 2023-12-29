package ssf.vttp.miniproject.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.amadeus.exceptions.ResponseException;

import jakarta.validation.Valid;
import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Recommendation;
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


   @GetMapping(path="/home.html")
   public ModelAndView getCityPage () {
        logger.info("Request for API");
       ModelAndView mav = new ModelAndView("home");
       mav.addObject("cityCodes", planRepo.getCityCodes());
       return mav;
   }
    
   @GetMapping(path="cityview")
   public ModelAndView getTopCity(@RequestParam String cityCodes) throws ResponseException, CustomResponseException {
        ModelAndView mav = new ModelAndView("cityview");

        
        mav.addObject("cityCodes", planSvc.getAmadeusPage(cityCodes));
        mav.addObject("reccs", planSvc.getCompletePage());
        System.out.println("API is running now");
        return mav;
    }
   
  


    // @GetMapping(path="/cityview")
    // public String getTopFiveCity(@Valid Recommendation recommendation, @RequestParam String cityCodes , Model model ) throws ResponseException {
      
    
    //     List<City> cities = planSvc.getAmadeusPage(cityCodes);
    //     model.addAttribute("cityCodes", cities);

    //     List<Recommendation> reccs = planSvc.getCompletePage();
    //     model.addAttribute("reccs", reccs);
    //     return "cityview";        
        
       
    // }

   
    //write a POST mapping to call the longtitude and latitude, to call another method to recommend tours and activities
}
