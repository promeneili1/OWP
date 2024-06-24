package com.example.owpturistickaagencija.Daos;

import com.example.owpturistickaagencija.Models.Korisnik;
import com.example.owpturistickaagencija.Models.Putovanje;

import java.util.List;

public interface PutovanjeDao {
    Putovanje findOne(String nazivDestinacije,Long brojNocenja);
    public Putovanje findOne(Long id);
    public List<Putovanje> findAll();
    public int save(Putovanje putovanje);
    public int update(Putovanje putovanje);
    public int delete(Long id);
    public List<Putovanje> findSortedPutovanja(String sort, String pravac) ;
    List<Putovanje> searchPutovanje(String query);
    public List<Putovanje> searchPutovanjeByAmountRange(Long minCena, Long maxCena);


}
