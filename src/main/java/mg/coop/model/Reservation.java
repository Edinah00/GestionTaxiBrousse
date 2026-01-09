package mg.coop.model;

import java.time.LocalDateTime;

public class Reservation {

    private int id;
    private int taxiTrajetId;

    private String nomClient;
    private String telephone;

    private int nbPlaces;
    private String statut; // RESERVATION, ATTENTE, FIL

    private LocalDateTime dateReservation;

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxiTrajetId() {
        return taxiTrajetId;
    }

    public void setTaxiTrajetId(int taxiTrajetId) {
        this.taxiTrajetId = taxiTrajetId;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
}
