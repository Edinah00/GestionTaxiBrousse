package mg.coop.dao;


import mg.coop.model.Societe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocieteDAO {

    private Connection connection;

    public SocieteDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Societe> getAllSocietes() {

        List<Societe> societes = new ArrayList<>();
        String sql = "SELECT id, nom FROM societe ORDER BY nom";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Societe societe = new Societe();
                societe.setId(rs.getInt("id"));
                societe.setNom(rs.getString("nom"));
                societes.add(societe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return societes;
    }

    public Societe findById(int id) {
        Societe societe = null;
        String sql = "SELECT id, nom FROM societe WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                societe = new Societe();
                societe.setId(rs.getInt("id"));
                societe.setNom(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return societe;
    }
}

