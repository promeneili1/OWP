package com.example.owpturistickaagencija.Services.impl;

import com.example.owpturistickaagencija.Daos.KategorijaPutovanjaDao;
import com.example.owpturistickaagencija.Models.KategorijaPutovanja;
import com.example.owpturistickaagencija.Models.Korisnik;
import com.example.owpturistickaagencija.Services.KategorijaPutovanjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseKategorijaPutovanjaServiceImpl implements KategorijaPutovanjaService {
    @Autowired
    private KategorijaPutovanjaDao kategorijaPutovanjaDao;

    @Override
    public KategorijaPutovanja get(Long id, String nazivKategorije) {
        return kategorijaPutovanjaDao.findOne(id,nazivKategorije);
    }

    @Override
    public KategorijaPutovanja get(Long id) {
        return kategorijaPutovanjaDao.findOne(id);
    }


    @Override
    public KategorijaPutovanja save(KategorijaPutovanja kategorijaPutovanja) {
        kategorijaPutovanjaDao.save(kategorijaPutovanja);
        return kategorijaPutovanja;
    }

    @Override
    public KategorijaPutovanja findKategorijaPutovanjaById(Long id) {
        return kategorijaPutovanjaDao.findKategorijaPutovanjaById(id);
    }

    @Override
    public KategorijaPutovanja update(KategorijaPutovanja kategorijaPutovanja) {
        kategorijaPutovanjaDao.update(kategorijaPutovanja);
        return kategorijaPutovanja;
    }


    @Override
    public List<KategorijaPutovanja> findAll() {
        return kategorijaPutovanjaDao.findAll();
    }


    @Override
    public KategorijaPutovanja delete(Long id) {
        KategorijaPutovanja kategorijaPutovanja = findKategorijaPutovanjaById(id);
        if(kategorijaPutovanja != null){
            kategorijaPutovanjaDao.delete(id);
        }
        return kategorijaPutovanja;
    }
}
