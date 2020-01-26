package drustvo.domain;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.web.multipart.MultipartFile;

import model.Planina;

public class PlaninarskaStazaSlika implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	private int idStaza;
	@Lob
	private MultipartFile mapa;
	private String naziv;
	private String opis;
	private int tezina;
	private Planina planina;
	
	
	public PlaninarskaStazaSlika() {
		
	}


	public int getIdStaza() {
		return idStaza;
	}


	public void setIdStaza(int idStaza) {
		this.idStaza = idStaza;
	}


	public MultipartFile getMapa() {
		return mapa;
	}


	public void setMapa(MultipartFile mapa) {
		this.mapa = mapa;
	}


	public String getNaziv() {
		return naziv;
	}


	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}


	public String getOpis() {
		return opis;
	}


	public void setOpis(String opis) {
		this.opis = opis;
	}


	public int getTezina() {
		return tezina;
	}


	public void setTezina(int tezina) {
		this.tezina = tezina;
	}


	public Planina getPlanina() {
		return planina;
	}


	public void setPlanina(Planina planina) {
		this.planina = planina;
	}
	
	

}
