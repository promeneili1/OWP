package com.example.owpturistickaagencija.Daos;


import com.example.owpturistickaagencija.Models.Korisnik;

import java.util.List;

public interface KorisnikDao {
    Korisnik findOne(String emailAdresa);
    Korisnik findOne(String emailAdresa, String lozinka);
    public Korisnik findOne(Long id);
    public Korisnik findKorisnikByEmail(String email);
    public Korisnik findKorisnikByEmailAndPassword(String email, String lozinka);
    public List<Korisnik> findAll();
    public int save(Korisnik korisnik);
    public int update(Korisnik korisnik);
    public int delete(Long id);
}
