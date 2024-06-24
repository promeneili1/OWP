package com.example.owpturistickaagencija.Services;

import com.example.owpturistickaagencija.Exceptions.UserNotFoundException;
import com.example.owpturistickaagencija.Models.Korisnik;
import com.example.owpturistickaagencija.Models.Putovanje;
import com.example.owpturistickaagencija.Models.Uloga;

import javax.servlet.http.Cookie;
import java.util.List;

public interface PutovanjeService {

    Putovanje get(Long id);

    Putovanje get(String nazivDestinacije,Long brojNocenja);

    Putovanje findOne(Long id) throws UserNotFoundException;

    List<Putovanje> findAll();

    Putovanje save(Putovanje putovanje);

    Putovanje delete(Long id);

    Putovanje update(Putovanje putovanje);

    List<Putovanje> searchPutovanje(String query);
    public List<Putovanje> findSortedPutovanje(String sort, String pravac) ;

    public List<Putovanje> searchPutovanjeByAmountRange(Long minCena, Long maxCena);
}
