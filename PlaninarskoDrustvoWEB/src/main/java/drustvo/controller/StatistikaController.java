package drustvo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.PlaninaRepository;
import drustvo.repository.RezervacijaRepository;
import model.Planina;
import model.Rezervacija;

@Controller
@RequestMapping(value = "/sekretar")
public class StatistikaController {

	@Autowired
	RezervacijaRepository rezRepo;
	
	@Autowired
	PlaninaRepository planinaRepo;
	
	
	@GetMapping("/rezervacije")
	public String prikaziRezervacije(HttpServletRequest request) {
		List<Rezervacija> rezervacije = rezRepo.findAll();
		List<Planina> planine = planinaRepo.findAll();
		
		Map<Planina, List<Rezervacija>> mapaStatistika = new HashMap<>();
		for(Planina planina : planine) {
			List<Rezervacija> poPlanini = new ArrayList<>();
			for(Rezervacija rez : rezervacije) {
				if(rez.getDom().getPlanina() == planina) {
					poPlanini.add(rez);
				}
			}
			mapaStatistika.put(planina, poPlanini);
		}
		
		request.getSession().setAttribute("statistike", mapaStatistika);
		
		return "podesavanjaKorisnika";
	}
}
