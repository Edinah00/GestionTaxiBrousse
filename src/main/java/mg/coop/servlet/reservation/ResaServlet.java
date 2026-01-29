package mg.coop.servlet.reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import mg.coop.config.DatabaseConfig;
import mg.coop.dao.*;
import mg.coop.model.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/resa")
public class ResaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String taxiTrajetIdStr = req.getParameter("taxiTrajetId");

        if (taxiTrajetIdStr == null || taxiTrajetIdStr.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Taxi trajet manquant");
            return;
        }

        int taxiTrajetId = Integer.parseInt(taxiTrajetIdStr);

        try (Connection conn = DatabaseConfig.getConnection()) {

            TaxiTrajetDAO taxiTrajetDAO = new TaxiTrajetDAO(conn);
            CategoriePlaceDAO categorieDAO = new CategoriePlaceDAO(conn);
            TypePassagerDAO typePassagerDAO = new TypePassagerDAO(conn);

            TaxiTrajet taxiTrajet = taxiTrajetDAO.findById(taxiTrajetId);

            req.setAttribute("taxiTrajet", taxiTrajet);
            req.setAttribute("categories", categorieDAO.findAll());
            req.setAttribute("typesPassager", typePassagerDAO.findAll());

            req.getRequestDispatcher("/WEB-INF/jsp/reservation/exemple.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    try (Connection conn = DatabaseConfig.getConnection()) {

        conn.setAutoCommit(false); // TRANSACTION

        ReservationDAO reservationDAO = new ReservationDAO(conn);
        ReservationPlaceDAO reservationPlaceDAO = new ReservationPlaceDAO(conn);
        PlacePassagerDAO placePassagerDAO = new PlacePassagerDAO(conn);

        // 1️⃣ Création réservation
        Reservation r = new Reservation();
        TaxiTrajet tt = new TaxiTrajet();
        tt.setId(Integer.parseInt(req.getParameter("taxiTrajetId")));
        r.setTaxiTrajet(tt);
        r.setNomClient(req.getParameter("nomClient"));
        r.setTelephone(req.getParameter("telephone"));

        String[] numeros = req.getParameterValues("numeroPlace[]");
        r.setNbPlaces(numeros.length);

        int reservationId = reservationDAO.createAndReturnId(r);

        // 2️⃣ Insertion des places
        String[] categorieIds = req.getParameterValues("categoriePlaceId[]");
        String[] typePassagerIds = req.getParameterValues("typePassagerId[]");

        for (int i = 0; i < numeros.length; i++) {

            int numeroPlace = Integer.parseInt(numeros[i]);
            int categId = Integer.parseInt(categorieIds[i]);
            int typePassagerId = Integer.parseInt(typePassagerIds[i]);

            // ✅ Vérifier si la place est déjà prise pour ce taxi_trajet
            if (reservationPlaceDAO.isPlaceTaken(tt.getId(), numeroPlace)) {
                conn.rollback(); // Annuler toute la transaction
                req.setAttribute("erreur", "La place n°" + numeroPlace + " est déjà réservée pour ce voyage.");
                req.getRequestDispatcher("/WEB-INF/jsp/reservation/exemple.jsp")
                   .forward(req, resp);
                return; // stop
            }

            // Récupérer ou créer PlacePassager
            PlacePassager pp = placePassagerDAO.findOrCreateObj(categId, typePassagerId);

            // Créer ReservationPlace
            Reservation rObj = new Reservation();
            rObj.setId(reservationId);

            ReservationPlace rp = new ReservationPlace();
            rp.setReservation(rObj);
            rp.setNumeroPlace(numeroPlace);
            rp.setPlacePassager(pp);

            reservationPlaceDAO.create(rp);
        }

        conn.commit();
        resp.sendRedirect(req.getContextPath() + "/pay?reservationId=" + reservationId);

    } catch (Exception e) {
        e.printStackTrace();
        throw new ServletException(e);
    }
}

}
