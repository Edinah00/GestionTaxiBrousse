package mg.coop.dao;

import java.sql.Connection;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.Diffusion;
import mg.coop.model.TaxiTrajet;

public class CADAO {

    private Connection connection;
    private DiffusionDAO diffusionDAO;
    private TaxiTrajetDAO taxiTrajetDAO;
 
    public CADAO(Connection connection) {
        this.connection = connection;
        this.diffusionDAO = new DiffusionDAO(connection);
        this.taxiTrajetDAO = new TaxiTrajetDAO(connection);
    }

    /**
     * Calcule et remplit les CA pour un TaxiTrajet
     */
    public void calculerEtRemplirCA(TaxiTrajet taxiTrajet, int mois, int annee) throws Exception {
        double caBillet = TaxiTrajetDAO.calculCA(taxiTrajet.getId());
        Diffusion diffusion = diffusionDAO.getCADiffusionParLigne(taxiTrajet.getId(), mois, annee);
        double caPub = diffusion.getCA();
        System.out.println(taxiTrajet.getId());
        taxiTrajet.setCaBillet(caBillet);
        taxiTrajet.setCaPub(caPub);
        taxiTrajet.setCaTotal(caBillet + caPub);
    }
  
}