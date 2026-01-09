package mg.coop.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.Personne;

public class PersonneDAO {
    public List<Personne> findAll() throws Exception {
        List<Personne> list = new ArrayList<>();
        String sql = "SELECT * FROM personne";

        try (Connection c = DatabaseConfig.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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

    public void add(Personne p) throws Exception {
    String sql = "INSERT INTO personne (nom, telephone, roles) VALUES (?, ?, ?)";
    
    try (Connection c = DatabaseConfig.getConnection();
         java.sql.PreparedStatement pst = c.prepareStatement(sql)) {
         
        pst.setString(1, p.getNom());
        pst.setString(2, p.getTelephone());
        pst.setString(3, p.getRole());
        pst.executeUpdate();
    }
}

public void update(Personne p) throws Exception {
    String sql = "UPDATE personne SET nom=?, telephone=?, roles=? WHERE id=?";
    
    try (Connection c = DatabaseConfig.getConnection();
         java.sql.PreparedStatement pst = c.prepareStatement(sql)) {
         
        pst.setString(1, p.getNom());
        pst.setString(2, p.getTelephone());
        pst.setString(3, p.getRole());
        pst.setInt(4, p.getId());
        pst.executeUpdate();
    }
}
public void delete(int id) throws Exception {
    String sql = "DELETE FROM personne WHERE id=?";
    
    try (Connection c = DatabaseConfig.getConnection();
         java.sql.PreparedStatement pst = c.prepareStatement(sql)) {
         
        pst.setInt(1, id);
        pst.executeUpdate();
    }
}

}
