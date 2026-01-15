<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, mg.coop.model.TaxiTrajet" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Recherche Taxi-Brousse</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
</head>
<body>



<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />
<div class="content">

    <h1>🚐 Réservation Taxi-Brousse</h1>
    <p>Trouvez et réservez votre place facilement</p>

    <% if (request.getAttribute("erreur") != null) { %>
        <div class="erreur"><%= request.getAttribute("erreur") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/rechercheVoiture" method="get">
        <label for="depart">📍 Lieu de départ</label>
        <select id="depart" name="depart" required>
            <option value="">Sélectionnez...</option>
            <% 
                List<String> lieuxDepart = (List<String>) request.getAttribute("lieuxDepart");
                String selectedDepart = request.getParameter("depart");
                for(String lieu : lieuxDepart) {
            %>
                <option value="<%= lieu %>" <%= lieu.equals(selectedDepart) ? "selected" : "" %> ><%= lieu %></option>
            <% } %>
        </select>

        <label for="arrivee">🎯 Lieu d'arrivée</label>
        <select id="arrivee" name="arrivee" required>
            <option value="">Sélectionnez...</option>
            <%
                List<String> lieuxArrivee = (List<String>) request.getAttribute("lieuxArrivee");
                String selectedArrivee = request.getParameter("arrivee");
                for(String lieu : lieuxArrivee) {
            %>
                <option value="<%= lieu %>" <%= lieu.equals(selectedArrivee) ? "selected" : "" %> ><%= lieu %></option>
            <% } %>
        </select>

        <label for="dateDepart">📅 Date de départ</label>
        <input type="date" id="dateDepart" name="dateDepart" value="<%= request.getParameter("dateDepart") %>" required>

        <label for="heureDepart">🕐 Heure de départ (minimum)</label>
        <input type="time" id="heureDepart" name="heureDepart" value="<%= request.getParameter("heureDepart") %>">

        <button type="submit">🔍 Rechercher</button>
    </form>

    <% 
        Boolean recherche = (Boolean) request.getAttribute("recherche");
        if (recherche != null && recherche) {
            List<TaxiTrajet> trajets = (List<TaxiTrajet>) request.getAttribute("trajets");
    %>
        <h2>Résultats : <%= trajets.size() %> voiture(s) trouvée(s)</h2>
        <% if (trajets.isEmpty()) { %>
            <p>Aucune voiture disponible pour ce trajet</p>
        <% } else { 
            for (TaxiTrajet t : trajets) { %>
                <div class="voiture-card">
                    <strong><%= t.getTypeVoiture() %></strong> - <%= t.getImmatriculation() %><br>
                    Coopérative : <%= t.getCooperative() %><br>
                    Chauffeur : <%= t.getNomChauffeur() %><br>
                    Départ : <%= t.getDateHeureDepart() %><br>
                    Distance : <%= t.getDistanceKm() %> km<br>
                    Capacité : <%= t.getNbrPlaces() %> places<br>
                    Prix : <%= t.getPrixBase() %> Ar<br>
                    <% int dispo = t.getPlacesDisponibles();
                       if (dispo == 0) { %>
                           Complet
                    <% } else { %>
                           <a href="<%= request.getContextPath() %>/reservation?taxiTrajetId=<%= t.getId() %>">Réserver</a> (<%= dispo %> places restantes)
                    <% } %>
                </div>
        <%   } 
           } 
        } %>
        </div>

</div>
</body>
</html>
