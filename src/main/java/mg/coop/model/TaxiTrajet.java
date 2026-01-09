package mg.coop.model;

import java.time.LocalDateTime;

/*public class TaxiTrajet {

    private int id;
    private int taxiId;
    private int trajetId;
    private int chauffeurId;
    private Integer aideChauffeurId;
    private LocalDateTime dateHeureDepart;

    public TaxiTrajet() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public int getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(int trajetId) {
        this.trajetId = trajetId;
    }

    public int getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(int chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public Integer getAideChauffeurId() {
        return aideChauffeurId;
    }

    public void setAideChauffeurId(Integer aideChauffeurId) {
        this.aideChauffeurId = aideChauffeurId;
    }

    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(LocalDateTime dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }
}
*/


import java.sql.Timestamp;

public class TaxiTrajet {
    private int id;
    private int taxiId;
    private String immatriculation;
    private String typeVoiture;
    private int nbrPlaces;
    private int trajetId;
    private String depart;
    private String arrivee;
    private int distanceKm;
    private double prixBase;
    private int chauffeurId;
    private String nomChauffeur;
    private Timestamp dateHeureDepart;
    private int placesReservees;
    private String cooperative;
    
    // Constructeurs
    public TaxiTrajet() {}
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getTaxiId() { return taxiId; }
    public void setTaxiId(int taxiId) { this.taxiId = taxiId; }
    
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { 
        this.immatriculation = immatriculation; 
    }
    
    public String getTypeVoiture() { return typeVoiture; }
    public void setTypeVoiture(String typeVoiture) { 
        this.typeVoiture = typeVoiture; 
    }
    
    public int getNbrPlaces() { return nbrPlaces; }
    public void setNbrPlaces(int nbrPlaces) { 
        this.nbrPlaces = nbrPlaces; 
    }
    
    public int getTrajetId() { return trajetId; }
    public void setTrajetId(int trajetId) { this.trajetId = trajetId; }
    
    public String getDepart() { return depart; }
    public void setDepart(String depart) { this.depart = depart; }
    
    public String getArrivee() { return arrivee; }
    public void setArrivee(String arrivee) { this.arrivee = arrivee; }
    
    public int getDistanceKm() { return distanceKm; }
    public void setDistanceKm(int distanceKm) { 
        this.distanceKm = distanceKm; 
    }
    
    public double getPrixBase() { return prixBase; }
    public void setPrixBase(double prixBase) { 
        this.prixBase = prixBase; 
    }
    
    public int getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(int chauffeurId) { 
        this.chauffeurId = chauffeurId; 
    }
    
    public String getNomChauffeur() { return nomChauffeur; }
    public void setNomChauffeur(String nomChauffeur) { 
        this.nomChauffeur = nomChauffeur; 
    }
    
    public Timestamp getDateHeureDepart() { return dateHeureDepart; }
    public void setDateHeureDepart(Timestamp dateHeureDepart) { 
        this.dateHeureDepart = dateHeureDepart; 
    }
    
    public int getPlacesReservees() { return placesReservees; }
    public void setPlacesReservees(int placesReservees) { 
        this.placesReservees = placesReservees; 
    }
    
    public String getCooperative() { return cooperative; }
    public void setCooperative(String cooperative) { 
        this.cooperative = cooperative; 
    }
    
    public int getPlacesDisponibles() {
        return nbrPlaces - placesReservees;
    }
}