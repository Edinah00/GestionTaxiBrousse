package mg.coop.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import mg.coop.dao.FactureDiffusionDAO;

public class FactureDiffusionMetier {
    private FactureDiffusionDAO factureDAO;

    public FactureDiffusionMetier(Connection connection) {
        this.factureDAO = new FactureDiffusionDAO(connection);
    }

    /**
     * Calcule le total payé par diffusion pour un voyage
     */
    public double calculTotalPayeDiffusionParVoyage(int taxiTrajetId) throws SQLException {
        return factureDAO.calculTotalPayeDiffusionParVoyage(taxiTrajetId);
    }

    /**
     * Calcule le montant total de diffusion pour un voyage
     */
    public double calculMontantDiffusionParVoyage(int taxiTrajetId) throws SQLException {
        return factureDAO.calculMontantDiffusionParVoyage(taxiTrajetId);
    }

    /**
     * Calcule le reste à payer pour un voyage
     */
    public double calculResteAPayerParVoyage(int taxiTrajetId) throws SQLException {
        return factureDAO.calculResteAPayerParVoyage(taxiTrajetId);
    }

    /**
     * Calcule le reste à payer pour une société
     */
    public Map<String, Object> calculResteAPayerParSociete(int societeId) throws SQLException {
        return factureDAO.calculResteAPayerParSociete(societeId);
    }

    /**
     * Calcule le reste à payer pour toutes les sociétés
     */
    public List<Map<String, Object>> calculResteAPayerToutesSocietes() throws SQLException {
        return factureDAO.calculResteAPayerToutesSocietes();
    }

    /**
     * Récupère les détails par voyage pour une société
     */
    public List<Map<String, Object>> getDetailsVoyageParSociete(int societeId) throws SQLException {
        return factureDAO.getDetailsVoyageParSociete(societeId);
    }
}