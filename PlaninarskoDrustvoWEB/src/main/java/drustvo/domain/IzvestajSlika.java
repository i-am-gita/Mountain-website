package drustvo.domain;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.web.multipart.MultipartFile;

import model.Izvestaj;
import model.Znamenitost;

public class IzvestajSlika implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private int idSlika;
	@Lob
	private MultipartFile slika;
	private Izvestaj izvestaj;
	private Znamenitost znamenitost;
	
	public IzvestajSlika() {
		
	}
	
	
	public int getIdSlika() {
		return idSlika;
	}
	public void setIdSlika(int idSlika) {
		this.idSlika = idSlika;
	}
	public MultipartFile getSlika() {
		return slika;
	}
	public void setSlika(MultipartFile slika) {
		this.slika = slika;
	}
	public Izvestaj getIzvestaj() {
		return izvestaj;
	}
	public void setIzvestaj(Izvestaj izvestaj) {
		this.izvestaj = izvestaj;
	}
	public Znamenitost getZnamenitost() {
		return znamenitost;
	}
	public void setZnamenitost(Znamenitost znamenitost) {
		this.znamenitost = znamenitost;
	}
}
