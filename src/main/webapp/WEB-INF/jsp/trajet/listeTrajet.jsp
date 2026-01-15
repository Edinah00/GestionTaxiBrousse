<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, mg.coop.model.Trajet" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Trajets</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
    <style>
        
    </style>
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <div class="header-section">
            <h1>🛣️ Gestion des Trajets</h1>
            <a href="<%= request.getContextPath() %>/trajet?action=add" class="btn-add">
                ➕ Ajouter un trajet
            </a>
        </div>

        <% if (request.getParameter("erreur") != null) { %>
            <div class="alert alert-error">
                ⚠️ Impossible de supprimer ce trajet : il est utilisé par des taxi-trajets
            </div>
        <% } %>

        <!-- SECTION RECHERCHE -->
        <div class="search-section">
            <h3 style="margin-top: 0;">🔍 Rechercher un trajet</h3>
            <form method="get" action="<%= request.getContextPath() %>/trajet" class="search-form">
                <input type="hidden" name="action" value="liste">
                
                <div class="form-group">
                    <label for="depart">📍 Lieu de départ</label>
                    <select id="depart" name="depart">
                        <option value="">-- Tous --</option>
                        <%
                            List<String> departs = (List<String>) request.getAttribute("departs");
                            String selectedDepart = request.getParameter("depart");
                            if (departs != null) {
                                for (String d : departs) {
                        %>
                            <option value="<%= d %>" <%= (selectedDepart != null && selectedDepart.equals(d)) ? "selected" : "" %>>
                                <%= d %>
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="arrivee">🎯 Lieu d'arrivée</label>
                    <select id="arrivee" name="arrivee">
                        <option value="">-- Tous --</option>
                        <%
                            List<String> arrivees = (List<String>) request.getAttribute("arrivees");
                            String selectedArrivee = request.getParameter("arrivee");
                            if (arrivees != null) {
                                for (String a : arrivees) {
                        %>
                            <option value="<%= a %>" <%= (selectedArrivee != null && selectedArrivee.equals(a)) ? "selected" : "" %>>
                                <%= a %>
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <button type="submit" class="btn-search">Rechercher</button>
            </form>
        </div>

        <!-- TABLE DES TRAJETS -->
        <div class="trajets-table">
            <%
                List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
                if (trajets != null && !trajets.isEmpty()) {
            %>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Trajet</th>
                            <th>Distance</th>
                            <th>Prix de base</th>
                            <th>Augmentation</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Trajet t : trajets) {
                        %>
                            <tr>
                                <td>#<%= t.getId() %></td>
                                <td>
                                    <span class="route">
                                        📍 <%= t.getDepart() %> → <%= t.getArrivee() %>
                                    </span>
                                </td>
                                <td><%= t.getDistanceKm() %> km</td>
                                <td><%= String.format("%.0f", t.getPrixBase()) %> Ar</td>
                                <td><%= String.format("%.0f", t.getPourcentageAugmentation()) %>%</td>
                                <td>
                                    <div class="actions">
                                       
                                        <a href="<%= request.getContextPath() %>/trajet?action=edit&id=<%= t.getId() %>" 
                                           class="btn-edit">
                                            ✏️ Modifier
                                        </a>
                                        <a href="<%= request.getContextPath() %>/trajet?action=delete&id=<%= t.getId() %>" 
                                           class="btn-delete"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce trajet ?')">
                                            🗑️ Supprimer
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            <%
                } else {
            %>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h2>Aucun trajet trouvé</h2>
                    <p>Il n'y a pas de trajets correspondant à vos critères de recherche.</p>
                </div>
            <%
                }
            %>
        </div>
    </div>
</div>

</body>
</html>

    <style>
     
    </style>
</head>
<body>
