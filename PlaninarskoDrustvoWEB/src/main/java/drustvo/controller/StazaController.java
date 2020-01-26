package drustvo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.PlaninaRepository;
import drustvo.repository.PlaninarskaStazaRepository;
import model.Planina;
import model.Planinarska_staza;
import model.Znamenitost;

@Controller
@RequestMapping(value = "/planina")
public class StazaController {
	
	@Autowired
	PlaninarskaStazaRepository stazaRepo;
	
	@Autowired
	PlaninaRepository planinaRepo;
	
	//Dobavljanje planinarskih staza u sklopu izabrane planine
	@GetMapping("/staze")
	public String getStaze(Integer idPlanine, HttpServletRequest request, HttpServletResponse response) {
		
		Planina planina = planinaRepo.findById(idPlanine).get();
		
		List<Planinarska_staza> staze = planina.getPlaninarskaStazas();
		
		//Svaka staza ima svoje znamenitosti, korišćenjem mape na JSP stranici možemo ovim putem dobaviti sve znamenitosti koje pritom odgovaraju svojim stazama
		Map<Integer, List<Znamenitost>> znamenitosti = new HashMap<>();
		
		for(Planinarska_staza staza : staze) {
			znamenitosti.put(staza.getIdStaza(), staza.getZnamenitosts());
		}
		
		request.getSession().setAttribute("znamenitosti", znamenitosti);
		
		request.getSession().setAttribute("staze", staze);
		
		return "rezervacijaDoma";
	}
	
	//Obrada slika iz baze i njihov prikaz na odgovarajućoj JSP stranici
	@GetMapping("/staza/slika")
	public void slika(Integer idStaza, HttpServletResponse response) {
		Planinarska_staza staza = stazaRepo.findById(idStaza).get();
		byte[] slika = staza.getMapa();
		response.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
		try {
			response.getOutputStream().write(slika);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
