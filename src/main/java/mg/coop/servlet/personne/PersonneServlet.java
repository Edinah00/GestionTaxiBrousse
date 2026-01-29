package mg.coop.servlet.personne;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.PersonneDAO;
import mg.coop.model.Personne;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/personnes")
public class PersonneServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection conn = DatabaseConfig.getConnection()) {
            PersonneDAO dao = new PersonneDAO(conn);

            if ("form".equals(action)) {
                // Formulaire d'ajout
                req.getRequestDispatcher("/WEB-INF/jsp/personne/form.jsp")
                   .forward(req, resp);

            } else if ("edit".equals(action)) {
                // Formulaire de modification
                int id = Integer.parseInt(req.getParameter("id"));
                Personne p = dao.findById(id);
                req.setAttribute("personne", p);
                req.getRequestDispatcher("/WEB-INF/jsp/personne/form.jsp")
                   .forward(req, resp);

            } else if ("delete".equals(action)) {
                // Suppression (à implémenter dans DAO si besoin)
                resp.sendRedirect(req.getContextPath() + "/personnes");

            } else {
                // Liste avec recherche
                String nom = req.getParameter("nom");
                String role = req.getParameter("role");

                List<Personne> personnes = dao.search(nom, role);
                req.setAttribute("personnes", personnes);
                req.getRequestDispatcher("/WEB-INF/jsp/personne/list.jsp")
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
            PersonneDAO dao = new PersonneDAO(conn);

            String idStr = req.getParameter("id");
            String nom = req.getParameter("nom");
            String telephone = req.getParameter("telephone");
            String role = req.getParameter("role");

            Personne p = new Personne();
            p.setNom(nom);
            p.setTelephone(telephone);
            p.setRole(role);

            if (idStr != null && !idStr.isEmpty()) {
                // Modification
                p.setId(Integer.parseInt(idStr));
                dao.update(p);
            } else {
                // Création
                dao.create(p);
            }

            resp.sendRedirect(req.getContextPath() + "/personnes");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}