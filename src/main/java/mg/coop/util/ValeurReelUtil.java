package mg.coop.util;

import mg.coop.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe utilitaire pour calculer la valeur réelle d'un taxi-trajet
 */
public class ValeurReelUtil {

    /**
     * Classe interne pour stocker les informations de ventes par catégorie
     */
    public static class VentesParCategorie {
        private int nbPlacesVip;
        private int nbPlacesPremium;
        private int nbPlacesStandard;
        private int nbEnfants;
        private double prixVip;
        private double prixPremium;
        private double prixStandard;
        private double prixEnfant;

        // Getters et Setters
        public int getNbPlacesVip() { return nbPlacesVip; }
        public void setNbPlacesVip(int nbPlacesVip) { this.nbPlacesVip = nbPlacesVip; }

        public int getNbPlacesPremium() { return nbPlacesPremium; }
        public void setNbPlacesPremium(int nbPlacesPremium) { this.nbPlacesPremium = nbPlacesPremium; }

        public int getNbPlacesStandard() { return nbPlacesStandard; }
        public void setNbPlacesStandard(int nbPlacesStandard) { this.nbPlacesStandard = nbPlacesStandard; }

        public int getNbEnfants() { return nbEnfants; }
        public void setNbEnfants(int nbEnfants) { this.nbEnfants = nbEnfants; }

        public double getPrixVip() { return prixVip; }
        public void setPrixVip(double prixVip) { this.prixVip = prixVip; }

        public double getPrixPremium() { return prixPremium; }
        public void setPrixPremium(double prixPremium) { this.prixPremium = prixPremium; }

        public double getPrixStandard() { return prixStandard; }
        public void setPrixStandard(double prixStandard) { this.prixStandard = prixStandard; }

        public double getPrixEnfant() { return prixEnfant; }
        public void setPrixEnfant(double prixEnfant) { this.prixEnfant = prixEnfant; }

        /**
         * Calcule la valeur réelle totale
         */
        public double getValeurReelle() {
            double valeursVip = nbPlacesVip * prixVip;
            double valeursPremium = nbPlacesPremium * prixPremium;
            double valeursStandardAdultes = (nbPlacesStandard - nbEnfants) * prixStandard;
            double valeursEnfants = nbEnfants * prixEnfant;
            
            return valeursVip + valeursPremium + valeursStandardAdultes + valeursEnfants;
        }

        @Override
        public String toString() {
            return String.format(
                "VIP: %d × %.0f Ar = %.0f Ar\n" +
                "Premium: %d × %.0f Ar = %.0f Ar\n" +
                "Standard adultes: %d × %.0f Ar = %.0f Ar\n" +
                "Enfants: %d × %.0f Ar = %.0f Ar\n" +
                "TOTAL: %.0f Ar",
                nbPlacesVip, prixVip, nbPlacesVip * prixVip,
                nbPlacesPremium, prixPremium, nbPlacesPremium * prixPremium,
                (nbPlacesStandard - nbEnfants), prixStandard, (nbPlacesStandard - nbEnfants) * prixStandard,
                nbEnfants, prixEnfant, nbEnfants * prixEnfant,
                getValeurReelle()
            );
        }
    }

    /**
     * Récupère les prix du trajet
     */
    public static double[] getPrixTrajet(int trajetId) throws Exception {
        String sql = """
            SELECT 
                COALESCE(prix_base, 0) as prix_standard,
                COALESCE(prix_premium, 0) as prix_premium,
                COALESCE(prix_vip, 0) as prix_vip,
                COALESCE(prix_enfant, 0) as prix_enfant
            FROM trajet
            WHERE id = ?
        """;

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, trajetId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new double[] {
                    rs.getDouble("prix_standard"),
                    rs.getDouble("prix_premium"),
                    rs.getDouble("prix_vip"),
                    rs.getDouble("prix_enfant")
                };
            }
        }
        return new double[] {0.0, 0.0, 0.0, 0.0};
    }

    /**
     * Récupère le nombre de places vendues par catégorie pour un taxi-trajet
     */
    public static int[] getPlacesVenduesParCategorie(int taxiTrajetId) throws Exception {
        String sql = """
          SELECT
    COALESCE(SUM(CASE WHEN rp.type_place = 'VIP' THEN 1 ELSE 0 END), 0) AS nb_vip,
    COALESCE(SUM(CASE WHEN rp.type_place = 'PREMIUM' THEN 1 ELSE 0 END), 0) AS nb_premium,
    COALESCE(SUM(CASE WHEN rp.type_place = 'STANDARD' THEN 1 ELSE 0 END), 0) AS nb_standard,
    COALESCE((
        SELECT SUM(r.nb_enfants)
        FROM reservation r
        WHERE r.taxi_trajet_id = 1
    ), 0) AS nb_enfants
FROM reservation_place rp
WHERE rp.taxi_trajet_id = ?;

""";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new int[] {
                    rs.getInt("nb_vip"),
                    rs.getInt("nb_premium"),
                    rs.getInt("nb_standard"),
                    rs.getInt("nb_enfants")
                };
            }
        }
        return new int[] {0, 0, 0, 0};
    }

    /**
     * Récupère toutes les informations de ventes pour un taxi-trajet
     */
    public static VentesParCategorie getVentesParCategorie(int taxiTrajetId) throws Exception {
        VentesParCategorie ventes = new VentesParCategorie();

        // Récupérer le trajet_id
        String sqlTrajet = "SELECT trajet_id FROM taxi_trajet WHERE id = ?";
        int trajetId = 0;

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlTrajet)) {
            ps.setInt(1, taxiTrajetId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                trajetId = rs.getInt("trajet_id");
            }
        }

        if (trajetId == 0) {
            throw new Exception("Taxi-trajet non trouvé avec l'ID: " + taxiTrajetId);
        }

        // Récupérer les prix
        double[] prix = getPrixTrajet(trajetId);
        ventes.setPrixStandard(prix[0]);
        ventes.setPrixPremium(prix[1]);
        ventes.setPrixVip(prix[2]);
        ventes.setPrixEnfant(prix[3]);

        // Récupérer les places vendues
        int[] placesVendues = getPlacesVenduesParCategorie(taxiTrajetId);
        ventes.setNbPlacesVip(placesVendues[0]);
        ventes.setNbPlacesPremium(placesVendues[1]);
        ventes.setNbPlacesStandard(placesVendues[2]);
        ventes.setNbEnfants(placesVendues[3]);

        return ventes;
    }

    /**
     * Calcule la valeur réelle d'un taxi-trajet
     */
    public static double getValeurReel(int taxiTrajetId) throws Exception {
        VentesParCategorie ventes = getVentesParCategorie(taxiTrajetId);
        return ventes.getValeurReelle();
    }

    /**
     * Méthode pour afficher un rapport détaillé
     */
    public static String getRapportDetaille(int taxiTrajetId) throws Exception {
        VentesParCategorie ventes = getVentesParCategorie(taxiTrajetId);
        return ventes.toString();
    }

    public static void main(String[] args) {
        try {
            int taxiTrajetId = 1; // Exemple d'ID
            String rapport = getRapportDetaille(taxiTrajetId);
            System.out.println(rapport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}