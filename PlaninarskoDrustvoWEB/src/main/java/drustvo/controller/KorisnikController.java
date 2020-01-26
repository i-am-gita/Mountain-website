package drustvo.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import drustvo.repository.KorisnikRepository;
import drustvo.repository.UlogaRepository;
import model.Korisnik;
import model.Uloga;

@Controller
@RequestMapping(value = "/")
public class KorisnikController {

	@Autowired
	KorisnikRepository korisnikRepo;

	@Autowired
	UlogaRepository ulogaRepo;
	
	//Registracija novog korisnika i čuvanje u bazu. Nakon što je korisnik registrovan njegova uloga je gost sve dok ga sekretar ne primi u društvo.
	//Korisnik kao gost ima mogućnost da vidi domove, planinarske staze, znamenitosti, izveštaje i slike, ali nije u mogućnosti da postavlja sadržaj,
	//rezerviše domove, termine za posetu znamenitosti, komentariše i slično.
	@PostMapping("/register")
	public String registracija(HttpServletRequest request) {
		String ime = request.getParameter("name");
		String prezime = request.getParameter("lastname");
		String korisnickoIme = request.getParameter("username");
		String lozinka = request.getParameter("password");
		Uloga gost = ulogaRepo.findById(3).get();

		Korisnik novi = new Korisnik();
		novi.setIme(ime);
		novi.setPrezime(prezime);
		novi.setKorisnickoIme(korisnickoIme);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		novi.setLozinka(passwordEncoder.encode(lozinka));
		novi.setUloga(gost);
		korisnikRepo.save(novi);
		return "index";

	}
	//Redirekcija na autentifikaciju koja je u sklopu spring-security-a.
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//Nakon što se korisnik uloguje, ovaj kontroler se aktivira koji ima zadatak da pošalje podatke o korisniku na početnu stranu
	//Ti podaci se koriste pri ispisu poruke dobrodošlice i odgovarajuće poruke ukoliko je korisnik gost.
	@ResponseBody
	@GetMapping("/clan/pocetna")
	public void getKorisnickoIme(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Principal principal = request.getUserPrincipal();
		String korisnickoIme = principal.getName();
		Korisnik ulogovan = korisnikRepo.findByKorisnickoIme(korisnickoIme).get();
		request.getSession().setAttribute("uloga", ulogovan.getUloga());
		request.getSession().setAttribute("korisnickoIme", ulogovan.getIme());
		request.getRequestDispatcher("/pocetna.jsp").forward(request, response);
	}

}
