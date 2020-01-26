package drustvo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Izvestaj;

public interface IzvestajRepository extends JpaRepository<Izvestaj, Integer>{

	@Query(value = "SELECT * from Izvestaj i WHERE i.Rezervacija_idRezervacija in \n" + 
			"(SELECT idRezervacija FROM Rezervacija r WHERE r.Dom_idDom in \n" + 
			"(SELECT idDom FROM Dom d WHERE d.Planina_idPlanina = :idP)) \n" + 
			"AND i.Rezervacija_idRezervacija IN (SELECT idRezervacija FROM Rezervacija re WHERE re.Korisnik_idKorisnik = :idK)", nativeQuery = true)
	public List<Izvestaj> findByPlanina(@Param("idP")Integer idP, @Param("idK")Integer idK);
	
	
	public Izvestaj findByTekst(String tekst);
}
