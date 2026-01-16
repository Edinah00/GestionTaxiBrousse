package mg.coop.servlet.reservation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mg.coop.dao.ReservationDAO;
import mg.coop.dao.TaxiTrajetDAO;
import mg.coop.model.Reservation;
import mg.coop.model.TaxiTrajet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    
    private ReservationDAO reservationDAO;
    private TaxiTrajetDAO taxiTrajetDAO;
    
    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
        taxiTrajetDAO = new TaxiTrajetDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            // Action pour afficher la liste des réservations
            if ("liste".equals(action)) {
                afficherListeReservations(request, response);
                return;
            }
            
            // Action pour afficher le formulaire de réservation
            String taxiTrajetIdStr = request.getParameter("taxiTrajetId");
            
            if (taxiTrajetIdStr != null && !taxiTrajetIdStr.isEmpty()) {
                int taxiTrajetId = Integer.parseInt(taxiTrajetIdStr);
                
                // Récupérer les informations du trajet
                List<TaxiTrajet> trajets = taxiTrajetDAO.rechercherTrajets("", "", null, null);
                TaxiTrajet trajet = null;
                for (TaxiTrajet t : trajets) {
                    if (t.getId() == taxiTrajetId) {
                        trajet = t;
                        break;
                    }
                }
                
                if (trajet != null) {
                    List<Integer> placesOccupees = reservationDAO.getPlacesOccupees(taxiTrajetId);
                    
                    request.setAttribute("trajet", trajet);
                    request.setAttribute("placesOccupees", placesOccupees);
                    
                    request.getRequestDispatcher("/WEB-INF/jsp/reservation/formulaireReservation.jsp")
                           .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/rechercheVoiture");
                }
            } else {
                // Par défaut, afficher la liste des réservations
                afficherListeReservations(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur lors du chargement: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/formulaireReservation.jsp")
                   .forward(request, response);
        }
    }
    
    private void afficherListeReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Récupérer les paramètres de recherche
            String nomClient = request.getParameter("nomClient");
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");
            String taxiTrajetIdStr = request.getParameter("taxiTrajetId");
            
            Integer taxiTrajetId = null;
            if (taxiTrajetIdStr != null && !taxiTrajetIdStr.isEmpty()) {
                taxiTrajetId = Integer.parseInt(taxiTrajetIdStr);
            }
            
            // Récupérer les réservations selon les filtres
            List<Reservation> reservations;
            if (nomClient != null || dateDebut != null || dateFin != null || taxiTrajetId != null) {
                reservations = reservationDAO.searchReservations(nomClient, dateDebut, dateFin, taxiTrajetId);
            } else {
                reservations = reservationDAO.getAllReservations();
            }
            
            // Récupérer les numéros de places pour chaque réservation
            Map<Integer, List<Integer>> placesParReservation = new HashMap<>();
            for (Reservation reservation : reservations) {
                List<Integer> places = reservationDAO.getPlacesReservees(reservation.getId());
                placesParReservation.put(reservation.getId(), places);
            }
            
            // Calculer le chiffre d'affaires total
            double chiffreAffaires = 0;
            int totalPlaces = 0;
            for (Reservation r : reservations) {
                chiffreAffaires += r.getMontantPaye();
                totalPlaces += r.getNbPlaces();
            }
            
            // Récupérer la liste des taxi-trajets pour le filtre
            List<ReservationDAO.TaxiTrajetInfo> taxiTrajets = reservationDAO.getAllTaxiTrajetsWithReservations();
            
            request.setAttribute("reservations", reservations);
            request.setAttribute("placesParReservation", placesParReservation);
            request.setAttribute("chiffreAffaires", chiffreAffaires);
            request.setAttribute("totalReservations", reservations.size());
            request.setAttribute("totalPlaces", totalPlaces);
            request.setAttribute("taxiTrajets", taxiTrajets);
            
            // Conserver les paramètres de recherche
            request.setAttribute("searchNomClient", nomClient);
            request.setAttribute("searchDateDebut", dateDebut);
            request.setAttribute("searchDateFin", dateFin);
            request.setAttribute("searchTaxiTrajetId", taxiTrajetId);
            
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/listeReservations.jsp")
                   .forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur lors du chargement des réservations: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/listeReservations.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int taxiTrajetId = Integer.parseInt(request.getParameter("taxiTrajetId"));
            String nomClient = request.getParameter("nomClient");
            String telephone = request.getParameter("telephone");
            String modePaiement = request.getParameter("modePaiement");
            String typePaiement = request.getParameter("typePaiement");
            
            String[] placesSelectionnees = request.getParameterValues("places");
            
            if (placesSelectionnees == null || placesSelectionnees.length == 0) {
                request.setAttribute("erreur", "Veuillez sélectionner au moins une place");
                doGet(request, response);
                return;
            }
            
            // Vérifier la disponibilité de toutes les places
            for (String placeStr : placesSelectionnees) {
                int numeroPlace = Integer.parseInt(placeStr);
                if (!reservationDAO.verifierDisponibilitePlace(taxiTrajetId, numeroPlace)) {
                    request.setAttribute("erreur", "La place " + numeroPlace + " n'est plus disponible");
                    doGet(request, response);
                    return;
                }
            }
            
            // Récupérer le prix
            List<TaxiTrajet> trajets = taxiTrajetDAO.rechercherTrajets("", "", null, null);
            TaxiTrajet trajet = null;
            for (TaxiTrajet t : trajets) {
                if (t.getId() == taxiTrajetId) {
                    trajet = t;
                    break;
                }
            }
            
            if (trajet == null) {
                request.setAttribute("erreur", "Trajet non trouvé");
                doGet(request, response);
                return;
            }
            
            double montantTotal = trajet.getPrixBase() * placesSelectionnees.length;
            double montantPaiement = montantTotal;
            
            // Si c'est un acompte, calculer 50%
            if ("ACOMPTE".equals(typePaiement)) {
                montantPaiement = montantTotal * 0.5;
            } else if ("TOTAL ARRIVEE".equals(typePaiement)) {
                montantPaiement = 0;
            }
            
            // Créer la réservation
            Reservation reservation = new Reservation();
            reservation.setTaxiTrajetId(taxiTrajetId);
            reservation.setNomClient(nomClient);
            reservation.setTelephone(telephone);
            reservation.setNbPlaces(placesSelectionnees.length);
            reservation.setStatut("RESERVATION");
            
            int reservationId = reservationDAO.insertReservation(reservation);
            
            if (reservationId > 0) {
                // Enregistrer les places
                for (String placeStr : placesSelectionnees) {
                    int numeroPlace = Integer.parseInt(placeStr);
                    reservationDAO.insertReservationPlace(taxiTrajetId, reservationId, numeroPlace,reservation.getStatut());
                }
                
                // Enregistrer le paiement seulement si montant > 0
                if (montantPaiement > 0) {
                    reservationDAO.insertPaiement(reservationId, typePaiement, modePaiement, montantPaiement);
                }
                
                // Redirection vers une page de confirmation
                request.setAttribute("success", "Réservation effectuée avec succès!");
                request.setAttribute("reservationId", reservationId);
                request.setAttribute("montantPaye", montantPaiement);
                request.setAttribute("montantTotal", montantTotal);
                request.getRequestDispatcher("/WEB-INF/jsp/reservation/confirmation.jsp")
                       .forward(request, response);
            } else {
                request.setAttribute("erreur", "Erreur lors de la création de la réservation");
                doGet(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur lors de la réservation: " + e.getMessage());
            doGet(request, response);
        }
    }
}