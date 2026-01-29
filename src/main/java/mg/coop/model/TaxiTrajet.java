package mg.coop.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TaxiTrajet {
    private int id;
    private TaxiBrousse taxi;
    private Trajet trajet;
    private Personne chauffeur;
    private Personne aideChauffeur;
    private LocalDateTime dateHeureDepart;
    private double valeurMax;
    private double CA;

    // Propriétés CA détaillées
    private double caBillet;
    private double caPub;
    private double caPubPaye;      // NOUVEAU
    private double caPubReste;     // NOUVEAU
    private double caTotal;

    // Constructeurs
    public TaxiTrajet() {}

    public TaxiTrajet(int id, TaxiBrousse taxi, Trajet trajet, Personne chauffeur, 
                      Personne aideChauffeur, LocalDateTime dateHeureDepart) {
        this.id = id;
        this.taxi = taxi;
        this.trajet = trajet;
        this.chauffeur = chauffeur;
        this.aideChauffeur = aideChauffeur;
        this.dateHeureDepart = dateHeureDepart;
    }

    // Getters et Setters existants
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TaxiBrousse getTaxi() { return taxi; }
    public void setTaxi(TaxiBrousse taxi) { this.taxi = taxi; }

    public Trajet getTrajet() { return trajet; }
    public void setTrajet(Trajet trajet) { this.trajet = trajet; }

    public Personne getChauffeur() { return chauffeur; }
    public void setChauffeur(Personne chauffeur) { this.chauffeur = chauffeur; }

    public Personne getAideChauffeur() { return aideChauffeur; }
    public void setAideChauffeur(Personne aideChauffeur) { this.aideChauffeur = aideChauffeur; }

    public LocalDateTime getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(LocalDateTime dateHeureDepart) { 
        this.dateHeureDepart = dateHeureDepart; 
    }

    public double getValeurMax() { return valeurMax; }
    public void setValeurMax(double valeurMax) { this.valeurMax = valeurMax; }

    public double getCA() { return CA; }
    public void setCA(double cA) { CA = cA; }

    // Getters et setters CA
    public double getCaBillet() { return caBillet; }
    public void setCaBillet(double caBillet) { this.caBillet = caBillet; }

    public double getCaPub() { return caPub; }
    public void setCaPub(double caPub) { this.caPub = caPub; }

    public double getCaPubPaye() { return caPubPaye; }
    public void setCaPubPaye(double caPubPaye) { this.caPubPaye = caPubPaye; }

    public double getCaPubReste() { return caPubReste; }
    public void setCaPubReste(double caPubReste) { this.caPubReste = caPubReste; }

    public double getCaTotal() { return caTotal; }
    public void setCaTotal(double caTotal) { this.caTotal = caTotal; }
}