package mg.coop.model;

public class Diffusion {

    private int id;
    private int idPub;
    private int idTaxiTrajet;
    private int nbDiffusions;
    private int totalDiffusions;
    private double CA;
    public Diffusion() {
    }

    public Diffusion(int idPub, int idTaxiTrajet) {
        this.idPub = idPub;
        this.idTaxiTrajet = idTaxiTrajet;
    }

    public Diffusion(int id, int idPub, int idTaxiTrajet) {
        this.id = id;
        this.idPub = idPub;
        this.idTaxiTrajet = idTaxiTrajet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPub() {
        return idPub;
    }

    public void setIdPub(int idPub) {
        this.idPub = idPub;
    }

    public int getIdTaxiTrajet() {
        return idTaxiTrajet;
    }

    public void setIdTaxiTrajet(int idTaxiTrajet) {
        this.idTaxiTrajet = idTaxiTrajet;
    }
    public int getNbDiffusions() {
        return nbDiffusions;
    }
    public void setNbDiffusions(int nbDiffusions) {
        this.nbDiffusions = nbDiffusions;
    }
    public int getTotalDiffusions() {
        return totalDiffusions;
    }
    public void setTotalDiffusions(int totalDiffusions) {
        this.totalDiffusions = totalDiffusions; 
    }
    public double getCA() {
        return CA;
    }
    public void setCA(double cA) {
        CA = cA;}

}