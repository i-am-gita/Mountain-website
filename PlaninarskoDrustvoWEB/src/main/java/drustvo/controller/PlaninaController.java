package drustvo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.DomRepository;
import drustvo.repository.PlaninaRepository;
import model.Dom;
import model.Planina;
import model.Rezervacija;

@Controller
@RequestMapping(value = "/smestaj")
public class PlaninaController {
	
	@Autowired
	PlaninaRepository planinaRepo;
	
	@Autowired
	DomRepository domRepo;
	
	//Dobavljanje planina prilikom odabira one planine za koju korisnik želi da pregleda domove, staze i sl.
	@GetMapping("/planine")
	public String getPlanina(HttpServletRequest request) {
		
		List<Planina> planine = planinaRepo.findAll();
		request.getSession().setAttribute("planine", planine);
		
		//Uklanjanje iz sesije atributa koji ne treba da se prikazuju
		if(request.getSession().getAttribute("status") != null) {
			request.getSession().removeAttribute("status");
		}
		
		return "rezervacijaDoma";
	}
	
	//Dobavljanje domova za određenu planinu
	@GetMapping("/domovi")
	public String getDomovi(HttpServletRequest request) {
		
		String idPlanine = request.getParameter("planina");
		Integer id = Integer.parseInt(idPlanine);
		Planina planina = planinaRepo.findById(id).get();
		
		List<Dom> domovi = domRepo.findByPlanina(planina);
		
		request.getSession().setAttribute("domovi", domovi);
		
		Map<Integer, Integer> slobodnaMestaDomovi = getSlobodnaMesta(domovi);
		request.getSession().setAttribute("slobodnaMesta", slobodnaMestaDomovi);
		

		return "rezervacijaDoma";
	}
	
	private Map<Integer, Integer> getSlobodnaMesta(List<Dom> domovi){
		Map<Integer, Integer> slobodnaMesta = new HashMap<>();
		Date danas = new Date();
		for(Dom d : domovi) {
			int brZauzetih = 0;
			for(Rezervacija r : d.getRezervacijas()) {
				if(danas.compareTo(r.getOd()) >= 0  &&  danas.compareTo(r.getDo_()) <= 0) {
					brZauzetih++;
				}
			}
			int brSlobodnih = d.getMaxKapacitet() - brZauzetih;
			slobodnaMesta.put(d.getIdDom(), brSlobodnih);
		}
		
		return slobodnaMesta;
	}

}
