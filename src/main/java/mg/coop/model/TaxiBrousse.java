package mg.coop.model;

public class TaxiBrousse {

    private int id;
    private int cooperativeId;
    private int typeVoitureId;
    private String immatriculation;

    public TaxiBrousse() {}

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

    public int getTypeVoitureId() {
        return typeVoitureId;
    }

    public void setTypeVoitureId(int typeVoitureId) {
        this.typeVoitureId = typeVoitureId;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
}
