package com.example.owpturistickaagencija.Daos.impl;

import com.example.owpturistickaagencija.Daos.KupacDao;
import com.example.owpturistickaagencija.Daos.PutovanjeDao;
import com.example.owpturistickaagencija.Daos.RezervacijaDao;
import com.example.owpturistickaagencija.Models.Kupac;
import com.example.owpturistickaagencija.Models.Putovanje;
import com.example.owpturistickaagencija.Models.Rezervacija;
import com.example.owpturistickaagencija.Models.StatusRezervacije;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Repository
public class RezervacijaDaoImpl implements RezervacijaDao {
    private final JdbcTemplate jdbcTemplate;
    private final PutovanjeDao putovanjeDao;
    private final KupacDao kupacDao;
    public RezervacijaDaoImpl(JdbcTemplate jdbcTemplate, PutovanjeDao putovanjeDao, KupacDao kupacDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.putovanjeDao = putovanjeDao;
        this.kupacDao = kupacDao;
    }

    private class RezervacijaRowCallBackHandler implements RowCallbackHandler {
        private final Map<Long, Rezervacija> RezervacijaMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            LocalDateTime datumIVremeRezervacije = rs.getTimestamp(index++).toLocalDateTime();
            Long brojPutnika = rs.getLong(index++);
            Long putovanjeId = rs.getLong(index++);
            Long kupacId = rs.getLong(index++);

            Putovanje putovanje = putovanjeDao.findOne(putovanjeId);
            Kupac kupac = kupacDao.findOne(kupacId);

            Rezervacija rezervacija = RezervacijaMap.get(id);
            if (rezervacija == null) {
                rezervacija = new Rezervacija(id, datumIVremeRezervacije, brojPutnika, putovanje, kupac);
                RezervacijaMap.put(rezervacija.getId(), rezervacija);
            }
        }

        public List<Rezervacija> getRezervacije() {
            return new ArrayList<>(RezervacijaMap.values());
        }
    }

    @Override
    public Rezervacija findOne(Long id) {
        String sql = "SELECT r.id, r.datumIVremeRezervacije, r.brojPutnika, r.putovanjeId, r.kupacId "
                + "FROM rezervacije r " +
                "WHERE r.id = ? " + "ORDER BY r.id ";
        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, id);

        if(rowCallBackHandler.getRezervacije().size() == 0){
            return null;
        }
        return rowCallBackHandler.getRezervacije().get(0);
    }

    @Override
    public List<Rezervacija> findAll(){
        String sql =
                "SELECT r.id, r.datumIVremeRezervacije, r.brojPutnika, r.putovanjeId, r.kupacId "
                        + "FROM rezervacije r " + "ORDER BY r.id ";

        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler);

        if(rowCallBackHandler.getRezervacije().size() == 0){
            return null;
        }
        return rowCallBackHandler.getRezervacije();
    }

    @Transactional
    @Override
    public int save(Rezervacija rezervacija){
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO rezervacije (datumIVremeRezervacije, brojPutnika, putovanjeId, kupacId) VALUES (?, ?, ?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                Timestamp timestamp = Timestamp.valueOf(rezervacija.getDatumIVremeRezervacije());
                preparedStatement.setString(index++, timestamp.toString());
                preparedStatement.setLong(index++, rezervacija.getBrojPutnika());

                //preparedStatement.setLong(index++, rezervacija.getKupacId());
                if (rezervacija.getPutovanjeId() != null){
                    preparedStatement.setLong(index++, rezervacija.getPutovanjeId());
                } else{
                    preparedStatement.setNull(index++, Types.BIGINT);
                }
                // Handle null for kupacId
                if (rezervacija.getKupacId() != null) {
                    preparedStatement.setLong(index++, rezervacija.getKupacId());
                } else {
                    preparedStatement.setNull(index++, Types.BIGINT);
                }

                return preparedStatement;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return uspeh ? 1 : 0;
    }

    @Override
    public int update(Rezervacija rezervacija) {
        String sql =
                "UPDATE rezervacije SET datumIVremeRezervacije = ?, brojPutnika = ?, putovanjeId = ?, kupacId = ? WHERE id = ? ";
        boolean uspeh = jdbcTemplate.update(sql, Timestamp.valueOf(rezervacija.getDatumIVremeRezervacije()).toString(),
                rezervacija.getBrojPutnika(),
                rezervacija.getPutovanjeId(), rezervacija.getKupacId(), rezervacija.getId())== 1;
        return uspeh ? 1 : 0 ;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = " DELETE FROM rezervacije WHERE id = ? ";
        return jdbcTemplate.update(sql, id);
    }

    @Transactional
    @Override
    public void deleteByKupac(Long kupacId, Long id) {
        String sql = " DELETE FROM rezervacije WHERE kupacId = ? AND id <> ? ";

        jdbcTemplate.update(sql, kupacId, id);
    }

    @Override
    public List<Rezervacija> pretraziRezervacije(String upit){
        String sql = "SELECT r.id, r.datumIVremeRezervacije, r.brojPutnika, r.putovanjeId, r.kupacId " +
                "FROM rezervacije r " +
                "JOIN korisnici k ON r.kupacId = k.id " +
                "WHERE k.ime LIKE ? OR k.prezime LIKE ? or k.jmbg LIKE ? " +
                "ORDER BY r.id ";
        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        String upitU = "%" + upit + "%";
        jdbcTemplate.query(sql, rowCallBackHandler, upitU, upitU, upitU);

        return rowCallBackHandler.getRezervacije();
    }
    @Override
    public List<Rezervacija> findByPutovanjeId(Long putovanjeId) {
        String sql = "SELECT * FROM rezervacije WHERE putovanjeId = ?";
        RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, putovanjeId);

        return new ArrayList<>(rowCallBackHandler.RezervacijaMap.values());
    }

    @Override
    public List<Rezervacija> getAktivneRezervacijeByKupacId(Long kupacId) {
        String sql =
                "SELECT r.id, r.datumIVremeRezervacije, r.brojPutnika, r.putovanjeId, r.kupacId "
                        + "FROM rezervacije r "
                        + "WHERE r.kupacId = ? "
                        + "ORDER BY r.id";

        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, kupacId);

        if (rowCallBackHandler.getRezervacije().isEmpty()) {
            return null;
        }
        return rowCallBackHandler.getRezervacije();
    }


}