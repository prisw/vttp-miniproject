package ssf.vttp.miniproject.controllers;

import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.vttp.miniproject.models.Itinerary;
import ssf.vttp.miniproject.repository.PlannerRepository;
import ssf.vttp.miniproject.service.PlannerService;

@Controller
@RequestMapping()
public class PlannerController {

    private Logger logger = Logger.getLogger(PlannerController.class.getName());

    @Autowired
    PlannerService planSvc;

    @Autowired
    PlannerRepository planRepo;



    @GetMapping("/index")
    public String getIndexPage(Model model) {
        model.addAttribute("itinerary", new Itinerary());
        return "index";
    }

    @GetMapping("/planner")
    public String getItinerary(@RequestParam String name, Model model, HttpSession session){
        
        List<Itinerary> itineraries = planRepo.getAllItineraries(name);
        logger.info("Itineraries: %s - %s\n".formatted(name,itineraries));
        session.setAttribute(planRepo.ATTR_ITIN, itineraries);

        session.setAttribute("name", name);
        System.out.println(name);

        model.addAttribute("itineraries",itineraries);
        model.addAttribute("name", name);

        return "view1";
    }


    @GetMapping("/planner/success")
    public String goToHomepage(HttpSession session) {
        String name = session.getAttribute("name").toString();
    
        return "redirect:/planner?name="+name;
    }



    @GetMapping("/planner/addnew")
    public String itineraryAdd(Model model) {
        Itinerary itin = new Itinerary();
        model.addAttribute("itineraries", itin);
        return "view2";
    }

    @PostMapping("/planner/save")
    public String itinerarySave(@Valid @ModelAttribute("itineraries") Itinerary itinerary,BindingResult bindings, Model model, 
    HttpSession session) throws FileNotFoundException{

        if(bindings.hasErrors()) {
            System.out.println("error in Itinerary Adding Page");
            System.out.println(bindings.getAllErrors());
            return "view2";
        } 

        System.out.printf("itinerary: %s\n", itinerary);
        System.out.printf("error: %b\n", bindings.hasErrors());

        List<Itinerary> itineraries = PlannerRepository.getItineraries(session);        
        itineraries.add(itinerary);

        session.setAttribute("itineraries", itineraries);
        model.addAttribute("itineraries", itineraries);
        

        String username = (String) session.getAttribute("name");
        model.addAttribute("name", username);


        String id = planSvc.generateId();
        itinerary.setId(id);
        planSvc.save(username, itinerary);
        return "success";
    }

    @GetMapping("/planner/delete/{id}")
    public String deleteItinerary(@PathVariable("id") String id, HttpSession session) {
        String name = session.getAttribute("name").toString();
        planRepo.deleteItinerary(name,id);
        
        return "redirect:/planner?name="+name;
    }

    @GetMapping("planner/update/{id}")
    public String updateItinerary(@PathVariable("id") String id,Model model, HttpSession session) {
        
        String name = session.getAttribute("name").toString(); 
        
      
        Itinerary existingItinerary = planRepo.getOneItinerary(name, id);

        if(existingItinerary != null){
            model.addAttribute("itineraries", existingItinerary);
            return "view3";
        } else return "redirect:/planner?name="+name;
    }


    @PostMapping("planner/update")
    public String updateItineraryRecord(@Valid @ModelAttribute("itineraries") Itinerary itinerary,BindingResult bindings, Model model, 
    HttpSession session) {

    if(bindings.hasErrors()) {
        return "view3";
    }
    String name = session.getAttribute("name").toString();
    planRepo.updateItinerary(itinerary,name);
   
    return "redirect:/planner?name="+name;
}


@GetMapping(path="/exit")
public String exitPlannerPage(HttpSession session, Model model) {

    String nameObj= (String)session.getAttribute("name").toString();

    if (nameObj != null) {
        String name = nameObj;
        List<Itinerary> itineraries = PlannerRepository.getItineraries(session);
        for (Itinerary itinerary: itineraries){
              System.out.printf("Exit this planner page: %s\n", itinerary);
            planSvc.save(name,itinerary);
            }  model.addAttribute("name", name);
    } 
    session.invalidate();
    return "redirect:/";
}

}
   




    

