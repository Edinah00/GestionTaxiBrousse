package mg.coop.servlet.taxi;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.TypeVoitureDAO;
import mg.coop.model.TypeVoiture;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/type-voitures")
public class TypeVoitureServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection conn = DatabaseConfig.getConnection()) {
            TypeVoitureDAO dao = new TypeVoitureDAO(conn);

            if ("form".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/jsp/type-voiture/form.jsp")
                   .forward(req, resp);

            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                TypeVoiture tv = dao.findById(id);
                req.setAttribute("typeVoiture", tv);
                req.getRequestDispatcher("/WEB-INF/jsp/type-voiture/form.jsp")
                   .forward(req, resp);

            } else {
                List<TypeVoiture> typeVoitures = dao.findAll();
                req.setAttribute("typeVoitures", typeVoitures);
                req.getRequestDispatcher("/WEB-INF/jsp/type-voiture/list.jsp")
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
            TypeVoitureDAO dao = new TypeVoitureDAO(conn);

            String idStr = req.getParameter("id");
            String libelle = req.getParameter("libelle");
            int nbrPlaces = Integer.parseInt(req.getParameter("nbrPlaces"));

            TypeVoiture tv = new TypeVoiture();
            tv.setLibelle(libelle);
            tv.setNbrPlaces(nbrPlaces);

            if (idStr != null && !idStr.isEmpty()) {
                tv.setId(Integer.parseInt(idStr));
                dao.update(tv);
            } else {
                dao.create(tv);
            }

            resp.sendRedirect(req.getContextPath() + "/type-voitures");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}