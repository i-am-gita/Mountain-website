package drustvo.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import model.Korisnik;
import model.Uloga;

public class MyUserDetails implements UserDetails{
	
	private String korisnicko_ime;
	private String lozinka;
	private Uloga uloga;
	
	public MyUserDetails(Korisnik korisnik) {
		this.korisnicko_ime = korisnik.getKorisnickoIme();
		this.lozinka = korisnik.getLozinka();
		this.uloga = korisnik.getUloga();

	}
	
	public MyUserDetails() {
		
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + uloga.getNaziv()));
		return authorities;

	}

	@Override
	public String getPassword() {
		return lozinka;
	}

	@Override
	public String getUsername() {
		return korisnicko_ime;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
