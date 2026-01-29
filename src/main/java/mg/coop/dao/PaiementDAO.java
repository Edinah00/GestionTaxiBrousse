package mg.coop.dao;

import mg.coop.model.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class PaiementDAO {

    private Connection conn;

    public PaiementDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Paiement p) throws SQLException {

        String sql = """
            INSERT INTO paiement
            (reservation_id, type_paiement, mode_paiement, montant, date_paiement)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getReservation().getId());
            ps.setString(2, p.getTypePaiement());
            ps.setString(3, p.getModePaiement());
            ps.setDouble(4, p.getMontant());
            ps.setTimestamp(5, Timestamp.valueOf(p.getDatePaiement()));

            ps.executeUpdate();
        }

        // ✅ Mettre la réservation comme PAYÉE
        String update = "UPDATE reservation SET etat_paye = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setInt(1, p.getReservation().getId());
            ps.executeUpdate();
        }
    }

    public List<Paiement> findAll(
            LocalDate datePaiement,
            String modePaiement,
            String typePaiement
    ) throws SQLException {

        List<Paiement> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "p.id AS paiement_id, p.type_paiement, p.mode_paiement, p.montant, p.date_paiement, " +
            "r.id AS reservation_id, r.nom_client, " +
            "tt.id AS taxi_trajet_id, tt.date_heure_depart, " +
            "tr.depart, tr.arrivee " +
            "FROM paiement p " +
            "JOIN reservation r ON p.reservation_id = r.id " +
            "JOIN taxi_trajet tt ON r.taxi_trajet_id = tt.id " +
            "JOIN trajet tr ON tt.trajet_id = tr.id " +
            "WHERE 1=1"
        );

        if (datePaiement != null) {
            sql.append(" AND DATE(p.date_paiement) = ?");
        }
        if (modePaiement != null && !modePaiement.isEmpty()) {
            sql.append(" AND p.mode_paiement = ?");
        }
        if (typePaiement != null && !typePaiement.isEmpty()) {
            sql.append(" AND p.type_paiement = ?");
        }

        sql.append(" ORDER BY p.date_paiement DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (datePaiement != null) {
                ps.setDate(idx++, java.sql.Date.valueOf(datePaiement));
            }
            if (modePaiement != null && !modePaiement.isEmpty()) {
                ps.setString(idx++, modePaiement);
            }
            if (typePaiement != null && !typePaiement.isEmpty()) {
                ps.setString(idx++, typePaiement);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // Trajet
                    Trajet trajet = new Trajet();
                    trajet.setDepart(rs.getString("depart"));
                    trajet.setArrivee(rs.getString("arrivee"));

                    // TaxiTrajet
                    TaxiTrajet tt = new TaxiTrajet();
                    tt.setId(rs.getInt("taxi_trajet_id"));
                    tt.setDateHeureDepart(
                        rs.getTimestamp("date_heure_depart").toLocalDateTime()
                    );
                    tt.setTrajet(trajet);

                    // Reservation
                    Reservation r = new Reservation();
                    r.setId(rs.getInt("reservation_id"));
                    r.setNomClient(rs.getString("nom_client"));
                    r.setTaxiTrajet(tt);

                    // Paiement
                    Paiement p = new Paiement();
                    p.setId(rs.getInt("paiement_id"));
                    p.setTypePaiement(rs.getString("type_paiement"));
                    p.setModePaiement(rs.getString("mode_paiement"));
                    p.setMontant(rs.getDouble("montant"));
                    p.setDatePaiement(
                        rs.getTimestamp("date_paiement").toLocalDateTime()
                    );
                    p.setReservation(r);

                    list.add(p);
                }
            }
        }

        return list;
    }

    public double montantApayer(int idReservation) throws Exception {
        double montant = 0;
        String sql = "SELECT SUM(prix) AS montant FROM reservation_place AS rp JOIN place_passager AS pp ON pp.id = rp.id_place_passager WHERE rp.reservation_id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                montant = rs.getDouble("montant");
            }
        }
        return montant;
    }
}
