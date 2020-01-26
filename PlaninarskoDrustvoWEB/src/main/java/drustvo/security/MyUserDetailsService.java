package drustvo.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import drustvo.repository.KorisnikRepository;
import model.Korisnik;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	KorisnikRepository korisnikRepository;


	@Override
	public UserDetails loadUserByUsername(String korisnicko_ime) throws UsernameNotFoundException {
		Optional<Korisnik> korisnik = korisnikRepository.findByKorisnickoIme(korisnicko_ime);

		korisnik.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + korisnicko_ime));

		return korisnik.map(MyUserDetails::new).get();
	}

}
