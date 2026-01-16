package mg.coop.servlet.trajet;

import mg.coop.dao.PersonneDAO;
import mg.coop.dao.TaxiBrousseDAO;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.dao.TrajetDAO;
import mg.coop.model.TaxiTrajet;
import mg.coop.model.Trajet;
import mg.coop.model.Personne;
import mg.coop.model.TaxiBrousse;
import mg.coop.util.ValeurReelUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/taxitrajet")
public class TaxiTrajetServlet extends HttpServlet {

    private TaxiTrajetDAO taxiTrajetDao = new TaxiTrajetDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null)
            action = "liste";

        try {
            switch (action) {
                case "add":
                    // Charger trajets et chauffeurs pour dropdown
                    List<Trajet> trajets = TrajetDAO.findAll();
                    List<Personne> chauffeurs = PersonneDAO.getAllChauffeurs();
                    List<TaxiBrousse> taxis = TaxiBrousseDAO.findAll();
                    request.setAttribute("taxis", taxis);

                    request.setAttribute("trajets", trajets);
                    request.setAttribute("chauffeurs", chauffeurs);
                    request.getRequestDispatcher("/WEB-INF/jsp/taxitrajet/form.jsp")
                            .forward(request, response);
                    break;

                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    TaxiTrajet tt = taxiTrajetDao.findById(idEdit);
                    request.setAttribute("taxitrajet", tt);

                    // Charger trajets et chauffeurs pour dropdown
                    List<TaxiBrousse> taxiList = TaxiBrousseDAO.findAll();
                    request.setAttribute("taxis", taxiList);
                    request.setAttribute("trajets", TrajetDAO.findAll());
                    request.setAttribute("chauffeurs", PersonneDAO.getAllChauffeurs());

                    request.getRequestDispatcher("/WEB-INF/jsp/taxitrajet/form.jsp")
                            .forward(request, response);
                    break;

                case "delete":
                    int idDelete = Integer.parseInt(request.getParameter("id"));
                    TaxiTrajet taxiTrajet = new TaxiTrajet();
                    taxiTrajet.setId(idDelete);
                    TaxiTrajetDAO.delete(taxiTrajet);
                    response.sendRedirect(request.getContextPath() + "/taxitrajet?action=liste");
                    break;

                case "liste":
                default:
                    String trajetFiltre = request.getParameter("trajet");

                    List<TaxiTrajet> liste;
                    if (trajetFiltre == null || trajetFiltre.isEmpty()) {
                        liste = TaxiTrajetDAO.findAll();
                    } else {
                        liste = taxiTrajetDao.findByTrajet(trajetFiltre);
                    }

                    // Calculer valeur max et valeur réelle pour chaque taxi-trajet
                    for (TaxiTrajet t : liste) {
                        double valMax = TaxiTrajetDAO.getValeurMax(t);
                        double valReel = TaxiTrajetDAO.getValeurReel(t.getId());
                        t.setValMax(valMax);
                        t.setValReel(valReel);
                    }

                    // Liste des trajets pour la dropdown
                    List<String> trajetsDisponibles = taxiTrajetDao.getAllTrajets();

                    request.setAttribute("taxitrajets", liste);
                    request.setAttribute("trajetsDisponibles", trajetsDisponibles);

                    request.getRequestDispatcher("/WEB-INF/jsp/taxitrajet/liste.jsp")
                            .forward(request, response);
                    break;
                    
                case "detailsValeurReel":
                    int idTaxiTrajet = Integer.parseInt(request.getParameter("id"));
                    TaxiTrajet taxiTrajetDetails = taxiTrajetDao.findById(idTaxiTrajet);
                    
                    // Récupérer les informations détaillées
                    ValeurReelUtil.VentesParCategorie ventes = 
                        ValeurReelUtil.getVentesParCategorie(idTaxiTrajet);
                    double valeurMax = TaxiTrajetDAO.getValeurMax(taxiTrajetDetails);
                    
                    request.setAttribute("taxitrajet", taxiTrajetDetails);
                    request.setAttribute("ventes", ventes);
                    request.setAttribute("valeurMax", valeurMax);
                    
                    request.getRequestDispatcher("/WEB-INF/jsp/taxitrajet/detailsValeurReel.jsp")
                            .forward(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
                    ? Integer.parseInt(request.getParameter("id"))
                    : 0;

            TaxiTrajet tt = new TaxiTrajet();
            tt.setId(id);

            tt.setTaxiId(Integer.parseInt(request.getParameter("taxiId")));
            tt.setTrajetId(Integer.parseInt(request.getParameter("trajetId")));
            tt.setChauffeurId(Integer.parseInt(request.getParameter("chauffeurId")));

            String aideChauffeurId = request.getParameter("aideChauffeurId");
            if (aideChauffeurId != null && !aideChauffeurId.isEmpty()) {
                tt.setAideChauffeurId(Integer.parseInt(aideChauffeurId));
            }

            tt.setDateHeureDepart(LocalDateTime.parse(request.getParameter("dateHeureDepart")));

            if (id > 0) {
                taxiTrajetDao.update(tt);
            } else {
                taxiTrajetDao.save(tt);
            }

            response.sendRedirect(request.getContextPath() + "/taxitrajet?action=liste");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}