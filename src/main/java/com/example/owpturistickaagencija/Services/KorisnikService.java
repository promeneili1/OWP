package com.example.owpturistickaagencija.Services;


import com.example.owpturistickaagencija.Exceptions.UserNotFoundException;
import com.example.owpturistickaagencija.Models.Korisnik;
import com.example.owpturistickaagencija.Models.Uloga;

import javax.servlet.http.Cookie;
import java.util.List;

public interface KorisnikService {
    Korisnik get(Long id);
    Korisnik get(String emailAdresa,String lozinka);
    Korisnik get(String emailAdresa);
    Korisnik findOne(Long id) throws UserNotFoundException;
    List<Korisnik> findAll();
    Korisnik findKorisnikByEmail(String email);
    Korisnik findKorisnikByEmailAndPassword(String email, String lozinka);
    Korisnik save(Korisnik korisnik);
    Korisnik update(Korisnik korisnik);
    Korisnik delete(Long id);
    boolean checkCookies(Cookie[] cookies, Uloga uloga);
    Korisnik checkCookieUser(Cookie[] cookies);
}
