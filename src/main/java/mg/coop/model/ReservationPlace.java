package mg.coop.model;

public class ReservationPlace {

    private int id;
    private int taxiTrajetId;
    private int reservationId;
    private int numeroPlace;

    public ReservationPlace() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxiTrajetId() {
        return taxiTrajetId;
    }

    public void setTaxiTrajetId(int taxiTrajetId) {
        this.taxiTrajetId = taxiTrajetId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getNumeroPlace() {
        return numeroPlace;
    }

    public void setNumeroPlace(int numeroPlace) {
        this.numeroPlace = numeroPlace;
    }
}
