package mg.coop.dao;

import mg.coop.model.TypeVoiture;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeVoitureDAO {

    private Connection conn;

    public TypeVoitureDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(TypeVoiture tv) throws SQLException {
        String sql = "INSERT INTO type_voiture (libelle, nbr_places) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tv.getLibelle());
            ps.setInt(2, tv.getNbrPlaces());
            ps.executeUpdate();
        }
    }

    public void update(TypeVoiture tv) throws SQLException {
        String sql = "UPDATE type_voiture SET libelle=?, nbr_places=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tv.getLibelle());
            ps.setInt(2, tv.getNbrPlaces());
            ps.setInt(3, tv.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM type_voiture WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public TypeVoiture findById(int id) throws SQLException {
        String sql = "SELECT * FROM type_voiture WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TypeVoiture tv = new TypeVoiture();
                tv.setId(rs.getInt("id"));
                tv.setLibelle(rs.getString("libelle"));
                tv.setNbrPlaces(rs.getInt("nbr_places"));
                return tv;
            }
        }
        return null;
    }

    public List<TypeVoiture> findAll() throws SQLException {
        List<TypeVoiture> list = new ArrayList<>();
        String sql = "SELECT * FROM type_voiture ORDER BY libelle";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TypeVoiture tv = new TypeVoiture();
                tv.setId(rs.getInt("id"));
                tv.setLibelle(rs.getString("libelle"));
                tv.setNbrPlaces(rs.getInt("nbr_places"));
                list.add(tv);
            }
        }
        return list;
    }
}