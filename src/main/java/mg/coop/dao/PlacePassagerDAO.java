package mg.coop.dao;

import java.sql.*;
import mg.coop.model.PlacePassager;
import mg.coop.model.TypePassager;
import mg.coop.model.CategoriePlace;

public class PlacePassagerDAO {

    private Connection conn;

    public PlacePassagerDAO(Connection conn) {
        this.conn = conn;
    }

    public int findOrCreate(int categId, int typePassagerId) throws SQLException {

        String select = """
            SELECT id FROM place_passager
            WHERE id_categ_place = ? AND id_type_passager = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setInt(1, categId);
            ps.setInt(2, typePassagerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        String insert = """
            INSERT INTO place_passager (id_categ_place, id_type_passager)
            VALUES (?, ?)
            RETURNING id
        """;

        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setInt(1, categId);
            ps.setInt(2, typePassagerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        throw new SQLException("Erreur place_passager");
    }

    public PlacePassager findOrCreateObj(int idCategPlace, int idTypePassager) throws SQLException {
        String sqlSelect = "SELECT id, id_categ_place, id_type_passager, prix FROM place_passager WHERE id_categ_place = ? AND id_type_passager = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
            ps.setInt(1, idCategPlace);
            ps.setInt(2, idTypePassager);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PlacePassager pp = new PlacePassager();
                pp.setId(rs.getInt("id"));
                CategoriePlace cp = new CategoriePlace();
                cp.setId(idCategPlace);

                pp.setCategoriePlace(cp);

                TypePassager tp = new TypePassager();
                tp.setId(idTypePassager);

                pp.setTypePassager(tp);

                pp.setPrix(rs.getDouble("prix"));
                return pp;
            }
        }

        // Si inexistant, créer
        String sqlInsert = "INSERT INTO place_passager (id_categ_place, id_type_passager, prix) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setInt(1, idCategPlace);
            ps.setInt(2, idTypePassager);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PlacePassager pp = new PlacePassager();
                pp.setId(rs.getInt("id"));
                CategoriePlace cp = new CategoriePlace();
                cp.setId(idCategPlace);

                pp.setCategoriePlace(cp);

                TypePassager tp = new TypePassager();
                tp.setId(idTypePassager);

                pp.setTypePassager(tp);

                pp.setPrix(rs.getDouble("prix"));
                return pp;
            }
        }
        return null; // ne devrait pas arriver
    }

}


