package mg.coop.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import mg.coop.model.*;

public class TaxiBrousseDAO {

    private final Connection conn;

    public TaxiBrousseDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(TaxiBrousse t) throws SQLException {
        String sql = "INSERT INTO taxi_brousse(cooperative_id, immatriculation, type_voiture_id) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getCooperative().getId());
            ps.setString(2, t.getImmatriculation());
            ps.setInt(3, t.getTypeVoiture().getId());
            ps.executeUpdate();
        }
    }

    public List<TaxiBrousse> search(String immatriculation, Integer typeVoitureId) throws SQLException {
        List<TaxiBrousse> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT tb.id, tb.immatriculation, " +
            "tv.id tv_id, tv.libelle, tv.nbr_places " +
            "FROM taxi_brousse tb " +
            "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
            "WHERE 1=1"
        );

        if (immatriculation != null && !immatriculation.isEmpty())
            sql.append(" AND tb.immatriculation ILIKE ?");
        if (typeVoitureId != null)
            sql.append(" AND tv.id = ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (immatriculation != null && !immatriculation.isEmpty())
                ps.setString(i++, "%" + immatriculation + "%");
            if (typeVoitureId != null)
                ps.setInt(i++, typeVoitureId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TaxiBrousse t = new TaxiBrousse();
                t.setId(rs.getInt("id"));
                t.setImmatriculation(rs.getString("immatriculation"));

                TypeVoiture tv = new TypeVoiture();
                tv.setId(rs.getInt("tv_id"));
                tv.setLibelle(rs.getString("libelle"));
                tv.setNbrPlaces(rs.getInt("nbr_places"));

                t.setTypeVoiture(tv);
                list.add(t);
            }
        }
        return list;
    }

    public void delete(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM taxi_brousse WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<TaxiBrousse> findAll() throws SQLException {
        List<TaxiBrousse> list = new ArrayList<>();

        String sql =
            "SELECT " +
            "t.id AS taxi_id, t.immatriculation, " +
            "tv.id AS tv_id, tv.libelle, tv.nbr_places, " +
            "c.id AS coop_id, c.nom AS coop_nom " +
            "FROM taxi_brousse t " +
            "JOIN type_voiture tv ON t.type_voiture_id = tv.id " +
            "JOIN cooperative c ON t.cooperative_id = c.id " +
            "ORDER BY t.immatriculation";

        try (PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                // TypeVoiture
                TypeVoiture tv = new TypeVoiture();
                tv.setId(rs.getInt("tv_id"));
                tv.setLibelle(rs.getString("libelle"));
                tv.setNbrPlaces(rs.getInt("nbr_places"));

                // Cooperative
                Cooperative coop = new Cooperative();
                coop.setId(rs.getInt("coop_id"));
                coop.setNom(rs.getString("coop_nom"));

                // TaxiBrousse
                TaxiBrousse taxi = new TaxiBrousse();
                taxi.setId(rs.getInt("taxi_id"));
                taxi.setImmatriculation(rs.getString("immatriculation"));
                taxi.setTypeVoiture(tv);
                taxi.setCooperative(coop);

                list.add(taxi);
            }
        }

        return list;
    }

}

