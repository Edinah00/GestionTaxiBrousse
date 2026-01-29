package mg.coop.dao;

import mg.coop.config.DatabaseConfig;
import mg.coop.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TaxiTrajetDAO {

    private Connection conn;

    public TaxiTrajetDAO(Connection conn) {
        this.conn = conn;
    }

    // 1️⃣ Liste des départs distincts
    public List<String> findAllDepart() throws SQLException {
        List<String> departList = new ArrayList<>();
        String sql = "SELECT DISTINCT depart FROM trajet ORDER BY depart";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departList.add(rs.getString("depart"));
            }
        }
        return departList;
    }

    // 2️⃣ Liste des arrivées distinctes
    public List<String> findAllArrivee() throws SQLException {
        List<String> arriveeList = new ArrayList<>();
        String sql = "SELECT DISTINCT arrivee FROM trajet ORDER BY arrivee";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                arriveeList.add(rs.getString("arrivee"));
            }
        }
        return arriveeList;
    }

    // 3️⃣ Rechercher taxi disponibles pour un trajet et une date
    public List<Map<String, Object>> findTaxisDisponibles(String depart,
                                                      String arrivee,
                                                      LocalDate date) throws SQLException {

        List<Map<String, Object>> result = new ArrayList<>();

        String sql = "SELECT tt.id AS taxi_trajet_id, tb.id AS taxi_id, tb.immatriculation, " +
                    "tv.libelle, tv.nbr_places, tt.date_heure_depart " +
                    "FROM taxi_trajet tt " +
                    "JOIN taxi_brousse tb ON tt.taxi_id = tb.id " +
                    "JOIN type_voiture tv ON tb.type_voiture_id = tv.id " +
                    "JOIN trajet t ON tt.trajet_id = t.id " +
                    "WHERE t.depart = ? AND t.arrivee = ? AND DATE(tt.date_heure_depart) = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, depart);
            ps.setString(2, arrivee);
            ps.setDate(3, java.sql.Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int taxiTrajetId = rs.getInt("taxi_trajet_id");
                    int nbrPlaces = rs.getInt("nbr_places");

                    int placesReservees = getNombrePlacesReservees(taxiTrajetId);
                    List<Integer> placesLibres =
                            getNumeroPlacesLibres(taxiTrajetId, nbrPlaces);

                    Map<String, Object> map = new HashMap<>();
                    map.put("taxiTrajetId", taxiTrajetId);
                    map.put("immatriculation", rs.getString("immatriculation"));
                    map.put("typeVoiture", rs.getString("libelle"));
                    map.put("placesDisponibles", nbrPlaces - placesReservees);
                    map.put("placesLibres", placesLibres);
                    map.put("dateHeureDepart",
                            rs.getTimestamp("date_heure_depart").toLocalDateTime());

                    result.add(map);
                }
            }
        }

        return result;
    }

    // Nombre de places réservées pour un taxi_trajet
    private int getNombrePlacesReservees(int taxiTrajetId) throws SQLException {

        String sql = """
            SELECT COUNT(rp.id) AS cnt
            FROM reservation_place rp
            JOIN reservation r ON rp.reservation_id = r.id
            WHERE r.taxi_trajet_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        }
        return 0;
    }

    // Numéros de places libres
    public List<Integer> getNumeroPlacesLibres(int taxiTrajetId, int nbrPlaces) throws SQLException {

        List<Integer> libres = new ArrayList<>();
        Set<Integer> prises = new HashSet<>();

        String sql = """
            SELECT rp.numero_place
            FROM reservation_place rp
            JOIN reservation r ON rp.reservation_id = r.id
            WHERE r.taxi_trajet_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taxiTrajetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prises.add(rs.getInt("numero_place"));
                }
            }
        }

        for (int i = 1; i <= nbrPlaces; i++) {
            if (!prises.contains(i)) {
                libres.add(i);
            }
        }

        return libres;
    }

    public  TaxiTrajet findById(int id) throws SQLException {
        String sql = "SELECT tt.id AS tt_id, tt.date_heure_depart, " +
             "t.id AS taxi_id, t.immatriculation, t.type_voiture_id, " +
             "tv.id AS type_id, tv.libelle, tv.nbr_places " +
             "FROM taxi_trajet tt " +
             "JOIN taxi_brousse t ON tt.taxi_id = t.id " +
             "JOIN type_voiture tv ON t.type_voiture_id = tv.id " +
             "WHERE tt.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // TypeVoiture
                    TypeVoiture type = new TypeVoiture();
                    type.setId(rs.getInt("type_id"));
                    type.setLibelle(rs.getString("libelle"));
                    type.setNbrPlaces(rs.getInt("nbr_places"));

                    // TaxiBrousse
                    TaxiBrousse taxi = new TaxiBrousse();
                    taxi.setId(rs.getInt("taxi_id"));
                    taxi.setImmatriculation(rs.getString("immatriculation"));
                    taxi.setTypeVoiture(type);

                    // TaxiTrajet
                    TaxiTrajet tt = new TaxiTrajet();
                    tt.setId(rs.getInt("tt_id"));
                    tt.setTaxi(taxi);
                    tt.setDateHeureDepart(rs.getTimestamp("date_heure_depart").toLocalDateTime());

                    return tt;
                }
            }
        }
        return null;
    }

    public List<TaxiTrajet> findAll() throws SQLException {
        List<TaxiTrajet> list = new ArrayList<>();
        String sql = "SELECT tt.id AS tt_id, " +
            "tt.date_heure_depart, " +
            "tr.depart AS depart, " +
            "tr.arrivee AS arrivee, " +
            "t.id AS taxi_id, " +
            "t.immatriculation, " +
            "tt.chauffeur_id AS ch_id, " +
            "p.nom AS ch_nom, " +
            "t.cooperative_id AS cooperative_id, " +
            "c.nom AS cooperative_nom, " +
            "t.type_voiture_id AS tv_id, " +
            "tv.libelle AS libelle, " +
            "tv.nbr_places AS nbr_places " +
        "FROM taxi_trajet tt " +
        "JOIN trajet tr ON tt.trajet_id = tr.id " +
        "JOIN taxi_brousse t ON tt.taxi_id = t.id " +
        "JOIN cooperative c ON t.cooperative_id = c.id " +
        "JOIN personne p ON tt.chauffeur_id = p.id " +
        "JOIN type_voiture tv ON t.type_voiture_id = tv.id";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Trajet trajet = new Trajet(); 
                trajet.setDepart(rs.getString("depart"));
                trajet.setArrivee(rs.getString("arrivee"));

                TaxiTrajet tt = new TaxiTrajet();
                tt.setId(rs.getInt("tt_id"));
                tt.setDateHeureDepart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
                tt.setTrajet(trajet);

                TaxiBrousse taxi = new TaxiBrousse();
                taxi.setId(rs.getInt("taxi_id"));
                taxi.setImmatriculation(rs.getString("immatriculation"));

                TypeVoiture tv = new TypeVoiture();
                tv.setId(rs.getInt("tv_id"));
                tv.setLibelle(rs.getString("libelle"));
                tv.setNbrPlaces(rs.getInt("nbr_places"));
                taxi.setTypeVoiture(tv);

                Cooperative coop = new Cooperative();
                coop.setId(rs.getInt("cooperative_id"));
                coop.setNom(rs.getString("cooperative_nom"));
                taxi.setCooperative(coop);

                tt.setTaxi(taxi);

                Personne chauffeur = new Personne();
                chauffeur.setId(rs.getInt("ch_id"));
                chauffeur.setNom(rs.getString("ch_nom"));
                tt.setChauffeur(chauffeur);

                list.add(tt);
            }
        }

        return list;
    }

    public void create(TaxiTrajet tt) throws SQLException {
        String sql = """
            INSERT INTO taxi_trajet
            (taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart)
            VALUES (?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tt.getTaxi().getId());
            ps.setInt(2, tt.getTrajet().getId());
            ps.setInt(3, tt.getChauffeur().getId());

            if (tt.getAideChauffeur() != null)
                ps.setInt(4, tt.getAideChauffeur().getId());
            else
                ps.setNull(4, Types.INTEGER);

            ps.setTimestamp(5, Timestamp.valueOf(tt.getDateHeureDepart()));
            ps.executeUpdate();
        }
    }

    /* ================= UPDATE ================= */
    public void update(TaxiTrajet tt) throws SQLException {
        String sql = """
            UPDATE taxi_trajet SET
            taxi_id=?, trajet_id=?, chauffeur_id=?, aide_chauffeur_id=?, date_heure_depart=?
            WHERE id=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tt.getTaxi().getId());
            ps.setInt(2, tt.getTrajet().getId());
            ps.setInt(3, tt.getChauffeur().getId());

            if (tt.getAideChauffeur() != null)
                ps.setInt(4, tt.getAideChauffeur().getId());
            else
                ps.setNull(4, Types.INTEGER);

            ps.setTimestamp(5, Timestamp.valueOf(tt.getDateHeureDepart()));
            ps.setInt(6, tt.getId());
            ps.executeUpdate();
        }
    }

    /* ================= DELETE ================= */
    public void delete(int id) throws SQLException {
        try (PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM taxi_trajet WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* ================= SEARCH ================= */
    public List<TaxiTrajet> search(Integer taxiId, Integer trajetId, LocalDate date)
            throws Exception {

        List<TaxiTrajet> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT tt.*, tb.immatriculation,
                   tr.depart, tr.arrivee,
                   p1.nom chauffeur_nom,
                   p2.nom aide_nom
            FROM taxi_trajet tt
            JOIN taxi_brousse tb ON tb.id = tt.taxi_id
            JOIN trajet tr ON tr.id = tt.trajet_id
            JOIN personne p1 ON p1.id = tt.chauffeur_id
            LEFT JOIN personne p2 ON p2.id = tt.aide_chauffeur_id
            WHERE 1=1
        """);

        if (taxiId != null) sql.append(" AND tt.taxi_id=?");
        if (trajetId != null) sql.append(" AND tt.trajet_id=?");
        if (date != null) sql.append(" AND DATE(tt.date_heure_depart)=?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (taxiId != null) ps.setInt(i++, taxiId);
            if (trajetId != null) ps.setInt(i++, trajetId);
            if (date != null) ps.setDate(i++, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private TaxiTrajet map(ResultSet rs) throws Exception {
        TaxiTrajet tt = new TaxiTrajet();
        tt.setId(rs.getInt("id"));

        TaxiBrousse tb = new TaxiBrousse();
        tb.setId(rs.getInt("taxi_id"));
        tb.setImmatriculation(rs.getString("immatriculation"));
        tt.setTaxi(tb);

        Trajet tr = new Trajet();
        tr.setId(rs.getInt("trajet_id"));
        tr.setDepart(rs.getString("depart"));
        tr.setArrivee(rs.getString("arrivee"));
        tt.setTrajet(tr);

        Personne ch = new Personne();
        ch.setId(rs.getInt("chauffeur_id"));
        ch.setNom(rs.getString("chauffeur_nom"));
        tt.setChauffeur(ch);

        if (rs.getInt("aide_chauffeur_id") != 0) {
            Personne a = new Personne();
            a.setId(rs.getInt("aide_chauffeur_id"));
            a.setNom(rs.getString("aide_nom"));
            tt.setAideChauffeur(a);
        }

        tt.setDateHeureDepart(
                rs.getTimestamp("date_heure_depart").toLocalDateTime());

        double valMax = this.calculValMax(tb.getId());
        tt.setValeurMax(valMax);

        double ca = calculCA(tt.getId());
        tt.setCA(ca);

        return tt;
    }

    public double calculValMax(int idVoiture) throws SQLException {
        double valMax = 0;

        String sql = """
            SELECT COALESCE(SUM(cp.nbr * cat.prix), 0) AS valeur_max
            FROM config_place cp
            JOIN categorie_place cat ON cp.id_categ_place = cat.id
            WHERE cp.id_taxi_brousse = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVoiture);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    valMax = rs.getDouble("valeur_max");
                }
            }
        }

        return valMax;
    }

    public static double calculCA(int taxiTrajetId) throws Exception {
        double ca = 0;

        String sql = """
            SELECT SUM(pp.prix) AS total_ca
            FROM reservation r
            JOIN reservation_place rp ON r.id = rp.reservation_id
            JOIN place_passager pp ON rp.id_place_passager = pp.id
            WHERE r.taxi_trajet_id = ? AND r.etat_paye = TRUE
        """;

        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ca = rs.getDouble("total_ca");
            }
        }

        return ca;
    }
    /**
 * Récupère tous les trajets d'un mois et année donnés
 */
}



