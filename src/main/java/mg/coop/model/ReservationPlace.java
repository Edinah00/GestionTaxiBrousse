package mg.coop.model;

public class ReservationPlace {
    private int id;

    private Reservation reservation;

    private int numeroPlace;

    private PlacePassager pp;

    public ReservationPlace() {
    }

    public ReservationPlace(int id, Reservation reservation, int numeroPlace, PlacePassager pp) {
        this.id = id;
        this.reservation = reservation;
        this.numeroPlace = numeroPlace;
        this.pp = pp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public int getNumeroPlace() {
        return numeroPlace;
    }

    public void setNumeroPlace(int numeroPlace) {
        this.numeroPlace = numeroPlace;
    }

    public PlacePassager getPlacePassager() {
        return pp;
    }

    public void setPlacePassager(PlacePassager pp) {
        this.pp = pp;
    }
}

