<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Taxi-Trajets</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; margin: 0; padding: 20px; }
        h1 { color: #2c3e50; }
        a.btn { padding: 8px 12px; border-radius: 4px; text-decoration: none; margin: 5px; }
        .btn-primary { background: #3498db; color: #fff; max-height: 50px; }
        .btn-warning { background: #f39c12; color: #fff; }
        .btn-danger { background: #e74c3c; color: #fff; }
        .btn-success { background: #1abc9c; color: #fff; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; background: #fff; }
        th, td { padding: 10px; border-bottom: 1px solid #ddd; }
        th { background: #3498db; color: #fff; }
        tr:hover { background: #f1f1f1; }
        
        .valeur-cell {
            text-align: right;
            font-weight: bold;
        }
        
        .valeur-max {
            color: #3498db;
        }
        
        .valeur-reel {
            color: #1abc9c;
        }
        
        .taux-badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: bold;
            color: white;
        }
        
        .taux-excellent {
            background: #27ae60;
        }
        
        .taux-bon {
            background: #1abc9c;
        }
        
        .taux-moyen {
            background: #f39c12;
        }
        
        .taux-faible {
            background: #e74c3c;
        }
    </style>
</head>
<body>
    <div class="container">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />
        <div class="content">

            <h1>Liste des Taxi-Trajets</h1>
            <a href="<%= request.getContextPath() %>/taxitrajet?action=add" class="btn btn-primary">➕ Nouveau Taxi-Trajet</a>
            
            <form method="get" action="<%= request.getContextPath() %>/taxitrajet">
                <input type="hidden" name="action" value="liste">

                <label for="trajet">Filtrer par trajet :</label>
                <select name="trajet" id="trajet" onchange="this.form.submit()">
                    <option value="">-- Tous les trajets --</option>
                    <%
                        List<String> trajets = (List<String>) request.getAttribute("trajetsDisponibles");
                        String trajetSelectionne = request.getParameter("trajet");

                        if (trajets != null) {
                            for (String t : trajets) {
                    %>
                        <option value="<%= t %>" <%= t.equals(trajetSelectionne) ? "selected" : "" %>>
                            <%= t %>
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
            </form>

            <table>
                <tr>
                    <th>Taxi</th>
                    <th>Trajet</th>
                    <th>Chauffeur</th>
                    <th>Date départ</th>
                    <th>Valeur Max</th>
                    <th>Valeur Réelle</th>
                    <th>Taux (%)</th>
                    <th>Actions</th>
                </tr>
                <%
                    List<TaxiTrajet> liste = (List<TaxiTrajet>) request.getAttribute("taxitrajets");
                    if (liste != null) {
                        for (TaxiTrajet tt : liste) {
                            double tauxRemplissage = tt.getTauxRemplissageValeur();
                            String tauxClass = "taux-faible";
                            if (tauxRemplissage >= 90) tauxClass = "taux-excellent";
                            else if (tauxRemplissage >= 70) tauxClass = "taux-bon";
                            else if (tauxRemplissage >= 50) tauxClass = "taux-moyen";
                %>
                <tr>
                    <td><%= tt.getImmatriculation() %></td>
                    <td><%= tt.getDepart() %> → <%= tt.getArrivee() %></td>
                    <td><%= tt.getNomChauffeur() %></td>
                    <td><%= tt.getDateHeureDepart() %></td>
                    <td class="valeur-cell valeur-max">
                        <%= String.format("%,d", (long)tt.getValMax()) %> Ar
                    </td>
                    <td class="valeur-cell valeur-reel">
                        <%= String.format("%,d", (long)tt.getValReel()) %> Ar
                    </td>
                    <td style="text-align: center;">
                        <span class="taux-badge <%= tauxClass %>">
                            <%= String.format("%.0f", tauxRemplissage) %>%
                        </span>
                    </td>
                    <td>
                        <a href="<%= request.getContextPath() %>/taxitrajet?action=detailsValeurReel&id=<%= tt.getId() %>" 
                           class="btn btn-success" 
                           title="Voir détails valeur réelle">
                            💰 Détails
                        </a>
                        <a href="<%= request.getContextPath() %>/taxitrajet?action=edit&id=<%= tt.getId() %>" 
                           class="btn btn-warning">
                            ✏️ Modifier
                        </a>
                        <a href="<%= request.getContextPath() %>/taxitrajet?action=delete&id=<%= tt.getId() %>" 
                           class="btn btn-danger"
                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce taxi-trajet ?')">
                            🗑️ Supprimer
                        </a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>
    </div>
</body>
</html>