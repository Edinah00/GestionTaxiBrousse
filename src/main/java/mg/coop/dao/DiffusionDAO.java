package mg.coop.dao;


import mg.coop.model.Diffusion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiffusionDAO {

    private Connection connection;

    public DiffusionDAO(Connection connection) {
        this.connection = connection;
    }

    // INSERT
    public void insert(Diffusion diffusion) throws SQLException {
        String sql = "INSERT INTO diffusion (idPub, idTaxiTrajet) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, diffusion.getIdPub());
            ps.setInt(2, diffusion.getIdTaxiTrajet());
            ps.executeUpdate();
        }
    }

    // SELECT BY ID
    public Diffusion findById(int id) throws SQLException {
        String sql = "SELECT * FROM diffusion WHERE id = ?";
        Diffusion diffusion = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                diffusion = new Diffusion(
                        rs.getInt("id"),
                        rs.getInt("idPub"),
                        rs.getInt("idTaxiTrajet")
                );
            }
        }
        return diffusion;
    }

    // SELECT ALL
    public List<Diffusion> findAll() throws SQLException {
        List<Diffusion> list = new ArrayList<>();
        String sql = "SELECT * FROM diffusion";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Diffusion(
                        rs.getInt("id"),
                        rs.getInt("idPub"),
                        rs.getInt("idTaxiTrajet")
                ));
            }
        }
        return list;
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM diffusion WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Diffusion getCADiffusionParMois(int mois, int annee) {

        String sql = """
            SELECT
                COALESCE(SUM(p.cout * d.nb_diffusions), 0) AS ca,
                COALESCE(SUM(d.nb_diffusions), 0) AS total_diffusions
            FROM diffusion d
            JOIN pub p ON d.idPub = p.id
            JOIN taxi_trajet tt ON d.idTaxiTrajet = tt.id
            WHERE EXTRACT(MONTH FROM tt.date_heure_depart) = ?
            AND EXTRACT(YEAR FROM tt.date_heure_depart) = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, mois);
            ps.setInt(2, annee);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Diffusion diffusion = new Diffusion();
                diffusion.setCA(rs.getDouble("ca"));
                diffusion.setTotalDiffusions(rs.getInt("total_diffusions"));
                return diffusion;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }                                                                      

        return null;
    }

    double tokonyHaloa(int idSociete , int mois ,int annee) {
        double du = 0;
        String sql = """
            SELECT
                COALESCE(SUM(p.cout * d.nb_diffusions), 0) AS ca
            FROM diffusion d
            JOIN pub p ON d.idPub = p.id
            JOIN taxi_trajet tt ON d.idTaxiTrajet = tt.id
            WHERE EXTRACT(MONTH FROM tt.date_heure_depart) = ?
            AND EXTRACT(YEAR FROM tt.date_heure_depart) = ?
            AND p.idSociete = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, mois);
            ps.setInt(2, annee);
            ps.setInt(3, idSociete);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                du = rs.getDouble("ca");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return du;

    }

    public double resteAPayer(int idSociete , int mois ,int annee) {
        double totalPayee = 0;
        double totalCA = 0;
        double reste = 0;

        String sqlPayment = """
            SELECT
                COALESCE(SUM(montant), 0) AS total_payee
            FROM payment_diffusion
            WHERE EXTRACT(MONTH FROM date_payment) = ?
            AND EXTRACT(YEAR FROM date_payment) = ?
            AND idSociete = ?
            """;

        try (PreparedStatement psPayment = connection.prepareStatement(sqlPayment)) {

            psPayment.setInt(1, mois);
            psPayment.setInt(2, annee);
            psPayment.setInt(3, idSociete);

            ResultSet rsPayment = psPayment.executeQuery();

            if (rsPayment.next()) {
                totalPayee = rsPayment.getDouble("total_payee");
            }

            totalCA = tokonyHaloa(idSociete, mois, annee);
            reste = totalCA - totalPayee;

            return reste;

        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return reste;
    }

    public double dejaPaye(int idSociete , int mois ,int annee) {
        double totalPayee = 0;

        String sqlPayment = """
            SELECT
                COALESCE(SUM(montant), 0) AS total_payee
            FROM payment_diffusion
            WHERE EXTRACT(MONTH FROM date_payment) = ?
            AND EXTRACT(YEAR FROM date_payment) = ?
            AND idSociete = ?
            """;

        try (PreparedStatement psPayment = connection.prepareStatement(sqlPayment)) {

            psPayment.setInt(1, mois);
            psPayment.setInt(2, annee);
            psPayment.setInt(3, idSociete);

            ResultSet rsPayment = psPayment.executeQuery();

            if (rsPayment.next()) {
                totalPayee = rsPayment.getDouble("total_payee");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return totalPayee;
    }
    
   public Diffusion getCADiffusionParLigne(int idTaxiTrajet, int mois, int annee) {
    Diffusion resultat = new Diffusion();

    String sql = """
        SELECT
            tt.id AS taxi_trajet_id,
            COALESCE(SUM(p.cout * d.nb_diffusions), 0) AS ca_diffusion,
            COALESCE(SUM(d.nb_diffusions), 0) AS total_diffusions
        FROM taxi_trajet tt
        LEFT JOIN diffusion d ON d.idTaxiTrajet = tt.id
        LEFT JOIN pub p ON d.idPub = p.id
        WHERE tt.id = ?
          AND EXTRACT(MONTH FROM tt.date_heure_depart) = ?
          AND EXTRACT(YEAR FROM tt.date_heure_depart) = ?
        GROUP BY tt.id
        """;

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idTaxiTrajet);
        ps.setInt(2, mois);
        ps.setInt(3, annee);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            resultat.setIdTaxiTrajet(rs.getInt("taxi_trajet_id"));
            resultat.setCA(rs.getDouble("ca_diffusion"));
            resultat.setTotalDiffusions(rs.getInt("total_diffusions"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return resultat;
}

}