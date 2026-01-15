package mg.coop.model;

public class Trajet {

    private int id;
    private String depart;
    private String arrivee;
    private int distanceKm;
    private double prixBase;
    private double pourcentageAugmentation;
    private int nombreJour;
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

    public double getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(double prixBase) {
        this.prixBase = prixBase;
    }

    public double getPourcentageAugmentation() {
        return pourcentageAugmentation;
    }

    public void setPourcentageAugmentation(double pourcentageAugmentation) {
        this.pourcentageAugmentation = pourcentageAugmentation;
    }
    public int getNombreJour() {
        return nombreJour;
    }
    public void setNombreJour(int nombreJour) {
        this.nombreJour = nombreJour;
    }
    
}
