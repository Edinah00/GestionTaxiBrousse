package mg.coop.dao;


import mg.coop.config.DatabaseConfig;
import mg.coop.model.TaxiTrajet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxiTrajetDAO {
    
    public List<TaxiTrajet> rechercherTrajets(String depart, String arrivee, 
                                              String dateDepart, String heureDepart) 
                                              throws Exception {
        List<TaxiTrajet> trajets = new ArrayList<>();
        
        String sql = "SELECT " +
            "tt.id, tt.taxi_id, tb.immatriculation, tv.libelle AS type_voiture, " +
            "tv.nbr_places, tt.trajet_id, t.depart, t.arrivee, t.distance_km, " +
            "t.prix_base, tt.chauffeur_id, p.nom AS nom_chauffeur, " +
            "tt.date_heure_depart, c.nom AS cooperative, " +
            "COALESCE(SUM(r.nb_places), 0) AS places_reservees " +
            "FROM taxi_trajet tt " +
            "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
            "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
            "JOIN trajet t ON tt.trajet_id = t.id " +
            "JOIN personne p ON tt.chauffeur_id = p.id " +
            "JOIN cooperative c ON tb.cooperative_id = c.id " +
            "LEFT JOIN reservation r ON tt.id = r.taxi_trajet_id " +
            "WHERE LOWER(t.depart) LIKE LOWER(?) " +
            "AND LOWER(t.arrivee) LIKE LOWER(?) ";
        
        if (dateDepart != null && !dateDepart.isEmpty()) {
            sql += "AND DATE(tt.date_heure_depart) = ? ";
        }
        if (heureDepart != null && !heureDepart.isEmpty()) {
            sql += "AND EXTRACT(HOUR FROM tt.date_heure_depart) >= ? ";
        }
        
        sql += "GROUP BY tt.id, tb.immatriculation, tv.libelle, tv.nbr_places, " +
               "t.depart, t.arrivee, t.distance_km, t.prix_base, p.nom, c.nom " +
               "ORDER BY tt.date_heure_depart";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql)) {
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, "%" + depart + "%");
            stmt.setString(paramIndex++, "%" + arrivee + "%");
            
            if (dateDepart != null && !dateDepart.isEmpty()) {
                stmt.setDate(paramIndex++, Date.valueOf(dateDepart));
            }
            if (heureDepart != null && !heureDepart.isEmpty()) {
                stmt.setInt(paramIndex++, Integer.parseInt(heureDepart.split(":")[0]));
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TaxiTrajet trajet = new TaxiTrajet();
                trajet.setId(rs.getInt("id"));
                trajet.setTaxiId(rs.getInt("taxi_id"));
                trajet.setImmatriculation(rs.getString("immatriculation"));
                trajet.setTypeVoiture(rs.getString("type_voiture"));
                trajet.setNbrPlaces(rs.getInt("nbr_places"));
                trajet.setTrajetId(rs.getInt("trajet_id"));
                trajet.setDepart(rs.getString("depart"));
                trajet.setArrivee(rs.getString("arrivee"));
                trajet.setDistanceKm(rs.getInt("distance_km"));
                trajet.setPrixBase(rs.getDouble("prix_base"));
                trajet.setChauffeurId(rs.getInt("chauffeur_id"));
                trajet.setNomChauffeur(rs.getString("nom_chauffeur"));
                trajet.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                trajet.setCooperative(rs.getString("cooperative"));
                trajet.setPlacesReservees(rs.getInt("places_reservees"));
                
                trajets.add(trajet);
            }
        }
        
        return trajets;
    }
    
    public List<String> getLieuxDepart() throws Exception {
        List<String> lieux = new ArrayList<>();
        String sql = "SELECT DISTINCT depart FROM trajet ORDER BY depart";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lieux.add(rs.getString("depart"));
            }
        }
        return lieux;
    }
    
    public List<String> getLieuxArrivee() throws Exception {
        List<String> lieux = new ArrayList<>();
        String sql = "SELECT DISTINCT arrivee FROM trajet ORDER BY arrivee";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lieux.add(rs.getString("arrivee"));
            }
        }
        return lieux;
    }
}
