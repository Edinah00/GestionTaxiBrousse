package mg.coop.servlet.reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.TaxiTrajetDAO;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet("/reservation/search")
public class ReservationSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try (Connection conn = mg.coop.config.DatabaseConfig.getConnection()) {

            TaxiTrajetDAO dao = new TaxiTrajetDAO(conn);
            req.setAttribute("departList", dao.findAllDepart());
            req.setAttribute("arriveeList", dao.findAllArrivee());

            req.getRequestDispatcher("/WEB-INF/jsp/reservation/search.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String depart = req.getParameter("depart");
        String arrivee = req.getParameter("arrivee");

        String dateStr = req.getParameter("date");   

        LocalDate date = LocalDate.parse(dateStr);

        try (Connection conn = DatabaseConfig.getConnection()) {

            TaxiTrajetDAO dao = new TaxiTrajetDAO(conn);

            List<Map<String, Object>> taxis =
                    dao.findTaxisDisponibles(depart, arrivee, date);

            req.setAttribute("departList", dao.findAllDepart());
            req.setAttribute("arriveeList", dao.findAllArrivee());
            req.setAttribute("taxis", taxis);

            for (Map<String, Object> taxi : taxis) {
            LocalDateTime dt = (LocalDateTime) taxi.get("dateHeureDepart");
            if (dt != null) {
                taxi.put("heureDepartStr", dt.format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                taxi.put("heureDepartStr", "");
            }
        }

            req.getRequestDispatcher("/WEB-INF/jsp/reservation/search.jsp")
            .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
