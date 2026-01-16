<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.text.SimpleDateFormat, mg.coop.model.Reservation, mg.coop.dao.ReservationDAO" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Réservations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <style>
        .search-panel {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .search-panel h3 {
            margin: 0 0 20px 0;
            color: #2c3e50;
        }

        .search-form {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 15px;
            align-items: end;
        }

        .search-form .form-group {
            display: flex;
            flex-direction: column;
        }

        .search-form label {
            margin-bottom: 5px;
            font-weight: bold;
            color: #2c3e50;
            font-size: 14px;
        }

        .search-form input,
        .search-form select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }

        .search-buttons {
            display: flex;
            gap: 10px;
        }

        .btn-search {
            background: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            flex: 1;
        }

        .btn-search:hover {
            background: #2980b9;
        }

        .btn-reset {
            background: #95a5a6;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            text-decoration: none;
            text-align: center;
        }

        .btn-reset:hover {
            background: #7f8c8d;
        }

        .stats-ca {
            background: linear-gradient(135deg, #1abc9c 0%, #16a085 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 15px rgba(26, 188, 156, 0.3);
        }

        .stats-ca h2 {
            margin: 0 0 15px 0;
            font-size: 18px;
            opacity: 0.9;
        }

        .ca-amount {
            font-size: 36px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .ca-details {
            display: flex;
            gap: 30px;
            font-size: 14px;
            opacity: 0.9;
        }

        .ca-detail-item {
            display: flex;
            align-items: center;
            gap: 8px;
        }
    </style>
</head>
<body>
<div class="container">

<jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />
<div class="content">
    
<div class="liste-container">
    
    <% if (request.getAttribute("erreur") != null) { %>
        <div class="erreur"><%= request.getAttribute("erreur") %></div>
    <% } %>
    
    <!-- PANNEAU DE RECHERCHE -->
    <div class="search-panel">
        <h3>🔍 Rechercher des réservations</h3>
        <form method="get" action="${pageContext.request.contextPath}/reservation" class="search-form">
            <input type="hidden" name="action" value="liste">
            
            <div class="form-group">
                <label for="nomClient">Nom du client</label>
                <input type="text" 
                       id="nomClient" 
                       name="nomClient" 
                       placeholder="Ex: Rakoto"
                       value="<%= request.getAttribute("searchNomClient") != null ? request.getAttribute("searchNomClient") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="dateDebut">Date de début</label>
                <input type="date" 
                       id="dateDebut" 
                       name="dateDebut"
                       value="<%= request.getAttribute("searchDateDebut") != null ? request.getAttribute("searchDateDebut") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="dateFin">Date de fin</label>
                <input type="date" 
                       id="dateFin" 
                       name="dateFin"
                       value="<%= request.getAttribute("searchDateFin") != null ? request.getAttribute("searchDateFin") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="taxiTrajetId">Taxi-Trajet</label>
                <select id="taxiTrajetId" name="taxiTrajetId">
                    <option value="">-- Tous --</option>
                    <%
                        List<ReservationDAO.TaxiTrajetInfo> taxiTrajets = 
                            (List<ReservationDAO.TaxiTrajetInfo>) request.getAttribute("taxiTrajets");
                        Integer selectedTaxiTrajetId = (Integer) request.getAttribute("searchTaxiTrajetId");
                        
                        if (taxiTrajets != null) {
                            for (ReservationDAO.TaxiTrajetInfo tt : taxiTrajets) {
                    %>
                        <option value="<%= tt.getId() %>" 
                                <%= (selectedTaxiTrajetId != null && selectedTaxiTrajetId == tt.getId()) ? "selected" : "" %>>
                            <%= tt.getDepart() %> → <%= tt.getArrivee() %> 
                            (<%= tt.getImmatriculation() %> - <%= dateTimeFormat.format(tt.getDateHeureDepart()) %>)
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            
            <div class="search-buttons">
                <button type="submit" class="btn-search">🔍 Rechercher</button>
            </div>
            
            <div class="search-buttons">
                <a href="${pageContext.request.contextPath}/reservation?action=liste" class="btn-reset">🔄 Réinitialiser</a>
            </div>
        </form>
    </div>
    
    <%
        List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
        Map<Integer, List<Integer>> placesParReservation = (Map<Integer, List<Integer>>) request.getAttribute("placesParReservation");
        
        Integer totalReservations = (Integer) request.getAttribute("totalReservations");
        Integer totalPlaces = (Integer) request.getAttribute("totalPlaces");
        Double chiffreAffaires = (Double) request.getAttribute("chiffreAffaires");
        
        if (totalReservations == null) totalReservations = 0;
        if (totalPlaces == null) totalPlaces = 0;
        if (chiffreAffaires == null) chiffreAffaires = 0.0;
    %>
    
    <!-- CHIFFRE D'AFFAIRES -->
    <div class="stats-ca">
        <h2>💰 Chiffre d'affaires</h2>
        <div class="ca-amount"><%= String.format("%,d", chiffreAffaires.longValue()) %> Ar</div>
        <div class="ca-details">
            <div class="ca-detail-item">
                <span>📊</span>
                <span><strong><%= totalReservations %></strong> réservations</span>
            </div>
            <div class="ca-detail-item">
                <span>🪑</span>
                <span><strong><%= totalPlaces %></strong> places réservées</span>
            </div>
            <div class="ca-detail-item">
                <span>📈</span>
                <span>Revenu moyen: <strong><%= totalReservations > 0 ? String.format("%,d", (long)(chiffreAffaires / totalReservations)) : "0" %> Ar</strong>/réservation</span>
            </div>
        </div>
    </div>
    
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
                <h3><%= String.format("%.0f", chiffreAffaires) %> Ar</h3>
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
                                <%= reservation.getDateReservation() != null ? reservation.getDateReservation().format(dateTimeFormatter) : "N/A" %>
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
            <p>Il n'y a pas de réservations correspondant à vos critères de recherche.</p>
        </div>
    <% } %>
</div>
</div>
</div>
</body>
</html>