package com.example.owpturistickaagencija.Daos;

import com.example.owpturistickaagencija.Models.Kupac;

import java.util.List;

public interface KupacDao {
    public Kupac findOne(Long id);

    public List<Kupac> findAll();

    public int save(Kupac kupac);

    public int update(Kupac kupac);

    public int delete(Long id);
}
