package mg.coop.dao;


import mg.coop.config.DatabaseConfig;
import mg.coop.model.TaxiTrajet;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaxiTrajetDAO {
    
    public List<TaxiTrajet> rechercherTrajets(String depart, String arrivee, 
                                              String dateDepart, String heureDepart) 
                                              throws Exception {
        List<TaxiTrajet> trajets = new ArrayList<>(); //voyage 
        
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
                trajet.setDateHeureDepart(rs.getObject("date_heure_depart",LocalDateTime.class));
                trajet.setCooperative(rs.getString("cooperative"));
                trajet.setPlacesReservees(rs.getInt("places_reservees"));
                
                trajets.add(trajet);
            }
        }
        
        return trajets;
    }
    
    public static List<String> getLieuxDepart() throws Exception {
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
    
    public static List<String> getLieuxArrivee() throws Exception {
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

    public static List<TaxiTrajet> findAll() throws Exception {
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
            "GROUP BY tt.id, tb.immatriculation, tv.libelle, tv.nbr_places, " +
               "t.depart, t.arrivee, t.distance_km, t.prix_base, p.nom, c.nom " +
               "ORDER BY tt.date_heure_depart"; 
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                TaxiTrajet trajet = new TaxiTrajet();
                trajet.setId(rs.getInt("id"));
                trajet.setTaxiId(rs.getInt("taxi_id"));
                trajet.setTrajetId(rs.getInt("trajet_id"));
                trajet.setChauffeurId(rs.getInt("chauffeur_id"));
                trajet.setDateHeureDepart(rs.getObject("date_heure_depart",LocalDateTime.class));

                // Set additional fields
                trajet.setImmatriculation(rs.getString("immatriculation"));
                trajet.setTypeVoiture(rs.getString("type_voiture"));
                trajet.setNbrPlaces(rs.getInt("nbr_places"));
                trajet.setDepart(rs.getString("depart"));
                trajet.setArrivee(rs.getString("arrivee"));
                trajet.setDistanceKm(rs.getDouble("distance_km"));
                trajet.setPrixBase(rs.getDouble("prix_base"));
                trajet.setNomChauffeur(rs.getString("nom_chauffeur"));
                trajet.setCooperative(rs.getString("cooperative"));
                trajet.setPlacesReservees(rs.getInt("places_reservees"));

                 trajets.add(trajet);
             }
        }
        return trajets;
    }

public TaxiTrajet findById(int id) throws Exception {
    TaxiTrajet t = null;

    String sql = """
        SELECT tt.id, tt.taxi_id, tb.immatriculation, tb.type_voiture, tb.nbr_places,
               tt.trajet_id, tr.depart, tr.arrivee, tr.distance_km, tr.prix_base,
               tt.chauffeur_id, p.nom AS nom_chauffeur,
               tt.aide_chauffeur_id, ap.nom AS nom_aide_chauffeur,
               tt.date_heure_depart
        FROM taxi_trajet tt
        JOIN taxi_brousse tb ON tt.taxi_id = tb.id
        JOIN trajet tr ON tt.trajet_id = tr.id
        JOIN personne p ON tt.chauffeur_id = p.id
        LEFT JOIN personne ap ON tt.aide_chauffeur_id = ap.id
        WHERE tt.id = ?
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                t = new TaxiTrajet();
                t.setId(rs.getInt("id"));
                t.setTaxiId(rs.getInt("taxi_id"));
                t.setImmatriculation(rs.getString("immatriculation"));
                t.setTypeVoiture(rs.getString("type_voiture"));
                t.setNbrPlaces(rs.getInt("nbr_places"));

                t.setTrajetId(rs.getInt("trajet_id"));
                t.setDepart(rs.getString("depart"));
                t.setArrivee(rs.getString("arrivee"));
                t.setDistanceKm(rs.getDouble("distance_km"));
                t.setPrixBase(rs.getDouble("prix_base"));

                t.setChauffeurId(rs.getInt("chauffeur_id"));
                t.setNomChauffeur(rs.getString("nom_chauffeur"));

                t.setAideChauffeurId((Integer) rs.getObject("aide_chauffeur_id"));
                t.setNomAideChauffeur(rs.getString("nom_aide_chauffeur"));

                t.setDateHeureDepart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
            }
        }
    }

    return t;
}


public void save(TaxiTrajet taxiTrajet) throws Exception {
    String sql = """
        INSERT INTO taxi_trajet 
        (taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart)
        VALUES (?, ?, ?, ?, ?)
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, taxiTrajet.getTaxiId());
        ps.setInt(2, taxiTrajet.getTrajetId());
        ps.setInt(3, taxiTrajet.getChauffeurId());

        if (taxiTrajet.getAideChauffeurId() != null) {
            ps.setInt(4, taxiTrajet.getAideChauffeurId());
        } else {
            ps.setNull(4, java.sql.Types.INTEGER);
        }

        // Conversion LocalDateTime -> Timestamp
        ps.setTimestamp(5, java.sql.Timestamp.valueOf(taxiTrajet.getDateHeureDepart()));

        ps.executeUpdate();
    }
}
public void update(TaxiTrajet taxiTrajet) throws Exception {
    String sql = """
        UPDATE taxi_trajet SET
            taxi_id = ?,
            trajet_id = ?,
            chauffeur_id = ?,
            aide_chauffeur_id = ?,
            date_heure_depart = ?
        WHERE id = ?
    """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, taxiTrajet.getTaxiId());
        ps.setInt(2, taxiTrajet.getTrajetId());
        ps.setInt(3, taxiTrajet.getChauffeurId());

        if (taxiTrajet.getAideChauffeurId() != null) {
            ps.setInt(4, taxiTrajet.getAideChauffeurId());
        } else {
            ps.setNull(4, java.sql.Types.INTEGER);
        }

        ps.setTimestamp(5, java.sql.Timestamp.valueOf(taxiTrajet.getDateHeureDepart()));
        ps.setInt(6, taxiTrajet.getId());

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated == 0) {
            throw new Exception("Aucun taxi_trajet mis à jour pour l'ID=" + taxiTrajet.getId());
        }
    }
}

public static void delete(TaxiTrajet taxiTrajet) throws Exception {
    String sql = "DELETE FROM taxi_trajet WHERE id = ?";

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, taxiTrajet.getId());

        int rowsDeleted = ps.executeUpdate();
        if (rowsDeleted == 0) {
            throw new Exception("Aucun taxi_trajet supprimé pour l'ID=" + taxiTrajet.getId());
        }
    }
}

/*
public static void main(String[] args) throws Exception {
        TaxiTrajetDAO dao = new TaxiTrajetDAO();
        List<TaxiTrajet> trajets = TaxiTrajetDAO.findAll();
        for (TaxiTrajet t : trajets) {
            System.out.println(t.getImmatriculation() + t.getNomChauffeur());
                }
    }
 */

}