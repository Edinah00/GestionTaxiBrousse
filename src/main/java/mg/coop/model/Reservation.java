package mg.coop.model;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private int id;

    private TaxiTrajet taxiTrajet;

    private String nomClient;
    private String telephone;

    private int nbPlaces;
    private LocalDateTime dateReservation;
    private String statut;
    private boolean etat;

    private List<ReservationPlace> places;

    public Reservation() {
    }

    public Reservation(int id, TaxiTrajet taxiTrajet, String nomClient, int nbPlaces, LocalDateTime dateReservation) {
        this.id = id;
        this.taxiTrajet = taxiTrajet;
        this.nomClient = nomClient;
        this.nbPlaces = nbPlaces;
        this.dateReservation = dateReservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaxiTrajet getTaxiTrajet() {
        return taxiTrajet;
    }

    public void setTaxiTrajet(TaxiTrajet taxiTrajet) {
        this.taxiTrajet = taxiTrajet;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<ReservationPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<ReservationPlace> places) {
        this.places = places;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }
}
