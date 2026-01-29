package mg.coop.model;

public class FactureLigne {
    private int taxiTrajetId;
    private double coutPub;
    private int nbDiffusions;
    private double total;
    private double dejaPaye;
    private double reste;

    public double getTotal() {
        return coutPub * nbDiffusions;
    }

    // getters & setters
}
