package mg.coop.servlet.paiement;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.PaiementDAO;
import mg.coop.dao.ReservationDAO;
import mg.coop.model.Paiement;
import mg.coop.model.Reservation;

@WebServlet("/paiement/form")
public class PaiementFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int reservationId =
            Integer.parseInt(req.getParameter("reservationId"));

        try (Connection conn = DatabaseConfig.getConnection()) {

            ReservationDAO rdao = new ReservationDAO(conn);
            Reservation r = rdao.findById(reservationId);

            double montant = r.getTaxiTrajet().getTrajet().getPrixBase() * r.getNbPlaces();

            req.setAttribute("reservation", r);
            req.setAttribute("montant", montant);

            req.getRequestDispatcher("/WEB-INF/jsp/paiement/form.jsp")
            .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int reservationId =
            Integer.parseInt(req.getParameter("reservationId"));

        String typePaiement = "TOTAL RESERVATION";
        String modePaiement = req.getParameter("modePaiement");
        double montant = Double.parseDouble(req.getParameter("montant"));

        try (Connection conn = DatabaseConfig.getConnection()) {

            PaiementDAO dao = new PaiementDAO(conn);

            Paiement p = new Paiement();

            Reservation r = new Reservation();
            r.setId(reservationId);

            p.setReservation(r);
            p.setTypePaiement(typePaiement);
            p.setModePaiement(modePaiement);
            p.setMontant(montant);
            p.setDatePaiement(LocalDateTime.now());

            dao.insert(p);

            resp.sendRedirect(
                req.getContextPath() + "/reservation/list"
            );

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
