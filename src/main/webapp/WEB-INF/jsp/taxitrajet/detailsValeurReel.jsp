<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mg.coop.util.ValeurReelUtil" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détails Valeur Réelle</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
    <style>
        .valeur-container {
            max-width: 900px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .header-valeur {
            background: linear-gradient(135deg, #1abc9c 0%, #16a085 100%);
            color: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 30px;
        }

        .header-valeur h1 {
            margin: 0 0 10px 0;
            font-size: 28px;
        }

        .header-valeur p {
            margin: 0;
            opacity: 0.9;
        }

        .comparison-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }

        .valeur-card {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 10px;
            border: 2px solid #ecf0f1;
        }

        .valeur-card.max {
            border-color: #3498db;
        }

        .valeur-card.reel {
            border-color: #1abc9c;
        }

        .valeur-card h3 {
            margin: 0 0 15px 0;
            color: #2c3e50;
            font-size: 18px;
        }

        .valeur-amount {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .valeur-card.max .valeur-amount {
            color: #3498db;
        }

        .valeur-card.reel .valeur-amount {
            color: #1abc9c;
        }

        .detail-table {
            width: 100%;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .detail-table table {
            width: 100%;
            border-collapse: collapse;
        }

        .detail-table th {
            background: #2c3e50;
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: bold;
        }

        .detail-table td {
            padding: 15px;
            border-bottom: 1px solid #ecf0f1;
        }

        .detail-table tr:last-child td {
            border-bottom: none;
        }

        .detail-table tr:hover {
            background: #f8f9fa;
        }

        .category-badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            color: white;
        }

        .badge-vip {
            background: #9b59b6;
        }

        .badge-premium {
            background: #e67e22;
        }

        .badge-standard {
            background: #3498db;
        }

        .badge-enfant {
            background: #1abc9c;
        }

        .total-row {
            background: #e8f8f5 !important;
            font-weight: bold;
            font-size: 18px;
        }

        .taux-remplissage {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
            border: 2px solid #ecf0f1;
        }

        .taux-remplissage h3 {
            margin: 0 0 15px 0;
            color: #2c3e50;
        }

        .progress-bar {
            width: 100%;
            height: 30px;
            background: #ecf0f1;
            border-radius: 15px;
            overflow: hidden;
            position: relative;
        }

        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #1abc9c 0%, #16a085 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
            transition: width 0.5s ease;
        }

        .btn-back {
            background: #95a5a6;
            color: white;
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            display: inline-block;
            margin-top: 20px;
        }

        .btn-back:hover {
            background: #7f8c8d;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />
    
    <div class="content">
        <div class="valeur-container">
            <%
                TaxiTrajet taxiTrajet = (TaxiTrajet) request.getAttribute("taxitrajet");
                ValeurReelUtil.VentesParCategorie ventes = 
                    (ValeurReelUtil.VentesParCategorie) request.getAttribute("ventes");
                Double valeurMax = (Double) request.getAttribute("valeurMax");
                
                if (taxiTrajet != null && ventes != null) {
                    double valeurReel = ventes.getValeurReelle();
                    double tauxRemplissage = (valeurMax > 0) ? (valeurReel / valeurMax * 100) : 0;
            %>
            
            <div class="header-valeur">
                <h1>💰 Analyse de la Valeur Réelle</h1>
                <p>
                    <%= taxiTrajet.getDepart() %> → <%= taxiTrajet.getArrivee() %> 
                    (<%= taxiTrajet.getImmatriculation() %>) - 
                    <%= taxiTrajet.getDateHeureDepart() %>
                </p>
            </div>
            
            <!-- Comparaison Valeur Max vs Valeur Réelle -->
            <div class="comparison-grid">
                <div class="valeur-card max">
                    <h3>📊 Valeur Maximale Théorique</h3>
                    <div class="valeur-amount">
                        <%= String.format("%,d", valeurMax.longValue()) %> Ar
                    </div>
                    <p style="color: #7f8c8d; margin: 0;">
                        Si toutes les places sont vendues
                    </p>
                </div>
                
                <div class="valeur-card reel">
                    <h3>💵 Valeur Réelle</h3>
                    <div class="valeur-amount">
                        <%= String.format("%,d", (long)valeurReel) %> Ar
                    </div>
                    <p style="color: #7f8c8d; margin: 0;">
                        Places effectivement vendues
                    </p>
                </div>
            </div>
            
            <!-- Taux de Remplissage -->
            <div class="taux-remplissage">
                <h3>📈 Taux de Remplissage (en valeur)</h3>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: <%= tauxRemplissage %>%">
                        <%= String.format("%.1f", tauxRemplissage) %>%
                    </div>
                </div>
            </div>
            
            <!-- Détail par Catégorie -->
            <h2 style="margin-top: 40px; color: #2c3e50;">📋 Détail des Ventes par Catégorie</h2>
            
            <div class="detail-table">
                <table>
                    <thead>
                        <tr>
                            <th>Catégorie</th>
                            <th>Places Vendues</th>
                            <th>Prix Unitaire</th>
                            <th>Sous-Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- VIP -->
                        <tr>
                            <td>
                                <span class="category-badge badge-vip">VIP</span>
                            </td>
                            <td><strong><%= ventes.getNbPlacesVip() %></strong> place(s)</td>
                            <td><%= String.format("%,d", (long)ventes.getPrixVip()) %> Ar</td>
                            <td><strong><%= String.format("%,d", (long)(ventes.getNbPlacesVip() * ventes.getPrixVip())) %> Ar</strong></td>
                        </tr>
                        
                        <!-- Premium -->
                        <tr>
                            <td>
                                <span class="category-badge badge-premium">PREMIUM</span>
                            </td>
                            <td><strong><%= ventes.getNbPlacesPremium() %></strong> place(s)</td>
                            <td><%= String.format("%,d", (long)ventes.getPrixPremium()) %> Ar</td>
                            <td><strong><%= String.format("%,d", (long)(ventes.getNbPlacesPremium() * ventes.getPrixPremium())) %> Ar</strong></td>
                        </tr>
                        
                        <!-- Standard Adultes -->
                        <tr>
                            <td>
                                <span class="category-badge badge-standard">STANDARD</span> (Adultes)
                            </td>
                            <td><strong><%= (ventes.getNbPlacesStandard() - ventes.getNbEnfants()) %></strong> place(s)</td>
                            <td><%= String.format("%,d", (long)ventes.getPrixStandard()) %> Ar</td>
                            <td><strong><%= String.format("%,d", (long)((ventes.getNbPlacesStandard() - ventes.getNbEnfants()) * ventes.getPrixStandard())) %> Ar</strong></td>
                        </tr>
                        
                        <!-- Enfants -->
                        <tr>
                            <td>
                                <span class="category-badge badge-enfant">ENFANT</span> 
                                <small style="color: #7f8c8d;">(Remise appliquée)</small>
                            </td>
                            <td><strong><%= ventes.getNbEnfants() %></strong> enfant(s)</td>
                            <td><%= String.format("%,d", (long)ventes.getPrixEnfant()) %> Ar</td>
                            <td><strong><%= String.format("%,d", (long)(ventes.getNbEnfants() * ventes.getPrixEnfant())) %> Ar</strong></td>
                        </tr>
                        
                        <!-- Total -->
                        <tr class="total-row">
                            <td colspan="3">TOTAL VALEUR RÉELLE</td>
                            <td><strong><%= String.format("%,d", (long)valeurReel) %> Ar</strong></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            
            <!-- Statistiques supplémentaires -->
            <div style="margin-top: 30px; padding: 20px; background: #f8f9fa; border-radius: 10px;">
                <h3 style="margin: 0 0 15px 0; color: #2c3e50;">📊 Statistiques</h3>
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px;">
                    <div>
                        <strong>Total places vendues:</strong><br>
                        <%= (ventes.getNbPlacesVip() + ventes.getNbPlacesPremium() + ventes.getNbPlacesStandard()) %> places
                    </div>
                    <div>
                        <strong>Prix moyen par place:</strong><br>
                        <%= String.format("%,d", (long)(valeurReel / (ventes.getNbPlacesVip() + ventes.getNbPlacesPremium() + ventes.getNbPlacesStandard()))) %> Ar
                    </div>
                    <div>
                        <strong>Manque à gagner:</strong><br>
                        <span style="color: #e74c3c;">
                            <%= String.format("%,d", (long)(valeurMax - valeurReel)) %> Ar
                        </span>
                    </div>
                </div>
            </div>
            
            <a href="<%= request.getContextPath() %>/taxitrajet?action=liste" class="btn-back">
                ← Retour à la liste
            </a>
            
            <% } else { %>
                <div class="empty-state">
                    <h2>Données non disponibles</h2>
                    <p>Impossible de récupérer les informations du taxi-trajet.</p>
                    <a href="<%= request.getContextPath() %>/taxitrajet?action=liste" class="btn-back">
                        ← Retour à la liste
                    </a>
                </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>