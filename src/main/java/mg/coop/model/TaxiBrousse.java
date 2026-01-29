package mg.coop.model;

public class TaxiBrousse {
    private int id;
    private String immatriculation;

    private Cooperative cooperative;
    private TypeVoiture typeVoiture;
    public TaxiBrousse() {
    }
    public TaxiBrousse(int id, String immatriculation, Cooperative cooperative, TypeVoiture typeVoiture) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.cooperative = cooperative;
        this.typeVoiture = typeVoiture;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImmatriculation() {
        return immatriculation;
    }
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
    public Cooperative getCooperative() {
        return cooperative;
    }
    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }
    public TypeVoiture getTypeVoiture() {
        return typeVoiture;
    }
    public void setTypeVoiture(TypeVoiture typeVoiture) {
        this.typeVoiture = typeVoiture;
    }

    // getters / setters
}
