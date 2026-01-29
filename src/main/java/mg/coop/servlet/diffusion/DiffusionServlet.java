package mg.coop.servlet.diffusion;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.DiffusionDAO;
import mg.coop.dao.SocieteDAO;
import mg.coop.model.Diffusion;
import mg.coop.model.Societe;

@WebServlet("/diff")
public class DiffusionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (Connection conn = mg.coop.config.DatabaseConfig.getConnection()) {
            SocieteDAO societeDAO = new SocieteDAO(conn);
            List<mg.coop.model.Societe> societes = societeDAO.getAllSocietes();
            req.setAttribute("societes", societes);
            req.getRequestDispatcher("/WEB-INF/jsp/diffusion/ca.jsp")
               .forward(req, resp);

            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String dateStr = req.getParameter("date");   
        int idSociete = Integer.parseInt(req.getParameter("societe"));
        LocalDate date = LocalDate.parse(dateStr);
        int annee = date.getYear();
        int mois = date.getMonthValue();
        try (Connection conn = DatabaseConfig.getConnection()) {

            DiffusionDAO dao = new DiffusionDAO(conn);
            SocieteDAO societeDAO = new SocieteDAO(conn);
            Diffusion d = dao.getCADiffusionParMois(mois, annee);
            double reste = dao.resteAPayer(idSociete, mois, annee);
            double dejaPaye = dao.dejaPaye(idSociete, mois, annee);
            List<mg.coop.model.Societe> societes = societeDAO.getAllSocietes();
            Societe soc = societeDAO.findById(idSociete);
            req.setAttribute("societes", societes);
            req.setAttribute("diffusions", d);
            req.setAttribute("date", date);
            req.setAttribute("reste", reste);
            req.setAttribute("dejaPaye", dejaPaye);
            req.setAttribute("soc", soc);
             req.getRequestDispatcher("/WEB-INF/jsp/diffusion/ca.jsp")
            .forward(req, resp);
        
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}