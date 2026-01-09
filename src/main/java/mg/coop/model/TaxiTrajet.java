package mg.coop.model;

import java.time.LocalDateTime;

public class TaxiTrajet {

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
