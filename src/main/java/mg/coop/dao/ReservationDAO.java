package mg.coop.dao;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    /**
     * Récupère les places occupées pour un taxi_trajet donné
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
     * Récupère les places réservées pour une réservation donnée
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

    public int insertReservation(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservation (taxi_trajet_id, nom_client, telephone, nb_places, statut) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, reservation.getTaxiTrajetId());
            ps.setString(2, reservation.getNomClient());
            ps.setString(3, reservation.getTelephone());
            ps.setInt(4, reservation.getNbPlaces());
            ps.setString(5, reservation.getStatut());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    public void insertReservationPlace(int taxiTrajetId, int reservationId, int numeroPlace) throws Exception {
        String sql = "INSERT INTO reservation_place (taxi_trajet_id, reservation_id, numero_place) " +
                     "VALUES (?, ?, ?)";
        
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, taxiTrajetId);
            ps.setInt(2, reservationId);
            ps.setInt(3, numeroPlace);
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
                return rs.getInt(1) == 0; // Place disponible si count = 0
            }
        }
        return false;
    }

    /**
     * CORRECTION: Ajout de GROUP BY pour éviter les doublons et agrégation des paiements
     */
    public List<Reservation> getAllReservations() throws Exception {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, " +
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
                     "GROUP BY r.id, r.taxi_trajet_id, r.nom_client, r.telephone, r.nb_places, " +
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
                reservation.setStatut(rs.getString("statut"));
                
                // Conversion Timestamp vers LocalDateTime
                Timestamp dateResTimestamp = rs.getTimestamp("date_reservation");
                if (dateResTimestamp != null) {
                    reservation.setDateReservation(dateResTimestamp.toLocalDateTime());
                }
                
                // Informations supplémentaires
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


}