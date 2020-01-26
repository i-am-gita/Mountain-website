package drustvo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import drustvo.repository.IzvestajRepository;
import drustvo.repository.KorisnikRepository;
import drustvo.repository.PlaninaRepository;
import drustvo.repository.SlikaRepository;
import model.Izvestaj;
import model.Korisnik;
import model.Planina;
import model.Rezervacija;
import model.Slika;

@Controller
@RequestMapping(value = "/sekcija")
public class SekcijaController {

	@Autowired 
	SlikaRepository slikaRepo;
	
	@Autowired
	PlaninaRepository planinaRepo;
	
	@Autowired
	KorisnikRepository korisnikRepo;
	
	@Autowired
	IzvestajRepository izvestajRepo;
	
	//
	@GetMapping("/prikaz")
	public String opcije(HttpServletRequest request) {
		
		//Dobavljanje svih planina
		List<Planina> planine = planinaRepo.findAll();
		request.getSession().setAttribute("svePlanine", planine);
		
		//Dobavljanje svih izvestaja
		List<Izvestaj> izvestaji = izvestajRepo.findAll();
		
		//Kreiranje mape čiji će ključevi biti ID planine kako bi podaci bili grupisani po njima. 
		//Vrednosti mape su liste izveštaja koje sadrže liste slika koje karakterišu taj izveštaj
		Map<Integer, List<Izvestaj>> podaci = new HashMap<>();
		int idPlanine = -1;
		
		//Za svaku planinu prolazimo kroz sve izvestaje i izdvajamo one koje pripadaju toj planini
		for(Planina p : planine) {
			List<Izvestaj> izvestajiZaPlaninu = new ArrayList<>();
			for(Izvestaj i : izvestaji) {
				idPlanine = i.getRezervacija().getDom().getPlanina().getIdPlanina();
				if(idPlanine == p.getIdPlanina()) {
					List<Slika> slke = slikaRepo.findByIdPlanina(idPlanine);
					i.setSlikas(slke);
					izvestajiZaPlaninu.add(i);
				}
			}
			podaci.put(p.getIdPlanina(), izvestajiZaPlaninu);
		}
		
		request.getSession().setAttribute("podaci", podaci);
		
		return "sekcija";
	}
	
	//Slanje slike na JSP
	@GetMapping("/slika")
	public void slikaSekcija(Integer idSlike, HttpServletResponse response) {
		Slika slika = slikaRepo.findById(idSlike).get();
		response.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
		try {
			byte[] slk = slika.getSlika();
			response.getOutputStream().write(slk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Postavljanje korisnikovih slika i izvestaja sa planinarenja
	@PostMapping("/postavi")
	public String postaviSliku(Integer idPlanina, String tekst, @RequestParam("slika") MultipartFile slikaFajl, HttpServletRequest request) throws IOException {
		Planina planina = planinaRepo.findById(idPlanina).get();
		
		Principal principal = request.getUserPrincipal();
		String korisnickoIme = principal.getName();
		Korisnik korisnik = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
		boolean posetioPlaninu = false;
		Rezervacija poseta = null;
		
		//Samo ukoliko je član imao rezervaciju za nekuiod domova na izabranoj planini može da postavlja sadržaj
		for(Rezervacija rez : korisnik.getRezervacijas()) {
			if(rez.getDom().getPlanina() == planina && rez.getOd().before(new Date())) {
				posetioPlaninu = true;
				poseta = rez;
				break;
			}
		}
		if(posetioPlaninu) {
			byte[] fajl = slikaFajl.getBytes();
			Slika slika = new Slika();
			slika.setSlika(fajl);
			slika.setidPlanine(idPlanina);
			slikaRepo.save(slika);
			
			//Ukoliko postoji već izveštaj koji je korisnik uneo podrazumeva se da slika pripada njemu tako da će se dodati u njegovu listu.
			//Ukoliko ne postoji, pravi se novi.
			Izvestaj izvestaj = izvestajRepo.findByTekst(tekst);
			if(izvestaj == null) {
				izvestaj = new Izvestaj();
				izvestaj.setTekst(tekst);
			}
			List<Slika> slikeIzvestaja = new ArrayList<>();
			if(izvestaj.getSlikas() != null) {
				slikeIzvestaja.addAll(izvestaj.getSlikas());
			}
			slikeIzvestaja.add(slika);
			izvestaj.setSlikas(slikeIzvestaja);
			izvestaj.setRezervacija(poseta);
			izvestajRepo.save(izvestaj);
		}
		
		return "sekcija";
	}
}

