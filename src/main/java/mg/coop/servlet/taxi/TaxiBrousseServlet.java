package mg.coop.servlet.taxi;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.*;
import mg.coop.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/taxis")
public class TaxiBrousseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection conn = DatabaseConfig.getConnection()) {
            TaxiBrousseDAO dao = new TaxiBrousseDAO(conn);

            if ("form".equals(action)) {
                // Charger les coopératives et types de voiture
                CooperativeDAO coopDAO = new CooperativeDAO(conn);
                TypeVoitureDAO tvDAO = new TypeVoitureDAO(conn);

                req.setAttribute("cooperatives", coopDAO.findAll());
                req.setAttribute("typeVoitures", tvDAO.findAll());

                req.getRequestDispatcher("/WEB-INF/jsp/taxi/form.jsp")
                   .forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                // TODO: implémenter findById dans TaxiBrousseDAO
                
                CooperativeDAO coopDAO = new CooperativeDAO(conn);
                TypeVoitureDAO tvDAO = new TypeVoitureDAO(conn);

                req.setAttribute("cooperatives", coopDAO.findAll());
                req.setAttribute("typeVoitures", tvDAO.findAll());

                req.getRequestDispatcher("/WEB-INF/jsp/taxi/form.jsp")
                   .forward(req, resp);

            } else {
                // Liste avec recherche
                String immat = req.getParameter("immatriculation");
                String typeVoitureIdStr = req.getParameter("typeVoitureId");
                Integer typeVoitureId = (typeVoitureIdStr != null && !typeVoitureIdStr.isEmpty()) 
                    ? Integer.parseInt(typeVoitureIdStr) : null;

                List<TaxiBrousse> taxis = dao.search(immat, typeVoitureId);

                TypeVoitureDAO tvDAO = new TypeVoitureDAO(conn);
                req.setAttribute("taxis", taxis);
                req.setAttribute("typeVoitures", tvDAO.findAll());

                req.getRequestDispatcher("/WEB-INF/jsp/taxi/list.jsp")
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
            TaxiBrousseDAO dao = new TaxiBrousseDAO(conn);

            int cooperativeId = Integer.parseInt(req.getParameter("cooperativeId"));
            int typeVoitureId = Integer.parseInt(req.getParameter("typeVoitureId"));
            String immatriculation = req.getParameter("immatriculation");

            TaxiBrousse taxi = new TaxiBrousse();
            
            Cooperative coop = new Cooperative();
            coop.setId(cooperativeId);
            taxi.setCooperative(coop);

            TypeVoiture tv = new TypeVoiture();
            tv.setId(typeVoitureId);
            taxi.setTypeVoiture(tv);

            taxi.setImmatriculation(immatriculation);

            dao.create(taxi);

            resp.sendRedirect(req.getContextPath() + "/taxis");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}