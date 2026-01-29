package mg.coop.servlet.voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.*;
import mg.coop.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;

@WebServlet("/taxi-trajets/crud")
public class TaxiTrajetCrudServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection conn = DatabaseConfig.getConnection()) {
            TaxiTrajetDAO dao = new TaxiTrajetDAO(conn);

            if ("form".equals(action)) {
                // Charger les données pour le formulaire
                TaxiBrousseDAO taxiDAO = new TaxiBrousseDAO(conn);
                TrajetDAO trajetDAO = new TrajetDAO(conn);
                PersonneDAO personneDAO = new PersonneDAO(conn);

                req.setAttribute("taxis", taxiDAO.findAll());
                req.setAttribute("trajets", trajetDAO.search(null, null));
                req.setAttribute("chauffeurs", personneDAO.findByRole("CHAUFFEUR"));
                req.setAttribute("aides", personneDAO.findByRole("AIDE CHAUFFEUR"));

                req.getRequestDispatcher("/WEB-INF/jsp/taxi-trajet/form.jsp")
                   .forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                TaxiTrajet tt = dao.findById(id);

                TaxiBrousseDAO taxiDAO = new TaxiBrousseDAO(conn);
                TrajetDAO trajetDAO = new TrajetDAO(conn);
                PersonneDAO personneDAO = new PersonneDAO(conn);

                req.setAttribute("taxiTrajet", tt);
                req.setAttribute("taxis", taxiDAO.findAll());
                req.setAttribute("trajets", trajetDAO.search(null, null));
                req.setAttribute("chauffeurs", personneDAO.findByRole("CHAUFFEUR"));
                req.setAttribute("aides", personneDAO.findByRole("AIDE CHAUFFEUR"));

                req.getRequestDispatcher("/WEB-INF/jsp/taxi-trajet/form.jsp")
                   .forward(req, resp);

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/taxi-trajets");

            } else {
                resp.sendRedirect(req.getContextPath() + "/taxi-trajets");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DatabaseConfig.getConnection()) {
            TaxiTrajetDAO dao = new TaxiTrajetDAO(conn);

            String idStr = req.getParameter("id");
            int taxiId = Integer.parseInt(req.getParameter("taxiId"));
            int trajetId = Integer.parseInt(req.getParameter("trajetId"));
            int chauffeurId = Integer.parseInt(req.getParameter("chauffeurId"));
            String aideChauffeurIdStr = req.getParameter("aideChauffeurId");
            String dateHeureDepart = req.getParameter("dateHeureDepart");

            TaxiTrajet tt = new TaxiTrajet();

            TaxiBrousse taxi = new TaxiBrousse();
            taxi.setId(taxiId);
            tt.setTaxi(taxi);

            Trajet trajet = new Trajet();
            trajet.setId(trajetId);
            tt.setTrajet(trajet);

            Personne chauffeur = new Personne();
            chauffeur.setId(chauffeurId);
            tt.setChauffeur(chauffeur);

            if (aideChauffeurIdStr != null && !aideChauffeurIdStr.isEmpty()) {
                Personne aide = new Personne();
                aide.setId(Integer.parseInt(aideChauffeurIdStr));
                tt.setAideChauffeur(aide);
            }

            tt.setDateHeureDepart(LocalDateTime.parse(dateHeureDepart));

            if (idStr != null && !idStr.isEmpty()) {
                tt.setId(Integer.parseInt(idStr));
                dao.update(tt);
            } else {
                dao.create(tt);
            }

            resp.sendRedirect(req.getContextPath() + "/taxi-trajets");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}