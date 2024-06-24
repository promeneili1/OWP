package com.example.owpturistickaagencija.Daos;

import com.example.owpturistickaagencija.Models.KategorijaPutovanja;
import com.example.owpturistickaagencija.Models.Korisnik;

import java.util.List;

public interface KategorijaPutovanjaDao {
    KategorijaPutovanja findOne(Long id);
    KategorijaPutovanja findOne(Long id, String nazivKategorije);
    public KategorijaPutovanja findKategorijaPutovanjaById(Long id);
    public List<KategorijaPutovanja> findAll();
    public int save(KategorijaPutovanja kategorijaPutovanja);
    public int update(KategorijaPutovanja kategorijaPutovanja);
    public int delete(Long id);
}
