package drustvo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Planina;
import model.Rezervacija;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Integer>{

	@Query("SELECT r FROM Rezervacija r WHERE r.dom.planina = :planina")
	public List<Rezervacija> findByPlanina(@Param("planina") Planina p);
}
