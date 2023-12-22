package ssf.vttp.miniproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.amadeus.exceptions.ResponseException;

import ssf.vttp.miniproject.models.City;
import ssf.vttp.miniproject.models.Itinerary;
import ssf.vttp.miniproject.service.PlannerService;

@Controller
@RequestMapping
public class PlannerController {

    @Autowired
    PlannerService planSvc;

    @GetMapping("/")
    public String LandingPage(Model model){
        model.addAttribute("itinerary", new Itinerary());
        return "view0";
    }
    
}
