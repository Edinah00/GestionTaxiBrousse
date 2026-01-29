package mg.coop.servlet.reservation;

import mg.coop.dao.ReservationDAO;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.model.Reservation;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;

import mg.coop.config.DatabaseConfig;

@WebServlet("/reservation/list")
public class ReservationListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String dateStr = req.getParameter("dateDepart");
        String taxiTrajetStr = req.getParameter("taxiTrajetId");

        LocalDate dateDepart = null;
        Integer taxiTrajetId = null;

        if (dateStr != null && !dateStr.isEmpty()) {
            dateDepart = LocalDate.parse(dateStr);
        }
        if (taxiTrajetStr != null && !taxiTrajetStr.isEmpty()) {
            taxiTrajetId = Integer.parseInt(taxiTrajetStr);
        }

        try (Connection conn = DatabaseConfig.getConnection()) {

            ReservationDAO reservationDAO = new ReservationDAO(conn);
            TaxiTrajetDAO taxiTrajetDAO = new TaxiTrajetDAO(conn);

            // Liste des réservations filtrées
            List<Reservation> reservations =
                reservationDAO.findReservations(taxiTrajetId);

            // Chiffre d'affaires
            double chiffreAffaires = reservationDAO.calculerChiffreAffaires(dateDepart, taxiTrajetId);

            req.setAttribute("reservations", reservations);
            req.setAttribute("taxisTrajets", taxiTrajetDAO.findAll());
            req.setAttribute("chiffreAffaires", chiffreAffaires);

            req.getRequestDispatcher("/WEB-INF/jsp/reservation/list.jsp")
            .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
