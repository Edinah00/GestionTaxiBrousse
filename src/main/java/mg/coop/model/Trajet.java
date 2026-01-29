package mg.coop.model;

public class Trajet {

    private int id;
    private String depart;
    private String arrivee;
    private int distanceKm;
    private double nbrJour;
    private double prixBase;

    public Trajet() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getNbrJour() {
        return nbrJour;
    }

    public void setNbrJour(double nbrJour) {
        this.nbrJour = nbrJour;
    }

    public double getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(double prixBase) {
        this.prixBase = prixBase;
    }

}
