package drustvo.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.KomentarRepository;
import drustvo.repository.KorisnikRepository;
import drustvo.repository.ZnamenitostRepository;
import model.Komentar;
import model.Korisnik;
import model.Poseta;
import model.Rezervacija;
import model.Znamenitost;

@Controller
@RequestMapping(value = "/clan")
public class KomentarController {

	@Autowired
	KomentarRepository komRepo;
	
	@Autowired
	KorisnikRepository korisnikRepo;
	
	@Autowired
	ZnamenitostRepository znamenitostRepo;
	
	@GetMapping("/komentarisi")
	public String komentarisi(Integer idZnamenitost, String tekst,HttpServletRequest request) {
		//Dobavljamo korisnika preko principala
		Principal principal = request.getUserPrincipal();
		String korisnickoIme = principal.getName();
		Korisnik korisnik = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
		
		//Ažuriranje znamenitosti u sesiji kako bi se ispisao odmah novi komentar
		Znamenitost znm = znamenitostRepo.findById(idZnamenitost).get();
		request.getSession().setAttribute("znamenitost", znm);
		
		//Proveravamo da li je korisnik posetio znamenitost preko njegovih rezervacija, koje sadrze posete, koje imaju datum zavrsetka termina svake posete.
		//Proveravamo da li je datum završetka posete manji od trenutnog datuma, ako jeste to znači da je korisnik u prošlosti zaista posetio tu znamenitost.
		//Ukoliko potvrdimo posetu, izlazimo iz svih petlji kako ne bismo uzalud trošili resurse.
		boolean posetio = false;
		Poseta posetaKomentar = null;
		if(!korisnik.getRezervacijas().isEmpty()) {
			for(Rezervacija rezervacija : korisnik.getRezervacijas()) {
				if(posetio) break;
				if(!rezervacija.getPosetas().isEmpty()) {
					for(Poseta poseta : rezervacija.getPosetas()) {
						if(posetio) break;
						if(poseta.getTerminZnamenitost() != null) {
							if((poseta.getTerminZnamenitost().getKraj().before(new Date())) && poseta.getTerminZnamenitost().getZnamenitost().getIdZnamenitost() == znm.getIdZnamenitost()) {
								posetaKomentar = poseta;
								posetio = true;
								break;
							}
						}
					}
				}
			}
		}
		
		//Ukoliko je posetio, pravimo novi komentar sa odgovarajućom posetom i čuvamo ga u bazi.
		if(posetio) {
			Komentar komentar = new Komentar();
			komentar.setPoseta(posetaKomentar);
			komentar.setTekst(tekst);
			komRepo.save(komentar);
			
			request.getSession().setAttribute("posetio", "jeste");
		}else {
			request.getSession().setAttribute("posetio", "nije");
		}
		
		return "znamenitost";
	}
}
