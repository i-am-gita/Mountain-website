package drustvo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Korisnik;
import model.Uloga;

public interface KorisnikRepository extends JpaRepository<Korisnik, Integer>{

	Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);
	
	List<Korisnik> findByUloga(Uloga uloga);
}
