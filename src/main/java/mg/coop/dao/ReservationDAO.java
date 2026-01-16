package mg.coop.dao;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ReservationDAO {

    /**
     * Récupère les places occupées pour un taxi_trajet donné avec leur type
     */
    public Map<Integer, String> getPlacesOccupeesAvecType(int taxiTrajetId) throws Exception {
        Map<Integer, String> placesOccupees = new HashMap<>();
        String sql = "SELECT numero_place, type_place FROM reservation_place WHERE taxi_trajet_id = ? ORDER BY numero_place";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                placesOccupees.put(rs.getInt("numero_place"), rs.getString("type_place"));
            }
        }
        return placesOccupees;
    }

    /**
     * Récupère les places occupées (anciennes méthode pour compatibilité)
     */
    public List<Integer> getPlacesOccupees(int taxiTrajetId) throws Exception {
        List<Integer> placesOccupees = new ArrayList<>();
        String sql = "SELECT numero_place FROM reservation_place WHERE taxi_trajet_id = ? ORDER BY numero_place";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                placesOccupees.add(rs.getInt("numero_place"));
            }
        }
        return placesOccupees;
    }

    /**
     * Récupère les places réservées pour une réservation donnée avec leur type
     */
    public Map<Integer, String> getPlacesReserveesAvecType(int reservationId) throws Exception {
        Map<Integer, String> places = new HashMap<>();
        String sql = "SELECT numero_place, type_place FROM reservation_place WHERE reservation_id = ? ORDER BY numero_place";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                places.put(rs.getInt("numero_place"), rs.getString("type_place"));
            }
        }
        return places;
    }

    /**
     * Récupère les places réservées (ancienne méthode pour compatibilité)
     */
    public List<Integer> getPlacesReservees(int reservationId) throws Exception {
        List<Integer> places = new ArrayList<>();
        String sql = "SELECT numero_place FROM reservation_place WHERE reservation_id = ? ORDER BY numero_place";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                places.add(rs.getInt("numero_place"));
            }
        }
        return places;
    }

    /**
     * Insère une réservation avec nombre d'enfants
     */
    public int insertReservation(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservation (taxi_trajet_id, nom_client, telephone, nb_places, nb_enfants, statut) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservation.getTaxiTrajetId());
            ps.setString(2, reservation.getNomClient());
            ps.setString(3, reservation.getTelephone());
            ps.setInt(4, reservation.getNbPlaces());
            ps.setInt(5, reservation.getNbEnfants());
            ps.setString(6, reservation.getStatut());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    /**
     * Insère une place réservée avec son type
     */
    public void insertReservationPlace(int taxiTrajetId, int reservationId, int numeroPlace, String typePlace) throws Exception {
        String sql = "INSERT INTO reservation_place (taxi_trajet_id, reservation_id, numero_place, type_place) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, taxiTrajetId);
            ps.setInt(2, reservationId);
            ps.setInt(3, numeroPlace);
            ps.setString(4, typePlace != null ? typePlace : "STANDARD");
            ps.executeUpdate();
        }
    }

    public void insertPaiement(int reservationId, String typePaiement, String modePaiement, double montant) throws Exception {
        String sql = "INSERT INTO paiement (reservation_id, type_paiement, mode_paiement, montant) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            ps.setString(2, typePaiement);
            ps.setString(3, modePaiement);
            ps.setDouble(4, montant);
            ps.executeUpdate();
        }
    }

    public boolean verifierDisponibilitePlace(int taxiTrajetId, int numeroPlace) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservation_place WHERE taxi_trajet_id = ? AND numero_place = ?";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, taxiTrajetId);
            ps.setInt(2, numeroPlace);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    /**
     * Récupère toutes les réservations
     */
    public List<Reservation> getAllReservations() throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, r.nb_enfants, " +
                     "r.statut, r.date_reservation, " +
                     "tr.depart, tr.arrivee, tt.date_heure_depart, " +
                     "tb.immatriculation, tv.libelle as type_voiture, " +
                     "p_chauffeur.nom as nom_chauffeur, " +
                     "tr.prix_base, " +
                     "COALESCE(SUM(p.montant), 0) as montant_paye, " +
                     "MAX(p.mode_paiement) as mode_paiement, " +
                     "MAX(p.type_paiement) as type_paiement " +
                     "FROM reservation r " +
                     "JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id " +
                     "JOIN trajet tr ON tt.trajet_id = tr.id " +
                     "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
                     "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
                     "JOIN personne p_chauffeur ON tt.chauffeur_id = p_chauffeur.id " +
                     "LEFT JOIN paiement p ON r.id = p.reservation_id " +
                     "GROUP BY r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, r.nb_enfants, " +
                     "r.statut, r.date_reservation, tr.depart, tr.arrivee, tt.date_heure_depart, " +
                     "tb.immatriculation, tv.libelle, p_chauffeur.nom, tr.prix_base " +
                     "ORDER BY r.date_reservation DESC";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setTaxiTrajetId(rs.getInt("taxi_trajet_id"));
                reservation.setNomClient(rs.getString("nom_client"));
                reservation.setTelephone(rs.getString("telephone"));
                reservation.setNbPlaces(rs.getInt("nb_places"));
                reservation.setNbEnfants(rs.getInt("nb_enfants"));
                reservation.setStatut(rs.getString("statut"));
                
                Timestamp dateResTimestamp = rs.getTimestamp("date_reservation");
                if (dateResTimestamp != null) {
                    reservation.setDateReservation(dateResTimestamp.toLocalDateTime());
                }
                
                reservation.setDepart(rs.getString("depart"));
                reservation.setArrivee(rs.getString("arrivee"));
                reservation.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                reservation.setImmatriculation(rs.getString("immatriculation"));
                reservation.setTypeVoiture(rs.getString("type_voiture"));
                reservation.setNomChauffeur(rs.getString("nom_chauffeur"));
                reservation.setPrixBase(rs.getDouble("prix_base"));
                reservation.setMontantPaye(rs.getDouble("montant_paye"));
                reservation.setModePaiement(rs.getString("mode_paiement"));
                reservation.setTypePaiement(rs.getString("type_paiement"));
                
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    /**
     * Recherche des réservations avec filtres
     */
    public List<Reservation> searchReservations(String nomClient, String dateDebut, String dateFin, Integer taxiTrajetId) throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, r.nb_enfants, ")
           .append("r.statut, r.date_reservation, ")
           .append("tr.depart, tr.arrivee, tt.date_heure_depart, ")
           .append("tb.immatriculation, tv.libelle as type_voiture, ")
           .append("p_chauffeur.nom as nom_chauffeur, ")
           .append("tr.prix_base, ")
           .append("COALESCE(SUM(p.montant), 0) as montant_paye, ")
           .append("MAX(p.mode_paiement) as mode_paiement, ")
           .append("MAX(p.type_paiement) as type_paiement ")
           .append("FROM reservation r ")
           .append("JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id ")
           .append("JOIN trajet tr ON tt.trajet_id = tr.id ")
           .append("JOIN taxi_brousse tb ON tt.taxi_id = tb.id ")
           .append("JOIN type_voiture tv ON tb.type_voiture_id = tv.id ")
           .append("JOIN personne p_chauffeur ON tt.chauffeur_id = p_chauffeur.id ")
           .append("LEFT JOIN paiement p ON r.id = p.reservation_id ")
           .append("WHERE 1=1 ");
        
        if (nomClient != null && !nomClient.trim().isEmpty()) {
            sql.append("AND LOWER(r.nom_client) LIKE LOWER(?) ");
        }
        if (dateDebut != null && !dateDebut.trim().isEmpty()) {
            sql.append("AND DATE(r.date_reservation) >= ? ");
        }
        if (dateFin != null && !dateFin.trim().isEmpty()) {
            sql.append("AND DATE(r.date_reservation) <= ? ");
        }
        if (taxiTrajetId != null && taxiTrajetId > 0) {
            sql.append("AND r.taxi_trajet_id = ? ");
        }
        
        sql.append("GROUP BY r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, r.nb_enfants, ")
           .append("r.statut, r.date_reservation, tr.depart, tr.arrivee, tt.date_heure_depart, ")
           .append("tb.immatriculation, tv.libelle, p_chauffeur.nom, tr.prix_base ")
           .append("ORDER BY r.date_reservation DESC");
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            if (nomClient != null && !nomClient.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + nomClient.trim() + "%");
            }
            if (dateDebut != null && !dateDebut.trim().isEmpty()) {
                ps.setDate(paramIndex++, Date.valueOf(dateDebut));
            }
            if (dateFin != null && !dateFin.trim().isEmpty()) {
                ps.setDate(paramIndex++, Date.valueOf(dateFin));
            }
            if (taxiTrajetId != null && taxiTrajetId > 0) {
                ps.setInt(paramIndex++, taxiTrajetId);
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setTaxiTrajetId(rs.getInt("taxi_trajet_id"));
                reservation.setNomClient(rs.getString("nom_client"));
                reservation.setTelephone(rs.getString("telephone"));
                reservation.setNbPlaces(rs.getInt("nb_places"));
                reservation.setNbEnfants(rs.getInt("nb_enfants"));
                reservation.setStatut(rs.getString("statut"));
                
                Timestamp dateResTimestamp = rs.getTimestamp("date_reservation");
                if (dateResTimestamp != null) {
                    reservation.setDateReservation(dateResTimestamp.toLocalDateTime());
                }
                
                reservation.setDepart(rs.getString("depart"));
                reservation.setArrivee(rs.getString("arrivee"));
                reservation.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                reservation.setImmatriculation(rs.getString("immatriculation"));
                reservation.setTypeVoiture(rs.getString("type_voiture"));
                reservation.setNomChauffeur(rs.getString("nom_chauffeur"));
                reservation.setPrixBase(rs.getDouble("prix_base"));
                reservation.setMontantPaye(rs.getDouble("montant_paye"));
                reservation.setModePaiement(rs.getString("mode_paiement"));
                reservation.setTypePaiement(rs.getString("type_paiement"));
                
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    /**
     * Récupère tous les taxi-trajets distincts ayant des réservations
     */
    public List<TaxiTrajetInfo> getAllTaxiTrajetsWithReservations() throws Exception {
        List<TaxiTrajetInfo> trajets = new ArrayList<>();
        String sql = "SELECT DISTINCT tt.id, tr.depart, tr.arrivee, tt.date_heure_depart, tb.immatriculation " +
                     "FROM taxi_trajet tt " +
                     "JOIN trajet tr ON tt.trajet_id = tr.id " +
                     "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
                     "WHERE EXISTS (SELECT 1 FROM reservation r WHERE r.taxi_trajet_id = tt.id) " +
                     "ORDER BY tt.date_heure_depart DESC";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                TaxiTrajetInfo info = new TaxiTrajetInfo();
                info.setId(rs.getInt("id"));
                info.setDepart(rs.getString("depart"));
                info.setArrivee(rs.getString("arrivee"));
                info.setDateHeureDepart(rs.getTimestamp("date_heure_depart"));
                info.setImmatriculation(rs.getString("immatriculation"));
                trajets.add(info);
            }
        }
        return trajets;
    }

    // Classe interne pour stocker les infos de taxi-trajet
    public static class TaxiTrajetInfo {
        private int id;
        private String depart;
        private String arrivee;
        private Timestamp dateHeureDepart;
        private String immatriculation;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getDepart() { return depart; }
        public void setDepart(String depart) { this.depart = depart; }
        public String getArrivee() { return arrivee; }
        public void setArrivee(String arrivee) { this.arrivee = arrivee; }
        public Timestamp getDateHeureDepart() { return dateHeureDepart; }
        public void setDateHeureDepart(Timestamp dateHeureDepart) { this.dateHeureDepart = dateHeureDepart; }
        public String getImmatriculation() { return immatriculation; }
        public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    }
}