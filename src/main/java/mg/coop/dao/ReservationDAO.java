package mg.coop.dao;

import mg.coop.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    // Créer une réservation et les places associées
    public int insertReservation(Reservation r, List<Integer> places)
        throws SQLException {

        String sql = """
            INSERT INTO reservation
            (taxi_trajet_id, nom_client, telephone, nb_places,
            date_reservation, statut)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        int reservationId;

        try (PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getTaxiTrajet().getId());
            ps.setString(2, r.getNomClient());
            ps.setString(3, r.getTelephone());
            ps.setInt(4, r.getNbPlaces());
            ps.setTimestamp(5, Timestamp.valueOf(r.getDateReservation()));
            ps.setString(6, r.getStatut());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            reservationId = rs.getInt(1);
        }

        String sqlPlace = "INSERT INTO reservation_place (reservation_id, numero_place, id_place_passager) " +
                            "VALUES (?, ?, ?)";
        try (PreparedStatement psPlace = conn.prepareStatement(sqlPlace)) {
            for (Integer num : places) {
                psPlace.setInt(1, reservationId);
                psPlace.setInt(2, num);
                psPlace.setInt(3, 2);
                psPlace.addBatch();
            }
            psPlace.executeBatch();
        }

        return reservationId;
    }

    public List<Reservation> findReservations(
        Integer taxiTrajetId) throws SQLException {

        List<Reservation> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "r.id AS reservation_id, r.nom_client, r.telephone, r.nb_places, r.date_reservation, r.statut, r.etat_paye, " +
            "tt.id AS taxi_trajet_id, tt.date_heure_depart, " +
            "tb.id AS taxi_id, tb.immatriculation, " +
            "tv.id AS type_voiture_id, tv.libelle AS type_voiture_libelle, tv.nbr_places AS type_voiture_nbr_places, " +
            "tr.id AS trajet_id, tr.depart, tr.arrivee, tr.prix_base " +
            "FROM reservation r " +
            "JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id " +
            "JOIN trajet tr ON tt.trajet_id = tr.id " +
            "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
            "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
            "WHERE 1=1 AND r.etat_paye = TRUE"
        );

        if (taxiTrajetId != null) {
            sql.append(" AND r.taxi_trajet_id = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;

            if (taxiTrajetId != null) {
                ps.setInt(idx++, taxiTrajetId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReservation(rs));
                }
            }
        }

        return list;
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {

        Reservation r = new Reservation();
        r.setId(rs.getInt("reservation_id"));
        r.setNomClient(rs.getString("nom_client"));
        r.setTelephone(rs.getString("telephone"));
        r.setNbPlaces(rs.getInt("nb_places"));
        r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
        r.setStatut(rs.getString("statut"));
        r.setEtat(rs.getBoolean("etat_paye"));

        // ===== Trajet =====
        Trajet trajet = new Trajet();
        trajet.setId(rs.getInt("trajet_id"));
        trajet.setDepart(rs.getString("depart"));
        trajet.setArrivee(rs.getString("arrivee"));
        trajet.setPrixBase(rs.getDouble("prix_base")); 

        // ===== TaxiTrajet =====
        TaxiTrajet tt = new TaxiTrajet();
        tt.setId(rs.getInt("taxi_trajet_id"));
        tt.setDateHeureDepart(
            rs.getTimestamp("date_heure_depart").toLocalDateTime()
        );
        tt.setTrajet(trajet); 

        // ===== Taxi =====
        TaxiBrousse taxi = new TaxiBrousse();
        taxi.setId(rs.getInt("taxi_id"));
        taxi.setImmatriculation(rs.getString("immatriculation"));

        // ===== Type voiture =====
        TypeVoiture tv = new TypeVoiture();
        tv.setId(rs.getInt("type_voiture_id"));
        tv.setLibelle(rs.getString("type_voiture_libelle"));
        tv.setNbrPlaces(rs.getInt("type_voiture_nbr_places"));

        taxi.setTypeVoiture(tv);
        tt.setTaxi(taxi);
        r.setTaxiTrajet(tt);

        return r;
    }

    public Reservation findById(int id) throws SQLException {
        Reservation r = null;

        String sql =
            "SELECT " +
            "r.id AS r_id, r.nom_client, r.telephone, r.nb_places, r.date_reservation, r.statut, r.etat_paye, " +
            "tt.id AS tt_id, tt.date_heure_depart, " +
            "tr.id AS tr_id, tr.depart, tr.arrivee, tr.prix_base, " +
            "t.id AS t_id, t.immatriculation, " +
            "tv.id AS tv_id, tv.libelle, tv.nbr_places " +
            "FROM reservation r " +
            "JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id " +
            "JOIN trajet tr ON tt.trajet_id = tr.id " +
            "JOIN taxi_brousse t ON tt.taxi_id = t.id " +
            "JOIN type_voiture tv ON t.type_voiture_id = tv.id " +
            "WHERE r.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    // ===== Reservation =====
                    r = new Reservation();
                    r.setId(rs.getInt("r_id"));
                    r.setNomClient(rs.getString("nom_client"));
                    r.setTelephone(rs.getString("telephone"));
                    r.setNbPlaces(rs.getInt("nb_places"));
                    r.setDateReservation(
                        rs.getTimestamp("date_reservation").toLocalDateTime()
                    );
                    r.setStatut(rs.getString("statut"));
                    r.setEtat(rs.getBoolean("etat_paye"));

                    // ===== Trajet =====
                    Trajet trajet = new Trajet();
                    trajet.setId(rs.getInt("tr_id"));
                    trajet.setDepart(rs.getString("depart"));
                    trajet.setArrivee(rs.getString("arrivee"));
                    trajet.setPrixBase(rs.getDouble("prix_base"));

                    // ===== Taxi =====
                    TaxiBrousse taxi = new TaxiBrousse();
                    taxi.setId(rs.getInt("t_id"));
                    taxi.setImmatriculation(rs.getString("immatriculation"));

                    TypeVoiture tv = new TypeVoiture();
                    tv.setId(rs.getInt("tv_id"));
                    tv.setLibelle(rs.getString("libelle"));
                    tv.setNbrPlaces(rs.getInt("nbr_places"));
                    taxi.setTypeVoiture(tv);

                    // ===== TaxiTrajet =====
                    TaxiTrajet tt = new TaxiTrajet();
                    tt.setId(rs.getInt("tt_id"));
                    tt.setDateHeureDepart(
                        rs.getTimestamp("date_heure_depart").toLocalDateTime()
                    );
                    tt.setTaxi(taxi);
                    tt.setTrajet(trajet);

                    // ===== Liaison finale =====
                    r.setTaxiTrajet(tt);
                }
            }
        }

        return r;
    }

    public double calculerChiffreAffaires(
        LocalDate dateDepart,
        Integer taxiTrajetId
    ) throws SQLException {

        double total = 0;

        StringBuilder sql = new StringBuilder(
            "SELECT COALESCE(SUM(p.montant), 0) AS total_ca " +
            "FROM paiement p " +
            "JOIN reservation r ON p.reservation_id = r.id " +
            "JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id " +
            "WHERE r.etat_paye = TRUE "
        );

        if (dateDepart != null) {
            sql.append("AND DATE(tt.date_heure_depart) = ? ");
        }

        if (taxiTrajetId != null) {
            sql.append("AND tt.id = ? ");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (dateDepart != null) {
                ps.setDate(idx++, java.sql.Date.valueOf(dateDepart));
            }
            if (taxiTrajetId != null) {
                ps.setInt(idx++, taxiTrajetId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_ca");
                }
            }
        }

        return total;
    }

    public int createAndReturnId(Reservation r) throws SQLException {
        String sql = """
            INSERT INTO reservation
            (taxi_trajet_id, nom_client, telephone, nb_places)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getTaxiTrajet().getId());
            ps.setString(2, r.getNomClient());
            ps.setString(3, r.getTelephone());
            ps.setInt(4, r.getNbPlaces());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Insertion réservation échouée");
    }


}

