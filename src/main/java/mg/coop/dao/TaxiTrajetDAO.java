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

                double valMax = TaxiTrajetDAO.getValeurMax(trajet);
                trajet.setValMax(valMax);

                 trajets.add(trajet);
             }
        }
        return trajets;
    }public TaxiTrajet findById(int id) throws Exception {
        String sql = "SELECT tt.id, tt.taxi_id, tt.trajet_id, tt.chauffeur_id, " +
                     "tt.aide_chauffeur_id, tt.date_heure_depart, " +
                     "tb.immatriculation, tv.libelle AS type_voiture, tv.nbr_places, " +
                     "tr.depart, tr.arrivee, tr.distance_km, tr.prix_base, " +
                     "p.nom AS nom_chauffeur, ap.nom AS nom_aide_chauffeur " +
                     "FROM taxi_trajet tt " +
                     "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
                     "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
                     "JOIN trajet tr ON tt.trajet_id = tr.id " +
                     "JOIN personne p ON tt.chauffeur_id = p.id " +
                     "LEFT JOIN personne ap ON tt.aide_chauffeur_id = ap.id " +
                     "WHERE tt.id = ?";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TaxiTrajet tt = new TaxiTrajet();
                tt.setId(rs.getInt("id"));
                tt.setTaxiId(rs.getInt("taxi_id"));
                tt.setTrajetId(rs.getInt("trajet_id"));
                tt.setChauffeurId(rs.getInt("chauffeur_id"));
                
                int aideId = rs.getInt("aide_chauffeur_id");
                if (!rs.wasNull()) {
                    tt.setAideChauffeurId(aideId);
                }
                
                tt.setDateHeureDepart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
                tt.setImmatriculation(rs.getString("immatriculation"));
                tt.setTypeVoiture(rs.getString("type_voiture"));
                tt.setNbrPlaces(rs.getInt("nbr_places"));
                tt.setDepart(rs.getString("depart"));
                tt.setArrivee(rs.getString("arrivee"));
                tt.setDistanceKm(rs.getDouble("distance_km"));
                tt.setPrixBase(rs.getDouble("prix_base"));
                tt.setNomChauffeur(rs.getString("nom_chauffeur"));
                tt.setNomAideChauffeur(rs.getString("nom_aide_chauffeur"));

                return tt;
            }
        }
        return null;
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
public List<String> getAllTrajets() throws Exception {
    List<String> trajets = new ArrayList<>();

    String sql = "SELECT DISTINCT depart || ' → ' || arrivee AS trajet FROM trajet ORDER BY trajet";

    try (Connection c = DatabaseConfig.getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            trajets.add(rs.getString("trajet"));
        }
    }
    return trajets;
}public List<TaxiTrajet> findByTrajet(String trajet) throws Exception {
    List<TaxiTrajet> trajets = new ArrayList<>();

    String sql = """
        SELECT
            tt.id, tt.taxi_id, tb.immatriculation, tv.libelle AS type_voiture,
            tv.nbr_places, tt.trajet_id, t.depart, t.arrivee, t.distance_km,
            t.prix_base, tt.chauffeur_id, p.nom AS nom_chauffeur,
            tt.date_heure_depart, c.nom AS cooperative,
            COALESCE(SUM(r.nb_places), 0) AS places_reservees
        FROM taxi_trajet tt
        JOIN taxi_brousse tb ON tt.taxi_id = tb.id
        JOIN type_voiture tv ON tb.type_voiture_id = tv.id
        JOIN trajet t ON tt.trajet_id = t.id
        JOIN personne p ON tt.chauffeur_id = p.id
        JOIN cooperative c ON tb.cooperative_id = c.id
        LEFT JOIN reservation r ON tt.id = r.taxi_trajet_id
        WHERE (t.depart || ' → ' || t.arrivee) = ?
        GROUP BY tt.id, tb.immatriculation, tv.libelle, tv.nbr_places,
                 t.depart, t.arrivee, t.distance_km, t.prix_base, p.nom, c.nom
        ORDER BY tt.date_heure_depart
    """;

    try (Connection c = DatabaseConfig.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, trajet);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            TaxiTrajet t = new TaxiTrajet();

            t.setId(rs.getInt("id"));
            t.setTaxiId(rs.getInt("taxi_id"));
            t.setTrajetId(rs.getInt("trajet_id"));
            t.setChauffeurId(rs.getInt("chauffeur_id"));
            t.setDateHeureDepart(rs.getObject("date_heure_depart", LocalDateTime.class));

            t.setImmatriculation(rs.getString("immatriculation"));
            t.setTypeVoiture(rs.getString("type_voiture"));
            t.setNbrPlaces(rs.getInt("nbr_places"));
            t.setDepart(rs.getString("depart"));
            t.setArrivee(rs.getString("arrivee"));
            t.setDistanceKm(rs.getDouble("distance_km"));
            t.setPrixBase(rs.getDouble("prix_base"));
            t.setNomChauffeur(rs.getString("nom_chauffeur"));
            t.setCooperative(rs.getString("cooperative"));
            t.setPlacesReservees(rs.getInt("places_reservees"));
            double valMax = TaxiTrajetDAO.getValeurMax(t);
            t.setValMax(valMax);
            trajets.add(t);
        }
    }
    return trajets;
}

public static double getValeurMax(TaxiTrajet t) throws Exception{
    double[] prix = getPrixTrajet(t.getTrajetId());
    //0 standart 
  
    int[] nbplace= getPlacesTaxi(t.getTaxiId());
  
    double valeurMax = 0 ;
    valeurMax = (nbplace[0] * prix[0]) +(nbplace[1] * prix[1]) +(nbplace[2] * prix[2]);
 return valeurMax;

}
public static int[] getPlacesTaxi(int taxiId) throws Exception {

    String sql = """
        SELECT 
            tv.nbr_places AS places_standard,
            COALESCE(tv.nb_places_premium, 0) AS places_premium,
            COALESCE(tv.nb_places_vip, 0) AS places_vip
        FROM taxi_brousse tb
        JOIN type_voiture tv ON tb.type_voiture_id = tv.id
        WHERE tb.id = ?
    """;

    try (Connection c = DatabaseConfig.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, taxiId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new int[] {
                rs.getInt("places_standard"),
                rs.getInt("places_premium"),
                rs.getInt("places_vip")
            };
        }
    }
    return new int[] {0, 0, 0};
}
public static double[] getPrixTrajet(int trajetId) throws Exception {

    String sql = """
        SELECT 
            prix_base,
            prix_premium,prix_vip
        FROM trajet
        WHERE id = ?
    """;

    try (Connection c = DatabaseConfig.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, trajetId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new double[] {
                rs.getDouble("prix_base"),
                rs.getDouble("prix_premium"),
                rs.getDouble("prix_vip")
            };
        }
    }
    return new double[] {0.0, 0.0 ,0.0};
}



}