package mg.coop.servlet.voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/taxi-trajets")
public class TaxiTrajetServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {

                try (Connection conn = DatabaseConfig.getConnection()) {

                        TaxiTrajetDAO dao = new TaxiTrajetDAO(conn);

                        // listes déroulantes
                        req.setAttribute("trajets", new TrajetDAO(conn).search(null, null));
                        String trajetIdParam = req.getParameter("trajetId");

                        Integer trajetId = null;
                        if (trajetIdParam != null && !trajetIdParam.isBlank()) {
                                trajetId = Integer.parseInt(trajetIdParam);
                        }

                        req.setAttribute("list",
                                        dao.search(
                                                        null,
                                                        trajetId,
                                                        null));

                        req.getRequestDispatcher("/WEB-INF/jsp/taxi-trajet/list.jsp")
                                        .forward(req, resp);

                } catch (Exception e) {
                        throw new ServletException(e);
                }
        }
}
