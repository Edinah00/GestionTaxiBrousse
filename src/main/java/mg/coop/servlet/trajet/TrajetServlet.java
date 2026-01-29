package mg.coop.servlet.trajet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.TrajetDAO;
import mg.coop.model.Trajet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/trajets")
public class TrajetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection conn = DatabaseConfig.getConnection()) {
            TrajetDAO dao = new TrajetDAO(conn);

            if ("form".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/trajet/form.jsp")
                   .forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Trajet trajet = dao.findById(id);
                req.setAttribute("trajet", trajet);
                req.getRequestDispatcher("/WEB-INF/jsp/trajet/form.jsp")
                   .forward(req, resp);

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/trajets");

            } else {
                // Liste avec recherche
                String depart = req.getParameter("depart");
                String arrivee = req.getParameter("arrivee");

                List<Trajet> trajets = dao.search(depart, arrivee);
                req.setAttribute("trajets", trajets);

                req.getRequestDispatcher("/WEB-INF/jsp/trajet/list.jsp")
                   .forward(req, resp);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DatabaseConfig.getConnection()) {
            TrajetDAO dao = new TrajetDAO(conn);

            String idStr = req.getParameter("id");
            String depart = req.getParameter("depart");
            String arrivee = req.getParameter("arrivee");
            int distanceKm = Integer.parseInt(req.getParameter("distanceKm"));
            double prixBase = Double.parseDouble(req.getParameter("prixBase"));
            double nbrJour = Double.parseDouble(req.getParameter("nbrJour"));

            Trajet trajet = new Trajet();
            trajet.setDepart(depart);
            trajet.setArrivee(arrivee);
            trajet.setDistanceKm(distanceKm);
            trajet.setPrixBase(prixBase);
            trajet.setNbrJour(nbrJour);

            if (idStr != null && !idStr.isEmpty()) {
                trajet.setId(Integer.parseInt(idStr));
                dao.update(trajet);
            } else {
                dao.create(trajet);
            }

            resp.sendRedirect(req.getContextPath() + "/trajets");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}