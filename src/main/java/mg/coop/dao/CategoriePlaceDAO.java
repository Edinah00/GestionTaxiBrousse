package mg.coop.dao;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

import mg.coop.model.CategoriePlace;

public class CategoriePlaceDAO {

    private Connection conn;

    public CategoriePlaceDAO(Connection conn) {
        this.conn = conn;
    }

    public List<CategoriePlace> findAll() throws SQLException {
        List<CategoriePlace> list = new ArrayList<>();
        String sql = "SELECT * FROM categorie_place";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CategoriePlace cp = new CategoriePlace();
                cp.setId(rs.getInt("id"));
                cp.setLibelle(rs.getString("libelle"));
                cp.setPrix(rs.getDouble("prix"));
                
                list.add(cp);
            }
        }
        return list;
    }
}
