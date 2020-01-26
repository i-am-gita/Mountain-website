package drustvo.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.ClanarinaRepository;
import drustvo.repository.KorisnikRepository;
import drustvo.repository.UlogaRepository;
import model.Clanarina;
import model.Korisnik;
import model.Uloga;

@Controller
@RequestMapping(value = "/sekretar")
public class ClanarinaController {

	@Autowired
	ClanarinaRepository clanarinaRepo;
	
	@Autowired
	KorisnikRepository korisnikRepo;
	
	@Autowired
	UlogaRepository ulogaRepo;
	
	//Prikazivanje pre svega gostiju, a onda i regularnih članova kojima je moguće produžiti članarinu
	@GetMapping("/podesavanja")
	public String getZahtevi(HttpServletRequest request) {
		List<Korisnik> korisnici = korisnikRepo.findAll();
		
		//Izdvajamo korisnike čija je uloga gost u posebnu listu i brišemo ih iz prvobitne liste korisnika kako bi bili hijerarhijski prikazani
		List<Korisnik> gosti = korisnici.stream()
									.filter(k -> k.getUloga().getNaziv().equals("gost"))
									.collect(Collectors.toList());
		korisnici.removeAll(gosti);
		
		request.getSession().setAttribute("gosti", gosti);
		request.getSession().setAttribute("clanovi", korisnici);
		
		return "podesavanjaKorisnika";
		
	}
	
	//Prihvatanje korisnika u društvo odnosno promena uloge iz gost u član
	@GetMapping("/potvrdiClanstvo")
	public void potvrdiClanstvo(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.err.println(id);
		//Integer idK = Integer.parseInt(idKorisnik);
		Korisnik korisnik = korisnikRepo.findById(id).get();
		Uloga ulogaClan = ulogaRepo.findById(1).get();
		korisnik.setUloga(ulogaClan);
		
		//Postavljamo članarinu korisnika na trajanje od mesec dana od trenutka primanja u društvo
		Date datumOd = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(datumOd);
		cal.add(Calendar.MONTH, 1);
		Date datumDo = cal.getTime();
		
		Clanarina clanarina = new Clanarina();
		clanarina.setDo_(datumDo);
		clanarina.setOd(datumOd);
		korisnik.setClanarina(clanarina);
		
		korisnikRepo.save(korisnik);
		
		request.getRequestDispatcher("/sekretar/podesavanja").forward(request, response);
	}
	
	//Produžavanje članarine korisnika za mesec dana od trenutka isteka prethodne.
	@GetMapping("/produziClanarinu")
	public void produziClanarinu(Integer id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Korisnik korisnik = korisnikRepo.findById(id).get();
		
		if(korisnik.getClanarina() == null) {
			
			Clanarina clanarina = new Clanarina();
			clanarina.setDo_(new Date());
			clanarina.setOd(new Date());
			korisnik.setClanarina(clanarina);
		}
		
		Date datumOd = korisnik.getClanarina().getDo_();
		Calendar cal = Calendar.getInstance();
		cal.setTime(datumOd);
		cal.add(Calendar.MONTH, 1); 
		Date datumDo = cal.getTime();
			
		Clanarina nova = new Clanarina();
		nova.setOd(datumOd);
		nova.setDo_(datumDo);
		korisnik.setClanarina(nova);
		korisnikRepo.save(korisnik);
		
		request.getRequestDispatcher("/sekretar/podesavanja").forward(request, response);

	}
}
