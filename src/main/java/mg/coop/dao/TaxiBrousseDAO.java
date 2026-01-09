package mg.coop.dao;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.TaxiBrousse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxiBrousseDAO {

    public List<TaxiBrousse> findAll() throws Exception {
        List<TaxiBrousse> list = new ArrayList<>();
        String sql = "SELECT * FROM taxi_brousse";

        try (Connection c = DatabaseConfig.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                TaxiBrousse t = new TaxiBrousse();
                t.setId(rs.getInt("id"));
                t.setCooperativeId(rs.getInt("cooperative_id"));
                t.setTypeVoitureId(rs.getInt("type_voiture_id"));
                t.setImmatriculation(rs.getString("immatriculation"));
                list.add(t);
            }
        }
        return list;
    }

    public TaxiBrousse findById(int id) throws Exception {
        String sql = "SELECT * FROM taxi_brousse WHERE id=?";
        TaxiBrousse t = null;

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = new TaxiBrousse();
                t.setId(rs.getInt("id"));
                t.setCooperativeId(rs.getInt("cooperative_id"));
                t.setTypeVoitureId(rs.getInt("type_voiture_id"));
                t.setImmatriculation(rs.getString("immatriculation"));
            }
        }
        return t;
    }

    public void insert(TaxiBrousse t) throws Exception {
        String sql = """
            INSERT INTO taxi_brousse (cooperative_id, type_voiture_id, immatriculation)
            VALUES (?, ?, ?)
        """;

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, t.getCooperativeId());
            ps.setInt(2, t.getTypeVoitureId());
            ps.setString(3, t.getImmatriculation());
            ps.executeUpdate();
        }
    }

    public void update(TaxiBrousse t) throws Exception {
        String sql = """
            UPDATE taxi_brousse
            SET cooperative_id=?, type_voiture_id=?, immatriculation=?
            WHERE id=?
        """;

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, t.getCooperativeId());
            ps.setInt(2, t.getTypeVoitureId());
            ps.setString(3, t.getImmatriculation());
            ps.setInt(4, t.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM taxi_brousse WHERE id=?";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean isUsed(int taxiId) throws SQLException, Exception {
        String sql = "SELECT COUNT(*) FROM taxi_trajet WHERE taxi_id = ?";
        try (Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, taxiId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }


}
