package com.example.owpturistickaagencija.Services;

import com.example.owpturistickaagencija.Exceptions.UserNotFoundException;
import com.example.owpturistickaagencija.Models.KategorijaPutovanja;
import com.example.owpturistickaagencija.Models.Korisnik;

import java.util.List;

public interface KategorijaPutovanjaService {

    KategorijaPutovanja get(Long id);

    KategorijaPutovanja get(Long id, String nazivKategorije);
    KategorijaPutovanja findKategorijaPutovanjaById(Long id) throws UserNotFoundException;

    List<KategorijaPutovanja> findAll();

    KategorijaPutovanja save(KategorijaPutovanja kategorijaPutovanja);

    KategorijaPutovanja delete(Long id);

    KategorijaPutovanja update(KategorijaPutovanja kategorijaPutovanja);
}
