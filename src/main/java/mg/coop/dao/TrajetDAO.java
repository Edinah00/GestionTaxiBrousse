package mg.coop.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.Trajet;

public class TrajetDAO {
    public static List<Trajet> findAll() throws SQLException, Exception {
        // Récupérer tous les trajets depuis la base de données
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Trajet t = new Trajet();
                t.setId(rs.getInt("id"));
                t.setDepart(rs.getString("depart"));
                t.setArrivee(rs.getString("arrivee"));
                t.setDistanceKm(rs.getInt("distance_km"));
                t.setPrixBase(rs.getDouble("prix_base"));
                t.setPourcentageAugmentation(rs.getDouble("pourcentage_augmentation"));
                t.setNombreJour(
                rs.getInt("nombre_jour"));
                trajets.add(t);
            }
        }
        return trajets;
    }
    public static void findById(Trajet trajet) throws SQLException, Exception {
        // Récupérer tous les trajets depuis la base de données
                        

        String sql = "SELECT * FROM trajet where id="+trajet.getId();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trajet.setId(rs.getInt("id"));
                trajet.setDepart(rs.getString("depart"));
                trajet.setArrivee(rs.getString("arrivee"));
                trajet.setDistanceKm(rs.getInt("distance_km"));
                trajet.setPrixBase(rs.getDouble("prix_base"));
                trajet.setPourcentageAugmentation(rs.getDouble("pourcentage_augmentation"));
                trajet.setNombreJour(
                rs.getInt("nombre_jour"));

                
            }
        }
       
    }
public void save(Trajet trajet) throws Exception {

    String sql = """
        INSERT INTO trajet 
        (depart, arrivee, distance_km, prix_base, pourcentage_augmentation, nombre_jour)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, trajet.getDepart());
        ps.setString(2, trajet.getArrivee());
        ps.setInt(3, trajet.getDistanceKm());
        ps.setDouble(4, trajet.getPrixBase());
        ps.setDouble(5, trajet.getPourcentageAugmentation());
        ps.setInt(6, trajet.getNombreJour());

        ps.executeUpdate();
    }
}
public void update(Trajet trajet) throws Exception {

    String sql = """
        UPDATE trajet SET 
        depart = ?, 
        arrivee = ?, 
        distance_km = ?, 
        prix_base = ?, 
        pourcentage_augmentation = ?, 
        nombre_jour = ?
        WHERE id = ?
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, trajet.getDepart());
        ps.setString(2, trajet.getArrivee());
        ps.setInt(3, trajet.getDistanceKm());
        ps.setDouble(4, trajet.getPrixBase());
        ps.setDouble(5, trajet.getPourcentageAugmentation());
        ps.setInt(6, trajet.getNombreJour());
        ps.setInt(7, trajet.getId());

        ps.executeUpdate();
    }


}
public void delete(Trajet trajet) throws Exception {
    String sqlDeleteTrajet = "DELETE FROM trajet WHERE id = ?";

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlDeleteTrajet)) {

        ps.setInt(1, trajet.getId());
        ps.executeUpdate();
    }
}




public void getAllDepart() {
    // TODO Auto-generated method stub EFA MISY AO AM TAXI TRAJET

} //OTRAN ZAY KOA NY ARRIVEE

public List<Trajet> search(String depart, String arrivee, String dateDepart, String heureDepart) throws Exception {
    List<Trajet> trajets = new ArrayList<>();
    String sql = "SELECT * FROM trajet WHERE depart LIKE ? AND arrivee LIKE ?";
    
    if (dateDepart != null && !dateDepart.isEmpty()) {
        sql += " AND date_heure_depart >= ?";
    }
    if (heureDepart != null && !heureDepart.isEmpty()) {
        sql += " AND EXTRACT(HOUR FROM date_heure_depart) = ?";
    }
    
    try (Connection c = DatabaseConfig.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        
        int paramIndex = 1;
        stmt.setString(paramIndex++, "%" + depart + "%");
        stmt.setString(paramIndex++, "%" + arrivee + "%");
        
        if (dateDepart != null && !dateDepart.isEmpty()) {
            stmt.setDate(paramIndex++, java.sql.Date.valueOf(dateDepart));
        }
        if (heureDepart != null && !heureDepart.isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(heureDepart.split(":")[0]));
        }
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Trajet trajet = new Trajet();
            trajet.setId(rs.getInt("id"));
            trajet.setDepart(rs.getString("depart"));
            trajet.setArrivee(rs.getString("arrivee"));
            trajet.setDistanceKm(rs.getInt("distance_km"));
            trajet.setPrixBase(rs.getDouble("prix_base"));
            trajet.setPourcentageAugmentation(rs.getDouble("pourcentage_augmentation"));
            trajet.setNombreJour(rs.getInt("nombre_jour"));
            
            trajets.add(trajet);
        }
    }
    
    return trajets;
}
public static boolean hasTaxiTrajets(int id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'hasTaxiTrajets'");
}
}
