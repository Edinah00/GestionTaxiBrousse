package mg.coop.dao;

import mg.coop.model.Cooperative;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CooperativeDAO {

    private Connection conn;

    public CooperativeDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Cooperative coop) throws SQLException {
        String sql = "INSERT INTO cooperative (nom) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coop.getNom());
            ps.executeUpdate();
        }
    }

    public void update(Cooperative coop) throws SQLException {
        String sql = "UPDATE cooperative SET nom=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coop.getNom());
            ps.setInt(2, coop.getId());
            ps.executeUpdate();
        }
    }

    public Cooperative findById(int id) throws SQLException {
        String sql = "SELECT * FROM cooperative WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cooperative coop = new Cooperative();
                coop.setId(rs.getInt("id"));
                coop.setNom(rs.getString("nom"));
                return coop;
            }
        }
        return null;
    }

    public List<Cooperative> findAll() throws SQLException {
        List<Cooperative> list = new ArrayList<>();
        String sql = "SELECT * FROM cooperative ORDER BY nom";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cooperative coop = new Cooperative();
                coop.setId(rs.getInt("id"));
                coop.setNom(rs.getString("nom"));
                list.add(coop);
            }
        }
        return list;
    }
}