package mg.coop.dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import mg.coop.model.*;

public class TrajetDAO {

    private final Connection conn;

    public TrajetDAO(Connection conn) {
        this.conn = conn;
    }

    /* ================= CREATE ================= */
    public void create(Trajet t) throws SQLException {
        String sql = """
            INSERT INTO trajet
            (depart, arrivee, distance_km, prix_base, nbr_jour)
            VALUES (?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getDepart());
            ps.setString(2, t.getArrivee());
            ps.setInt(3, t.getDistanceKm());
            ps.setDouble(4, t.getPrixBase());
            ps.setDouble(5, t.getNbrJour());
            ps.executeUpdate();
        }
    }

    /* ================= UPDATE ================= */
    public void update(Trajet t) throws SQLException {
        String sql = """
            UPDATE trajet SET
            depart=?, arrivee=?, distance_km=?, prix_base=?, nbr_jour=?
            WHERE id=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getDepart());
            ps.setString(2, t.getArrivee());
            ps.setInt(3, t.getDistanceKm());
            ps.setDouble(4, t.getPrixBase());
            ps.setDouble(5, t.getNbrJour());
            ps.setInt(7, t.getId());
            ps.executeUpdate();
        }
    }

    /* ================= DELETE ================= */
    public void delete(int id) throws SQLException {
        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM trajet WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* ================= FIND BY ID ================= */
    public Trajet findById(int id) throws SQLException {
        String sql = "SELECT * FROM trajet WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    /* ================= SEARCH ================= */
    public List<Trajet> search(String depart, String arrivee) throws SQLException {
        List<Trajet> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM trajet WHERE 1=1");

        if (depart != null && !depart.isEmpty())
            sql.append(" AND depart ILIKE ?");
        if (arrivee != null && !arrivee.isEmpty())
            sql.append(" AND arrivee ILIKE ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (depart != null && !depart.isEmpty())
                ps.setString(i++, "%" + depart + "%");
            if (arrivee != null && !arrivee.isEmpty())
                ps.setString(i++, "%" + arrivee + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private Trajet map(ResultSet rs) throws SQLException {
        Trajet t = new Trajet();
        t.setId(rs.getInt("id"));
        t.setDepart(rs.getString("depart"));
        t.setArrivee(rs.getString("arrivee"));
        t.setDistanceKm(rs.getInt("distance_km"));
        t.setPrixBase(rs.getDouble("prix_base"));
        t.setNbrJour(rs.getDouble("nombre_jour"));
        return t;
    }
}

