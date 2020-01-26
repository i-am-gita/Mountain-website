package drustvo.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.DomRepository;
import drustvo.repository.KorisnikRepository;
import drustvo.repository.RezervacijaRepository;
import model.Dom;
import model.Korisnik;
import model.Rezervacija;

@Controller
@RequestMapping(value = "/clan")
public class RezervacijaController {

	@Autowired
	KorisnikRepository korisnikRepo;
	
	@Autowired
	DomRepository domRepo;
	
	@Autowired
	RezervacijaRepository rezRepo;
	
	//Rezervacija noćenja u domu
	@PostMapping("/rezervacija")
	public String rezervisi(HttpServletRequest request) throws ParseException {
		
		//Datumi dolaska i odlaska
		String datum1 = request.getParameter("datumOd");
		String datum2 = request.getParameter("datumDo");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		Date datumOd = df.parse(datum1);
		Date datumDo = df.parse(datum2);
		
		//Rezervacija je moguća samo ako je u budućnosti i ako traje bar jednu noć
		Date danas = new Date();
		if(datumOd.compareTo(danas) >= 0  && datumDo.compareTo(datumOd) > 0) {
		
			//Izabran dom
			String id = request.getParameter("idDom");
			Integer idDom = Integer.parseInt(id);
			Dom dom = domRepo.findById(idDom).get();
			
			//Korisnik koji rezerviše
			Principal principal = request.getUserPrincipal();
			String korisnickoIme = principal.getName();
			Korisnik korisnik = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
			
			Rezervacija rezervacija = new Rezervacija();
			rezervacija.setOd(datumOd);
			rezervacija.setDo_(datumDo);
			rezervacija.setDom(dom);
			rezervacija.setKorisnik(korisnik);
			
			rezRepo.save(rezervacija);
			request.getSession().setAttribute("status", "uspeh");
		}else {
			request.getSession().setAttribute("status", "greska");
		}
		
		
		return "rezervacijaDoma";
	}
}
