package mg.coop.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactureDiffusionDAO {
    private Connection connection;

    public FactureDiffusionDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Calcule le total payé par diffusion pour UN voyage spécifique
     */
    public double calculTotalPayeDiffusionParVoyage(int taxiTrajetId) throws SQLException {
        double totalPaye = 0;
        
        String sql = """
            SELECT COALESCE(SUM(
                pd.montant * (fdl.montant / f.montant_total)
            ), 0) AS total_paye
            FROM facture_diffusion_ligne fdl
            JOIN facture_diffusion f ON fdl.id_facture = f.id
            LEFT JOIN paiement_diffusion pd ON pd.id_facture = f.id
            WHERE fdl.id_taxi_trajet = ?
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                totalPaye = rs.getDouble("total_paye");
            }
        }
        
        return totalPaye;
    }

    /**
     * Calcule le montant total de diffusion pour UN voyage (CA brut)
     */
    public double calculMontantDiffusionParVoyage(int taxiTrajetId) throws SQLException {
        double montant = 0;
        
        String sql = """
            SELECT COALESCE(SUM(fdl.montant), 0) AS montant_total
            FROM facture_diffusion_ligne fdl
            WHERE fdl.id_taxi_trajet = ?
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                montant = rs.getDouble("montant_total");
            }
        }
        
        return montant;
    }

    /**
     * Calcule le reste à payer pour UN voyage spécifique
     */
    public double calculResteAPayerParVoyage(int taxiTrajetId) throws SQLException {
        double resteAPayer = 0;
        
        String sql = """
            SELECT COALESCE(SUM(
                fdl.montant_restant * (fdl.montant / f.montant_total)
            ), 0) AS reste_a_payer
            FROM facture_diffusion_ligne fdl
            JOIN facture_diffusion f ON fdl.id_facture = f.id
            WHERE fdl.id_taxi_trajet = ?
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                resteAPayer = rs.getDouble("reste_a_payer");
            }
        }
        
        return resteAPayer;
    }

    /**
     * Calcule le reste à payer pour UNE société spécifique
     */
    public Map<String, Object> calculResteAPayerParSociete(int societeId) throws SQLException {
        Map<String, Object> resultat = new HashMap<>();
        
        String sql = """
            SELECT 
                s.id AS societe_id,
                s.nom AS societe_nom,
                COALESCE(SUM(f.montant_total), 0) AS montant_total,
                COALESCE(SUM(f.montant_total - f.montant_restant), 0) AS montant_paye,
                COALESCE(SUM(f.montant_restant), 0) AS montant_restant,
                COUNT(f.id) AS nb_factures
            FROM societe s
            LEFT JOIN facture_diffusion f ON f.id_societe = s.id
            WHERE s.id = ?
            GROUP BY s.id, s.nom
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, societeId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                resultat.put("societeId", rs.getInt("societe_id"));
                resultat.put("societeNom", rs.getString("societe_nom"));
                resultat.put("montantTotal", rs.getDouble("montant_total"));
                resultat.put("montantPaye", rs.getDouble("montant_paye"));
                resultat.put("montantRestant", rs.getDouble("montant_restant"));
                resultat.put("nbFactures", rs.getInt("nb_factures"));
                
                double reste = rs.getDouble("montant_restant");
                String statut = reste == 0 ? "PAYEE" : "EN_COURS";
                resultat.put("statut", statut);
            }
        }
        
        return resultat;
    }

    /**
     * Calcule le reste à payer pour TOUTES les sociétés
     */
    public List<Map<String, Object>> calculResteAPayerToutesSocietes() throws SQLException {
        List<Map<String, Object>> resultats = new ArrayList<>();
        
        String sql = """
            SELECT 
                s.id AS societe_id,
                s.nom AS societe_nom,
                COALESCE(SUM(f.montant_total), 0) AS montant_total,
                COALESCE(SUM(f.montant_total - f.montant_restant), 0) AS montant_paye,
                COALESCE(SUM(f.montant_restant), 0) AS montant_restant,
                COUNT(f.id) AS nb_factures
            FROM societe s
            LEFT JOIN facture_diffusion f ON f.id_societe = s.id
            GROUP BY s.id, s.nom
            ORDER BY s.nom
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> societe = new HashMap<>();
                societe.put("societeId", rs.getInt("societe_id"));
                societe.put("societeNom", rs.getString("societe_nom"));
                societe.put("montantTotal", rs.getDouble("montant_total"));
                societe.put("montantPaye", rs.getDouble("montant_paye"));
                societe.put("montantRestant", rs.getDouble("montant_restant"));
                societe.put("nbFactures", rs.getInt("nb_factures"));
                
                double reste = rs.getDouble("montant_restant");
                double paye = rs.getDouble("montant_paye");
                
                String statut;
                if (reste == 0 && paye > 0) {
                    statut = "PAYEE";
                } else if (paye > 0) {
                    statut = "EN_COURS";
                } else {
                    statut = "IMPAYE";
                }
                societe.put("statut", statut);
                
                resultats.add(societe);
            }
        }
        
        return resultats;
    }

    /**
     * Récupère les détails de diffusion par voyage pour une société
     */
    public List<Map<String, Object>> getDetailsVoyageParSociete(int societeId) throws SQLException {
        List<Map<String, Object>> details = new ArrayList<>();
        
        String sql = """
            SELECT 
                tt.id AS taxi_trajet_id,
                tt.date_heure_depart,
                t.depart,
                t.arrivee,
                fdl.nb_diffusions,
                fdl.montant,
                fdl.montant_restant,
                f.id AS facture_id,
                f.date_facture
            FROM facture_diffusion f
            JOIN facture_diffusion_ligne fdl ON fdl.id_facture = f.id
            JOIN taxi_trajet tt ON tt.id = fdl.id_taxi_trajet
            JOIN trajet t ON t.id = tt.trajet_id
            WHERE f.id_societe = ?
            ORDER BY tt.date_heure_depart DESC
        """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, societeId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("taxiTrajetId", rs.getInt("taxi_trajet_id"));
                detail.put("dateVoyage", rs.getTimestamp("date_heure_depart"));
                detail.put("depart", rs.getString("depart"));
                detail.put("arrivee", rs.getString("arrivee"));
                detail.put("nbDiffusions", rs.getInt("nb_diffusions"));
                detail.put("montant", rs.getDouble("montant"));
                detail.put("montantRestant", rs.getDouble("montant_restant"));
                detail.put("factureId", rs.getInt("facture_id"));
                detail.put("dateFacture", rs.getDate("date_facture"));
                
                details.add(detail);
            }
        }
        
        return details;
    }
}