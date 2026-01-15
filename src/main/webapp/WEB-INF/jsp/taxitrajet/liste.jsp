<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Taxi-Trajets</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; margin: 0; padding: 20px; }
        h1 { color: #2c3e50; }
        a.btn { padding: 8px 12px; border-radius: 4px; text-decoration: none; margin: 5px; }
        .btn-primary { background: #3498db; color: #fff; }
        .btn-warning { background: #f39c12; color: #fff; }
        .btn-danger { background: #e74c3c; color: #fff; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; background: #fff; }
        th, td { padding: 10px; border-bottom: 1px solid #ddd; }
        th { background: #3498db; color: #fff; }
        tr:hover { background: #f1f1f1; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <h1>Liste des Taxi-Trajets</h1>
    <a href="<%= request.getContextPath() %>/taxitrajet?action=add" class="btn btn-primary">➕ Nouveau Taxi-Trajet</a>

    <table>
        <tr>
            <th>Taxi</th><th>Trajet</th><th>Chauffeur</th><th>Date départ</th><th>Actions</th>
        </tr>
        <%
            List<mg.coop.model.TaxiTrajet> liste = (List<mg.coop.model.TaxiTrajet>) request.getAttribute("taxitrajets");
            if (liste != null) {
                for (mg.coop.model.TaxiTrajet tt : liste) {
        %>
        <tr>
            <td><%= tt.getImmatriculation() %></td>
            <td><%= tt.getDepart() %> → <%= tt.getArrivee() %></td>
            <td><%= tt.getNomChauffeur() %></td>
            <td><%= tt.getDateHeureDepart() %></td>
            <td>
                <a href="<%= request.getContextPath() %>/taxitrajet?action=edit&id=<%= tt.getId() %>" class="btn btn-warning">✏️ Modifier</a>
                <a href="<%= request.getContextPath() %>/taxitrajet?action=delete&id=<%= tt.getId() %>" class="btn btn-danger">🗑️ Supprimer</a>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
