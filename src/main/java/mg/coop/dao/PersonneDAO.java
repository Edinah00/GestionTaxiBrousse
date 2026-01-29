package mg.coop.dao;

import mg.coop.model.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PersonneDAO {

    private final Connection conn;

    public PersonneDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Personne p) throws SQLException {
        String sql = "INSERT INTO personne(nom, telephone, roles) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getTelephone());
            ps.setString(3, p.getRole());
            ps.executeUpdate();
        }
    }

    public void update(Personne p) throws SQLException {
        String sql = "UPDATE personne SET nom=?, telephone=?, roles=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getTelephone());
            ps.setString(3, p.getRole());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    public Personne findById(int id) throws SQLException {
        String sql = "SELECT * FROM personne WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Personne p = new Personne();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setTelephone(rs.getString("telephone"));
                p.setRole(rs.getString("roles"));
                return p;
            }
        }
        return null;
    }

    public List<Personne> search(String nom, String role) throws SQLException {
        List<Personne> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM personne WHERE 1=1");

        if (nom != null && !nom.isEmpty()) sql.append(" AND nom ILIKE ?");
        if (role != null && !role.isEmpty()) sql.append(" AND roles = ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (nom != null && !nom.isEmpty()) ps.setString(i++, "%" + nom + "%");
            if (role != null && !role.isEmpty()) ps.setString(i++, role);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Personne p = new Personne();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setTelephone(rs.getString("telephone"));
                p.setRole(rs.getString("roles"));
                list.add(p);
            }
        }
        return list;
    }

    public List<Personne> findByRole(String role) throws SQLException {
        List<Personne> list = new ArrayList<>();

        String sql = "SELECT id, nom, telephone, roles " +
                    "FROM personne " +
                    "WHERE roles = ? " +
                    "ORDER BY nom";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Personne p = new Personne();
                    p.setId(rs.getInt("id"));
                    p.setNom(rs.getString("nom"));
                    p.setTelephone(rs.getString("telephone"));
                    p.setRole(rs.getString("roles"));

                    list.add(p);
                }
            }
        }

        return list;
    }

}
