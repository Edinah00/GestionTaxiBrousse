package mg.coop.model;

import java.time.LocalDate;

public class Depense {
    private int id;

    private Cooperative cooperative;

    private String type;
    private double montant;
    private LocalDate dateDepense;
    public Depense() {
    }
    public Depense(int id, Cooperative cooperative, String type, double montant, LocalDate dateDepense) {
        this.id = id;
        this.cooperative = cooperative;
        this.type = type;
        this.montant = montant;
        this.dateDepense = dateDepense;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Cooperative getCooperative() {
        return cooperative;
    }
    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
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
