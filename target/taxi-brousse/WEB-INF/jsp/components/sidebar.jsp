<%@ page contentType="text/html; charset=UTF-8" %>

<div class="sidebar">
    <h2>🚐 Taxi Brousse</h2>

    <ul>
        <li><a href="<%= request.getContextPath() %>/">Accueil</a></li>
        <li><a href="<%= request.getContextPath() %>/taxi">Taxis</a></li>
        <li><a href="<%= request.getContextPath() %>/trajet">Trajets</a></li>
        <li><a href="<%= request.getContextPath() %>/reservation">Réservations</a></li>
        <li><a href="<%= request.getContextPath() %>/depense">Dépenses</a></li>
    </ul>
</div>
