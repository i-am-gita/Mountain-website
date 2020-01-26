package drustvo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import model.Dom;
import model.Planina;

public interface PlaninaRepository extends JpaRepository<Planina, Integer>{

}
