package mg.coop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mg.coop.model.ReservationPlace;

public class ReservationPlaceDAO {

    private Connection conn;

    public ReservationPlaceDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(ReservationPlace rp) throws SQLException {
        String sql = """
            INSERT INTO reservation_place
            (reservation_id, numero_place, id_place_passager)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rp.getReservation().getId());
            ps.setInt(2, rp.getNumeroPlace());
            ps.setInt(3, rp.getPlacePassager().getId());
            ps.executeUpdate();
        }
    }

    public boolean isPlaceTaken(int taxiTrajetId, int numeroPlace) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS cnt
            FROM reservation_place rp
            JOIN reservation r ON rp.reservation_id = r.id
            WHERE r.taxi_trajet_id = ? AND rp.numero_place = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            ps.setInt(2, numeroPlace);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        }
        return false;
    }


}
