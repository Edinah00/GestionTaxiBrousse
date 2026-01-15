<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat, mg.coop.model.Reservation" %>

<%
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    SimpleDateFormat localDateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>


<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Réservations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <style>
        .liste-container {
            max-width: 1400px;
            margin: 30px auto;
            padding: 20px;
        }

        .header-section {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header-section h1 {
            margin: 0;
            color: #2c3e50;
        }

        .stats {
            display: flex;
            gap: 20px;
        }

        .stat-item {
            text-align: center;
            padding: 10px 20px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .stat-item h3 {
            margin: 0;
            color: #1abc9c;
            font-size: 24px;
        }

        .stat-item p {
            margin: 5px 0 0 0;
            color: #7f8c8d;
            font-size: 14px;
        }

        .reservations-grid {
            display: grid;
            gap: 20px;
        }

        .reservation-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 20px;
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .reservation-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        }

        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 2px solid #ecf0f1;
        }

        .reservation-id {
            font-size: 18px;
            font-weight: bold;
            color: #2c3e50;
        }

        .statut-badge {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .statut-reservation {
            background: #3498db;
            color: white;
        }

        .statut-confirme {
            background: #1abc9c;
            color: white;
        }

        .statut-annule {
            background: #e74c3c;
            color: white;
        }

        .card-body {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .info-section {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .info-row {
            display: flex;
            align-items: start;
            gap: 10px;
        }

        .info-label {
            font-weight: bold;
            color: #7f8c8d;
            min-width: 100px;
        }

        .info-value {
            color: #2c3e50;
        }

        .trajet-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            grid-column: 1 / -1;
        }

        .trajet-route {
            font-size: 18px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .places-list {
            display: inline-flex;
            gap: 5px;
            flex-wrap: wrap;
        }

        .place-badge {
            background: #1abc9c;
            color: white;
            padding: 3px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }

        .paiement-section {
            background: #e8f8f5;
            padding: 15px;
            border-radius: 8px;
            grid-column: 1 / -1;
            margin-top: 10px;
        }

        .paiement-row {
            display: flex;
            justify-content: space-between;
            margin: 5px 0;
        }

        .montant {
            font-weight: bold;
            font-size: 16px;
        }

        .montant-total {
            color: #2c3e50;
        }

        .montant-paye {
            color: #1abc9c;
        }

        .montant-restant {
            color: #e74c3c;
        }

        .empty-state {
            background: white;
            padding: 60px 20px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .empty-state-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        .erreur {
            background: #e74c3c;
            color: white;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .filters {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .filter-group {
            display: flex;
            gap: 15px;
            align-items: end;
        }

        .filter-group input,
        .filter-group select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }

        .btn-filter {
            background: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-filter:hover {
            background: #2980b9;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

<div class="liste-container">
    
    <% if (request.getAttribute("erreur") != null) { %>
        <div class="erreur"><%= request.getAttribute("erreur") %></div>
    <% } %>
    
    <%
        List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
        Map<Integer, List<Integer>> placesParReservation = (Map<Integer, List<Integer>>) request.getAttribute("placesParReservation");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        int totalReservations = reservations != null ? reservations.size() : 0;
        int totalPlaces = 0;
        double totalRevenu = 0;
        
        if (reservations != null) {
            for (Reservation r : reservations) {
                totalPlaces += r.getNbPlaces();
                totalRevenu += r.getMontantPaye();
            }
        }
    %>
    
    <div class="header-section">
        <h1>📋 Liste des Réservations</h1>
        <div class="stats">
            <div class="stat-item">
                <h3><%= totalReservations %></h3>
                <p>Réservations</p>
            </div>
            <div class="stat-item">
                <h3><%= totalPlaces %></h3>
                <p>Places réservées</p>
            </div>
            <div class="stat-item">
                <h3><%= String.format("%.0f", totalRevenu) %> Ar</h3>
                <p>Revenu total</p>
            </div>
        </div>
    </div>
    
    <% if (reservations != null && !reservations.isEmpty()) { %>
        <div class="reservations-grid">
            <% for (Reservation reservation : reservations) { 
                List<Integer> places = placesParReservation.get(reservation.getId());
                double montantTotal = reservation.getMontantTotal();
                double montantRestant = reservation.getMontantRestant();
            %>
                <div class="reservation-card">
                    <div class="card-header">
                        <div>
                            <div class="reservation-id">🎫 Réservation #<%= reservation.getId() %></div>
                            <small style="color: #7f8c8d;">
    <%= reservation.getDateReservation() != null ? dateTimeFormat.format(reservation.getDateReservation()) : "N/A" %>
</small>

                        </div>
                        <span class="statut-badge statut-<%= reservation.getStatut().toLowerCase() %>">
                            <%= reservation.getStatut() %>
                        </span>
                    </div>
                    
                    <div class="card-body">
                        <div class="info-section">
                            <div class="info-row">
                                <span class="info-label">👤 Client:</span>
                                <span class="info-value"><%= reservation.getNomClient() %></span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">📞 Téléphone:</span>
                                <span class="info-value"><%= reservation.getTelephone() %></span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">🪑 Places:</span>
                                <span class="info-value">
                                    <%= reservation.getNbPlaces() %> place(s)
                                    <div class="places-list">
                                        <% if (places != null) {
                                            for (Integer place : places) { %>
                                                <span class="place-badge"><%= place %></span>
                                        <%  }
                                        } %>
                                    </div>
                                </span>
                            </div>
                        </div>
                        
                        <div class="info-section">
                            <div class="info-row">
                                <span class="info-label">🚗 Véhicule:</span>
                                <span class="info-value">
                                    <%= reservation.getTypeVoiture() %><br>
                                    <small><%= reservation.getImmatriculation() %></small>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">👨‍✈️ Chauffeur:</span>
                                <span class="info-value"><%= reservation.getNomChauffeur() %></span>
                            </div>
                        </div>
                        
                        <div class="trajet-info">
                            <div class="trajet-route">
                                📍 <%= reservation.getDepart() %> → <%= reservation.getArrivee() %>
                            </div>
                            <div style="color: #7f8c8d;">
                                🕐 Départ: <%= dateTimeFormat.format(reservation.getDateHeureDepart()) %>
                            </div>
                        </div>
                        
                        <div class="paiement-section">
                            <div class="paiement-row">
                                <span>Mode de paiement:</span>
                                <span><strong><%= reservation.getModePaiement() != null ? reservation.getModePaiement() : "N/A" %></strong></span>
                            </div>
                            <div class="paiement-row">
                                <span>Type:</span>
                                <span><strong><%= reservation.getTypePaiement() != null ? reservation.getTypePaiement() : "N/A" %></strong></span>
                            </div>
                            <hr style="margin: 10px 0; border: none; border-top: 1px solid #bdc3c7;">
                            <div class="paiement-row">
                                <span>Montant total:</span>
                                <span class="montant montant-total"><%= String.format("%.0f", montantTotal) %> Ar</span>
                            </div>
                            <div class="paiement-row">
                                <span>Montant payé:</span>
                                <span class="montant montant-paye"><%= String.format("%.0f", reservation.getMontantPaye()) %> Ar</span>
                            </div>
                            <% if (montantRestant > 0) { %>
                            <div class="paiement-row">
                                <span>Reste à payer:</span>
                                <span class="montant montant-restant"><%= String.format("%.0f", montantRestant) %> Ar</span>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } else { %>
        <div class="empty-state">
            <div class="empty-state-icon">📭</div>
            <h2>Aucune réservation trouvée</h2>
            <p>Il n'y a pas encore de réservations enregistrées dans le système.</p>
        </div>
    <% } %>
</div>

</body>
</html>