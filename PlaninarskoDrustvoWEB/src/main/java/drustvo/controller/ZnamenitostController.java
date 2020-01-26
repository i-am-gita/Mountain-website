package drustvo.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.KorisnikRepository;
import drustvo.repository.PosetaRepository;
import drustvo.repository.SlikaRepository;
import drustvo.repository.TerminZnamenitostiRepository;
import drustvo.repository.ZnamenitostRepository;
import model.Korisnik;
import model.Planina;
import model.Poseta;
import model.Rezervacija;
import model.Slika;
import model.Termin_znamenitost;
import model.Znamenitost;

@Controller
@RequestMapping(value = "/clan")
public class ZnamenitostController {

	@Autowired
	ZnamenitostRepository znamenitostRepo;
	
	@Autowired
	SlikaRepository slikaRepo;
	
	@Autowired
	TerminZnamenitostiRepository terminRepo;
	
	@Autowired
	KorisnikRepository korisnikRepo;
	
	@Autowired
	PosetaRepository posetaRepo;
	
	//Dobavljanje znamenitosti sa svim potrebnim informacijama koje su za nju vezane(rezervacija, komentar, opis)
	@GetMapping("/znamenitost")
	public String znamenitost(Integer idZnamenitost, HttpServletRequest request) {
		
		//Brišemo iz sesije atribute rezervisao i posetio kako ne bi korisniku ispisivao poruku koju nije potrebno ispisati u tom trenutku
		if(request.getSession().getAttribute("rezervisao") != null) 
			request.getSession().removeAttribute("rezervisao");
		
		if(request.getSession().getAttribute("posetio") != null)
			request.getSession().removeAttribute("posetio");
		
		Znamenitost znm = znamenitostRepo.findById(idZnamenitost).get();
		List<Termin_znamenitost> termini = new ArrayList<>();
		for(Termin_znamenitost termin : znm.getTerminZnamenitosts()) {
			if(termin.getObavezno() == 1) termini.add(termin);
		}
		//!!slike
		request.getSession().setAttribute("znamenitost", znm);
		request.getSession().setAttribute("termini", termini);
		
		
		return "znamenitost";
	}
	//Obrada slika znamenitosti iz baze i njihov prikaz na odgovarajućoj JSP stranici(znamenitost.jsp)
	@GetMapping("/znamenitost/slika")
	public void slika(Integer idSlike, HttpServletResponse response) {
		Slika slika = slikaRepo.findById(idSlike).get();
		response.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
		try {
			response.getOutputStream().write(slika.getSlika());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Rezervacija znamenitosti u određenom terminu. Ukoliko znamenitost nije potrebno rezervisati na JSP stranici se ova sekcija ne prikazuje.
	@GetMapping("/znamenitost/rezervisi")
	public String rezervisiZnamenitost(Integer terminPosete, HttpServletRequest request) {
		
		//Dobavljamo termin koji korisnik želi da rezerviše kao i planinu na kojoj se nalazi ta znamenitost.
		Termin_znamenitost termin = terminRepo.findById(terminPosete).get();
		Planina planina = termin.getZnamenitost().getPlaninarskaStaza().getPlanina();
		
		//Dobavljamo korisnika koji rezerviše
		Principal principal = request.getUserPrincipal();
		String korisnickoIme = principal.getName();
		Korisnik korisnik = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
		
		if(postojiRezervacija(korisnik.getRezervacijas(), termin)) {
			request.getSession().setAttribute("rezervisao", "jeste");
		}else{
			request.getSession().setAttribute("rezervisao", "nije");
		}
		
		return "znamenitost";
	}
	
	//Biranje termina za posetu znamenitosti na kojoj nije potrebna rezervacija
	@GetMapping("/znamenitost/poseti")
	public String posetiZnamenitost(String terminP, Integer znamenitost, HttpServletRequest request ) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		Date terminPosete = df.parse(terminP);
		Date now = new Date();
		Principal principal = request.getUserPrincipal();
		String korisnickoIme = principal.getName();
		Korisnik korisnik = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
		
		//Procesuiramo isključivo ako je član izabrao termin u budućnosti
		if(terminPosete.after(now)) {
			
			//Dobavljamo znamenitost koju član želi da poseti
			Znamenitost sePosecuje = znamenitostRepo.findById(znamenitost).get();
			
			//Kreiramo termin čiji je početak odabran datum korisnika, a kraj dan nakon toga.
			Termin_znamenitost termin = new Termin_znamenitost();
			termin.setZnamenitost(sePosecuje);
			termin.setPocetak(terminPosete);
			Calendar cal = Calendar.getInstance();
			cal.setTime(terminPosete);
			cal.add(Calendar.HOUR, 24);
			Date krajPosete = cal.getTime();
			termin.setKraj(krajPosete);
			termin.setObavezno(0);
			terminRepo.save(termin);

			if(postojiRezervacija(korisnik.getRezervacijas(), termin)){
				request.getSession().setAttribute("mogucaPoseta", "jeste");
			}else {
				terminRepo.delete(termin);
				request.getSession().setAttribute("mogucaPoseta", "nije");

			}
		}
		
		return "znamenitost";
	}
	
	//Proveravamo da li korisnik ima rezervaciju za dom koji se nalazi na planini na kojoj je znamenitost
	//Takođe proveravamo da li je datum termina posete znamenitosti u periodu trajanja rezervacije doma
	private boolean postojiRezervacija(List<Rezervacija> rezKorisnika, Termin_znamenitost terminPosete) {
		boolean postoji = false;
		Planina planina = terminPosete.getZnamenitost().getPlaninarskaStaza().getPlanina();
		for(Rezervacija rez : rezKorisnika) {
			if(planina == rez.getDom().getPlanina() && terminPosete.getPocetak().after(rez.getOd()) && terminPosete.getKraj().before(rez.getDo_())) {
				Poseta poseta = new Poseta();
				poseta.setRezervacija(rez);
				poseta.setTerminZnamenitost(terminPosete);
				posetaRepo.save(poseta);
				postoji = true;
				break;
			}
		}
		return postoji;
	}
}
