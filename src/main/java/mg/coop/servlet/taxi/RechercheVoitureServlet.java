package mg.coop.servlet.taxi;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.model.TaxiTrajet;


@WebServlet("/rechercheVoiture")
public class RechercheVoitureServlet extends HttpServlet {
    
    private TaxiTrajetDAO taxiTrajetDAO;
    
    @Override
    public void init() {
        taxiTrajetDAO = new TaxiTrajetDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Récupérer les lieux pour les select
            List<String> lieuxDepart = taxiTrajetDAO.getLieuxDepart();
            List<String> lieuxArrivee = taxiTrajetDAO.getLieuxArrivee();
            
            request.setAttribute("lieuxDepart", lieuxDepart);
            request.setAttribute("lieuxArrivee", lieuxArrivee);
            
            // Recherche si des paramètres sont fournis
            String depart = request.getParameter("depart");
            String arrivee = request.getParameter("arrivee");
            String dateDepart = request.getParameter("dateDepart");
            String heureDepart = request.getParameter("heureDepart");
            
            if (depart != null && arrivee != null) {
                List<TaxiTrajet> trajets = taxiTrajetDAO.rechercherTrajets(
                    depart, arrivee, dateDepart, heureDepart
                );
                request.setAttribute("trajets", trajets);
                request.setAttribute("recherche", true);
            }

            request.getRequestDispatcher("/WEB-INF/jsp/reservation/rechercheVoiture.jsp")
                   .forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur lors de la recherche: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/rechercheVoiture.jsp")
                   .forward(request, response);
        } catch (Exception e) {
    e.printStackTrace();
    request.setAttribute("erreur", "Erreur interne: " + e.getMessage());
    request.getRequestDispatcher("/WEB-INF/jsp/reservation/rechercheVoiture.jsp")
           .forward(request, response);
}

    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}