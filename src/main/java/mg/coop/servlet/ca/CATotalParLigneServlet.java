package mg.coop.servlet.ca;

import mg.coop.config.DatabaseConfig;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.model.FactureDiffusionMetier;
import mg.coop.model.TaxiTrajet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/ca")
public class CATotalParLigneServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        
        try {
            conn = DatabaseConfig.getConnection();
            
            TaxiTrajetDAO taxiTrajetDAO = new TaxiTrajetDAO(conn);
            FactureDiffusionMetier factureMetier = new FactureDiffusionMetier(conn);

            // Récupérer tous les trajets
            List<TaxiTrajet> trajets = taxiTrajetDAO.findAll();

            // Calculer le CA pour chaque trajet
            for (TaxiTrajet tt : trajets) {
                // Calcul CA billet
                double caBillet = taxiTrajetDAO.calculCA(tt.getId());
                tt.setCaBillet(caBillet);
                
                // Calcul CA pub (montant total de diffusion)
                double caPub = factureMetier.calculMontantDiffusionParVoyage(tt.getId());
                tt.setCaPub(caPub);
                
                // Calcul montant payé pour la pub
                double caPubPaye = factureMetier.calculTotalPayeDiffusionParVoyage(tt.getId());
                tt.setCaPubPaye(caPubPaye);
                
                // Calcul reste à payer pour la pub
                double caPubReste = factureMetier.calculResteAPayerParVoyage(tt.getId());
                tt.setCaPubReste(caPubReste);
                
                // CA Total
                tt.setCaTotal(caBillet + caPub);
            }

            // Récupérer aussi le résumé par société
            List<Map<String, Object>> societes = factureMetier.calculResteAPayerToutesSocietes();

            request.setAttribute("trajets", trajets);
            request.setAttribute("societes", societes);
            
            request.getRequestDispatcher("/WEB-INF/jsp/ca/caTotal.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors du calcul du CA", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}