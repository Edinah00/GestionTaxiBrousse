package mg.coop.dao;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

import mg.coop.model.TypePassager;

public class TypePassagerDAO {

    private Connection conn;

    public TypePassagerDAO(Connection conn) {
        this.conn = conn;
    }

    public List<TypePassager> findAll() throws SQLException {
        List<TypePassager> list = new ArrayList<>();
        String sql = "SELECT * FROM type_passager";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TypePassager cp = new TypePassager();
                cp.setId(rs.getInt("id"));
                cp.setLibelle(rs.getString("libelle"));
                
                list.add(cp);
            }
        }
        return list;
    }
}
