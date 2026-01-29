package mg.coop.servlet.reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import mg.coop.config.DatabaseConfig;
import mg.coop.dao.ReservationDAO;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.model.Reservation;
import mg.coop.model.TaxiBrousse;
import mg.coop.model.TaxiTrajet;
import mg.coop.model.TypeVoiture;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reservation/form")
public class ReservationFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String param = req.getParameter("taxiTrajetId");
        if (param == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "taxiTrajetId manquant");
            return;
        }

        int taxiTrajetId = Integer.parseInt(param);

        try (Connection conn = mg.coop.config.DatabaseConfig.getConnection()) {

            TaxiTrajetDAO taxiTrajetDAO = new TaxiTrajetDAO(conn);

            // 1️⃣ Charger le taxi_trajet COMPLET
            TaxiTrajet taxiTrajet = taxiTrajetDAO.findById(taxiTrajetId);
            if (taxiTrajet == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "TaxiTrajet introuvable");
                return;
            }

            // 2️⃣ Récupérer le type de voiture
            TaxiBrousse taxi = taxiTrajet.getTaxi();
            if (taxi == null || taxi.getTypeVoiture() == null) {
                throw new ServletException("TypeVoiture non chargé pour le taxi");
            }

            TypeVoiture typeVoiture = taxi.getTypeVoiture();
            int nbrPlaces = typeVoiture.getNbrPlaces();

            // 3️⃣ Calcul des places libres
            List<Integer> placesLibres =
                    taxiTrajetDAO.getNumeroPlacesLibres(taxiTrajetId, nbrPlaces);

            // 4️⃣ Données pour le JSP
            req.setAttribute("taxiTrajet", taxiTrajet);
            req.setAttribute("typeVoiture", typeVoiture);
            req.setAttribute("nbrPlaces", nbrPlaces);
            req.setAttribute("placesLibres", placesLibres);

            // 5️⃣ Forward
            req.getRequestDispatcher("/WEB-INF/jsp/reservation/form.jsp")
            .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int taxiTrajetId = Integer.parseInt(req.getParameter("taxiTrajetId"));
        String nomClient = req.getParameter("nomClient");
        String telephone = req.getParameter("telephone");

        // Récupération des places sélectionnées
        String placeStr = req.getParameter("numeroPlace");
        List<Integer> numeroPlaces = new ArrayList<>();

        if (placeStr != null && !placeStr.isEmpty()) {
            for (String p : placeStr.split(",")) {
                numeroPlaces.add(Integer.parseInt(p));
            }
        }

        if (numeroPlaces.isEmpty()) {
            throw new ServletException("Aucune place sélectionnée");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {

            ReservationDAO reservationDao = new ReservationDAO(conn);

            Reservation reservation = new Reservation();

            TaxiTrajet tt = new TaxiTrajet();
            tt.setId(taxiTrajetId);
            reservation.setTaxiTrajet(tt);

            reservation.setNomClient(nomClient);
            reservation.setTelephone(telephone);
            reservation.setNbPlaces(numeroPlaces.size());
            reservation.setDateReservation(LocalDateTime.now());
            reservation.setStatut("RESERVATION");

            int reservationId = reservationDao.insertReservation(reservation, numeroPlaces);

            // 👉 redirection vers paiement
            resp.sendRedirect(
                req.getContextPath() +
                "/paiement/form?reservationId=" + reservationId
            );

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


}
