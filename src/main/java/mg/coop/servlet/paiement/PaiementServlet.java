package mg.coop.servlet.paiement;

import java.io.IOException;
import java.sql.Connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.PaiementDAO;
import mg.coop.model.Paiement;

import java.util.List;
import java.time.LocalDate;

@WebServlet("/paiement")
public class PaiementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DatabaseConfig.getConnection()) {

            String dateStr = req.getParameter("date");
            String mode = req.getParameter("mode");
            String type = req.getParameter("type");

            LocalDate date = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                date = LocalDate.parse(dateStr);
            }

            PaiementDAO dao = new PaiementDAO(conn);

            List<Paiement> paiements =
                dao.findAll(date, mode, type);

            req.setAttribute("paiements", paiements);

            req.getRequestDispatcher("/WEB-INF/jsp/paiement/list.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
