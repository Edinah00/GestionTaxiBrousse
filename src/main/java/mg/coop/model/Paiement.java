package mg.coop.model;

import java.time.LocalDateTime;

public class Paiement {
    private int id;

    private Reservation reservation;

    private String typePaiement;
    private String modePaiement;
    private double montant;
    private LocalDateTime datePaiement;
    public Paiement() {
    }
    public Paiement(int id, Reservation reservation, String typePaiement, String modePaiement, double montant,
            LocalDateTime datePaiement) {
        this.id = id;
        this.reservation = reservation;
        this.typePaiement = typePaiement;
        this.modePaiement = modePaiement;
        this.montant = montant;
        this.datePaiement = datePaiement;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Reservation getReservation() {
        return reservation;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    public String getTypePaiement() {
        return typePaiement;
    }
    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }
    public String getModePaiement() {
        return modePaiement;
    }
    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
    public double getMontant() {
        return montant;
    }
    public void setMontant(double montant) {
        this.montant = montant;
    }
    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }
    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
}
