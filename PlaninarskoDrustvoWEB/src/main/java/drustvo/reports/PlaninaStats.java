package drustvo.reports;

import java.util.Date;

public class PlaninaStats {

	private String planina;
	private int brojRezervacija;
	private int brojNocenja;
	private String najviseRezervacija;
	private String odnos;
	
	public PlaninaStats() {
		
	}
	
	public PlaninaStats(String naziv, int brR, int brN, String naj, String odnos) {
		this.planina = naziv;
		this.brojRezervacija = brR;
		this.brojNocenja = brN;
		this.najviseRezervacija = naj;
		this.odnos = odnos;
	}
	
	public String getPlanina() {
		return planina;
	}
	public void setPlanina(String planina) {
		this.planina = planina;
	}
	public int getBrojRezervacija() {
		return brojRezervacija;
	}
	public void setBrojRezervacija(int brojRezervacija) {
		this.brojRezervacija = brojRezervacija;
	}
	public int getBrojNocenja() {
		return brojNocenja;
	}
	public void setBrojNocenja(int brojNocenja) {
		this.brojNocenja = brojNocenja;
	}
	public String getNajviseRezervacija() {
		return najviseRezervacija;
	}
	public void setNajviseRezervacija(String najviseRezervacija) {
		this.najviseRezervacija = najviseRezervacija;
	}
	public String getOdnos() {
		return odnos;
	}
	public void setOdnos(String odnos) {
		this.odnos = odnos;
	}
	
	
	
}
