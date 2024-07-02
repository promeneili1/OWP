package com.example.owpturistickaagencija.Daos;

import com.example.owpturistickaagencija.Models.Rezervacija;

import java.util.List;

public interface RezervacijaDao {
    Rezervacija findOne(Long id);
    List<Rezervacija> findAll();
    int save(Rezervacija rezervacija);
    int update(Rezervacija rezervacija);
    int delete(Long id);
    void deleteByKupac(Long kupacId, Long id);
    List<Rezervacija> pretraziRezervacije(String upit);
    List<Rezervacija> findByPutovanjeId(Long putovanjeId);
    List<Rezervacija> getAktivneRezervacijeByKupacId(Long id);



}
