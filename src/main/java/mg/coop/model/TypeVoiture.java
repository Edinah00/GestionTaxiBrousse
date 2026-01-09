package mg.coop.model;

public class TypeVoiture {

    private int id;
    private String libelle;
    private int nbrPlaces;
    private double poidsMaxBagage;
    private double consoCarburant;
    private double tarifBagage;

    public TypeVoiture() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNbrPlaces() {
        return nbrPlaces;
    }

    public void setNbrPlaces(int nbrPlaces) {
        this.nbrPlaces = nbrPlaces;
    }

    public double getPoidsMaxBagage() {
        return poidsMaxBagage;
    }

    public void setPoidsMaxBagage(double poidsMaxBagage) {
        this.poidsMaxBagage = poidsMaxBagage;
    }

    public double getConsoCarburant() {
        return consoCarburant;
    }

    public void setConsoCarburant(double consoCarburant) {
        this.consoCarburant = consoCarburant;
    }

    public double getTarifBagage() {
        return tarifBagage;
    }

    public void setTarifBagage(double tarifBagage) {
        this.tarifBagage = tarifBagage;
    }
}
