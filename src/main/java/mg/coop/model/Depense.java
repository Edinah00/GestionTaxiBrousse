package mg.coop.model;

import java.time.LocalDate;

public class Depense {

    private int id;
    private int cooperativeId;
    private String type; // CARBURANT, REPARATION, VISITE_TECHNIQUE, SALAIRE
    private double montant;
    private LocalDate dateDepense;

    public Depense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCooperativeId() {
        return cooperativeId;
    }

    public void setCooperativeId(int cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDate dateDepense) {
        this.dateDepense = dateDepense;
    }
}
