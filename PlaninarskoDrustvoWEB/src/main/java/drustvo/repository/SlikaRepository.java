package drustvo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Slika;
import model.Znamenitost;

public interface SlikaRepository extends JpaRepository<Slika, Integer>{

	public List<Slika> findByZnamenitost(Znamenitost znamenitost);
	
	public List<Slika> findByIdPlanina(Integer idPlanina);
}
