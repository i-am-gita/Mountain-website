package drustvo.reports;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import drustvo.repository.DomRepository;
import drustvo.repository.PlaninaRepository;
import drustvo.repository.RezervacijaRepository;
import drustvo.repository.TerminZnamenitostiRepository;
import drustvo.repository.ZnamenitostRepository;
import model.Dom;
import model.Planina;
import model.Rezervacija;
import model.Termin_znamenitost;
import model.Znamenitost;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("/sekretar")
public class ReportController {
	
	@Autowired
	RezervacijaRepository rezRepo;
	
	@Autowired
	PlaninaRepository planinaRepo;
	
	@Autowired
	DomRepository domRepo;
	
	@Autowired
	TerminZnamenitostiRepository terminRepo;
	
	@Autowired
	ZnamenitostRepository znamRepo;
	
	//Generisanje spiska rezervisanih termina za datu znamenitost
	@GetMapping("/znamenitost/spisak")
	public void generisiSpisak(Integer idZnamenitost, HttpServletRequest request, HttpServletResponse response) throws Exception { 
		List<ZnamenitostStats> stats = znamenitostStats(idZnamenitost);
	
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stats);
		InputStream inputStream = this.getClass().getResourceAsStream("/reports/Spisak.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=spisak.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
	
	//Dobavlja podatke koji se šalju u report
	private List<ZnamenitostStats> znamenitostStats(Integer id) {
		List<ZnamenitostStats> stats = new ArrayList<>();
		Znamenitost znamenitost = znamRepo.findById(id).get();
		List<Termin_znamenitost> termini = terminRepo.findByZnamenitost(znamenitost);
		
		for(Termin_znamenitost t : termini) {
			if(t.getObavezno()==1) {
				ZnamenitostStats zstats = new ZnamenitostStats();
				zstats.setPocetak(t.getPocetak().toString());
				zstats.setKraj(t.getKraj().toString());
				zstats.setTip(znamenitost.getTip());
				zstats.setNazivStaze(znamenitost.getPlaninarskaStaza().getNaziv());
				stats.add(zstats);
			}
		}
		return stats;
	}
	
	
	@GetMapping("/rezervacije/statistike")
	public void generisiIzvestaj(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		List<Rezervacija> rezervacije = rezRepo.findAll();
		List<PlaninaStats> stats = getStats();
	
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stats);
		InputStream inputStream = this.getClass().getResourceAsStream("/reports/Statistika.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		
		
		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "attachment; filename=statistike.pdf");
		OutputStream out = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,out);
	}
	
	//Statistike rezervacija grupisane po planinama
	private List<PlaninaStats> getStats() {
		List<PlaninaStats> result = new ArrayList<>();
		List<Planina> planine = planinaRepo.findAll();
		
		//Za svaku planinu dobavljamo rezervacije na toj planini i računamo statistike
		for(Planina p : planine) {
			List<Rezervacija> rezPlanina = rezRepo.findByPlanina(p);
			PlaninaStats pStats = new PlaninaStats();
			pStats.setPlanina(p.getNaziv());
			pStats.setBrojRezervacija(rezPlanina.size());
			int brNocenja = getBrNocenja(rezPlanina);
			pStats.setBrojNocenja(brNocenja);
			String najgusciDan = najviseRezervacija(rezPlanina);
			pStats.setNajviseRezervacija(najgusciDan);
			
			List<Dom> domPlanina = domRepo.findByPlanina(p);
			String odnosMesta = getOdnos(domPlanina);
			pStats.setOdnos(odnosMesta);
			
			result.add(pStats);
		}
		
		return result;
	}
	
	//Računa broj noćenja po planini tako što sabere sva trajanja rezervacija na toj planini
	private int getBrNocenja(List<Rezervacija> rezervacije) {
		int brN = 0;
		for(Rezervacija r : rezervacije) {
			brN += r.getTrajanjeRezervacije();
		}
		
		return brN;
	}
	
	//Dobavlja datum u kom ima najviše rezervacija na toj planini
	private String najviseRezervacija(List<Rezervacija> rezervacije) {
		String najgusci = "Nema rezervacija";
		HashSet<Date> datumi = new HashSet<>();
		
		//Dobavlja sve datume za koje postoji rezervacija(rezervacija je u toku) tako što krene od početka rezervacije i dodaje po jedan datum za svako noćenje
		for(Rezervacija r : rezervacije) {
			Date od = r.getOd();
			datumi.add(od);
			int trajanje = r.getTrajanjeRezervacije();
			for(int i = 0 ; i < trajanje; i ++) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(od);
				cal.add(Calendar.HOUR, 24);
				Date nocenje = cal.getTime();
				datumi.add(nocenje);
			}
		}
		
		//Za svaki datum prolazi kroz sve rezervacije i gleda da li njihovo trajanje obuhvata taj datum
		//Na taj način dobijam ukupan broj rezervacija za planinu po datumu, ostaje samo da se nađe maksimum a to je trivijalno.
		int maxRez = 0;
		Date najgusciDan = null;
		for(Date datum : datumi) {
			int brRez = 0;
			for(Rezervacija r : rezervacije) {
				if((datum.compareTo(r.getOd())>=0) && (datum.compareTo(r.getDo_()) <= 0)) {
					brRez++;
				}
			}
			if(brRez > maxRez) {
				maxRez = brRez;
				najgusciDan = datum;
			}
		}
		
		if(najgusciDan != null) {
			najgusci =  najgusciDan.toString();
		}
		
		return najgusci;
	}
	
	//Računa odnos između slobodnih i zauzetih mesta u domovima za svaku planinu u trenutku proveravanja
	//Mogao je ovaj odnos da se izračuna na nivou domova što je logičnije, ali zaista nemam vremena da to formatiram u jasper studiu
	private String getOdnos(List<Dom> domovi) {
		int brMesta = 0;
		int brZauzetih = 0;
		for(Dom d : domovi) {
			brMesta += d.getMaxKapacitet();
			brZauzetih += trenutniBrojRez(d.getRezervacijas());
		}
		return brZauzetih + "/" + brMesta;
	}
	
	//Računa trenutan broj zauzetih mesta za određeni dom
	private int trenutniBrojRez(List<Rezervacija> rezervacije) {
		Date trenutan = new Date();
		int brZauzetih = 0;
		for(Rezervacija r : rezervacije) {
			if(trenutan.compareTo(r.getOd())>=0 && trenutan.compareTo(r.getDo_())<=0) {
				brZauzetih++;
			}
		}
		
		return brZauzetih;
	}
}
