<%@ page contentType="text/html; charset=UTF-8" %>

<div class="sidebar">
    <h2>🚐 Taxi Brousse</h2>

    <ul>
        <li><a href="<%= request.getContextPath() %>/">🏠 Accueil</a></li>
        
        <li style="margin-top: 20px; color: #95a5a6; font-size: 12px; text-transform: uppercase; padding-left: 10px;">Gestion</li>
        <li><a href="<%= request.getContextPath() %>/personnes">👥 Personnel</a></li>
        <li><a href="<%= request.getContextPath() %>/type-voitures">🚗 Types de voitures</a></li>
        <li><a href="<%= request.getContextPath() %>/taxis">🚐 Taxis</a></li>
        <li><a href="<%= request.getContextPath() %>/trajets">🛣️ Trajets</a></li>
        
        <li style="margin-top: 20px; color: #95a5a6; font-size: 12px; text-transform: uppercase; padding-left: 10px;">Voyages</li>
        <li><a href="<%= request.getContextPath() %>/taxi-trajets">📅 Liste des voyages</a></li>
        <li><a href="<%= request.getContextPath() %>/taxi-trajets/crud?action=form">➕ Planifier un voyage</a></li>
        
        <li style="margin-top: 20px; color: #95a5a6; font-size: 12px; text-transform: uppercase; padding-left: 10px;">Réservations</li>
        <li><a href="<%= request.getContextPath() %>/reservation/search">🔍 Rechercher & Réserver</a></li>
        <li><a href="<%= request.getContextPath() %>/reservation/list">📋 Mes réservations</a></li>
        
        <li style="margin-top: 20px; color: #95a5a6; font-size: 12px; text-transform: uppercase; padding-left: 10px;">Finance</li>
        <li><a href="<%= request.getContextPath() %>/paiement">💰 Paiements</a></li>

        <li><a href="<%= request.getContextPath() %>/diff">▶️ Diffusions Pubs</a></li>
    </ul>
</div>

<style>
.sidebar ul {
    list-style: none;
    padding: 0;
    margin: 0;
}

.sidebar ul li a {
    display: block;
    padding: 10px 15px;
    color: #ecf0f1;
    text-decoration: none;
    border-radius: 5px;
    transition: all 0.3s;
}

.sidebar ul li a:hover {
    background: rgba(255, 255, 255, 0.1);
    padding-left: 20px;
}
</style>