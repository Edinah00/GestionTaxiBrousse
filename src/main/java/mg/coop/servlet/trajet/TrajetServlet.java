package mg.coop.servlet.trajet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.dao.TrajetDAO;
import mg.coop.model.TaxiTrajet;
import mg.coop.model.Trajet;

@WebServlet("/trajet")
public class TrajetServlet extends HttpServlet {

    private TrajetDAO trajetDAO;

    @Override
    public void init() {
        trajetDAO = new TrajetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("liste")) {
                // Récupérer paramètres de recherche
                String depart = request.getParameter("depart");
                String arrivee = request.getParameter("arrivee");
                String dateDepart = request.getParameter("date_depart");
                String heureDepart = request.getParameter("heure_depart");

                //conversion

                List<Trajet> liste;
                
                // Recherche ou liste complète
                if (depart != null || arrivee != null) {
                    liste = trajetDAO.search(depart, arrivee , dateDepart, heureDepart);
                } else {
                    liste = trajetDAO.findAll();
                }
                
                // Récupérer les lieux pour les dropdowns de recherche
                List<String> departs = TaxiTrajetDAO.getLieuxDepart();
                List<String> arrivees = TaxiTrajetDAO.getLieuxArrivee();
                
                request.setAttribute("trajets", liste);
                request.setAttribute("departs", departs);
                request.setAttribute("arrivees", arrivees);
                
                request.getRequestDispatcher("/WEB-INF/jsp/trajet/listeTrajet.jsp")
                       .forward(request, response);
            }
            else if (action.equals("add")) {
                request.getRequestDispatcher("/WEB-INF/jsp/trajet/formulaireTrajet.jsp")
                       .forward(request, response);
            }
            else if (action.equals("edit")) {
                int id = Integer.parseInt(request.getParameter("id"));
Trajet t = new Trajet();
                t.setId(id);

                TrajetDAO.findById(t);
                
                if (t != null) {
                    request.setAttribute("trajet", t);
                    request.getRequestDispatcher("/WEB-INF/jsp/trajet/formulaireTrajet.jsp")
                           .forward(request, response);
                } else {
                    response.sendRedirect("trajet?action=liste");
                }
            }
            else if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
             
                    Trajet t = new Trajet();
                    t.setId(id);    
                    trajetDAO.delete(t);
                    response.sendRedirect("trajet?action=liste");
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            int id = (idParam == null || idParam.isEmpty()) ? 0 : Integer.parseInt(idParam);

            Trajet t = new Trajet();
            t.setId(id);
            t.setDepart(request.getParameter("depart"));
            t.setArrivee(request.getParameter("arrivee"));
            
            String distanceStr = request.getParameter("distance_km");
            t.setDistanceKm((distanceStr != null && !distanceStr.isEmpty()) 
                ? Integer.parseInt(distanceStr) : 0);
            
            String prixStr = request.getParameter("prix_base");
            t.setPrixBase((prixStr != null && !prixStr.isEmpty())
                ? Double.parseDouble(prixStr) : 0.0);
            
            String pourcStr = request.getParameter("pourcentage_augmentation");
            t.setPourcentageAugmentation((pourcStr != null && !pourcStr.isEmpty())
                ? Double.parseDouble(pourcStr) : 0.0);

            if (id == 0) {
                trajetDAO.save(t);
            } else {
                trajetDAO.update(t);
            }

            response.sendRedirect("trajet?action=liste");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}