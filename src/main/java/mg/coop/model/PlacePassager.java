package mg.coop.model;

public class PlacePassager {
    private int id;
    private CategoriePlace cp;
    private TypePassager tp;
    private double prix;
    
    public PlacePassager() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public CategoriePlace getCategoriePlace() {
        return cp;
    }
    public void setCategoriePlace(CategoriePlace cp) {
        this.cp = cp;
    }
    public TypePassager getTypePassager() {
        return tp;
    }
    public void setTypePassager(TypePassager tp) {
        this.tp = tp;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
}
