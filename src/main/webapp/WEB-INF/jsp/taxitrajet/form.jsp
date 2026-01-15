<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= (request.getAttribute("taxitrajet") != null) ? "Modifier" : "Ajouter" %> un Taxi-Trajet</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; margin: 0; padding: 20px; }
        .form-container { max-width: 700px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h1 { color:  #2c3e50; }
        label { font-weight: bold; display: block; margin-top: 15px; }
        input, select { width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 5px; }
        .form-actions { margin-top: 20px; display: flex; gap: 10px; }
        .btn { padding: 10px 20px; border-radius: 5px; text-decoration: none; text-align: center; }
        .btn-primary { background: #1abc9c; color: #fff; }
        .btn-secondary { background: #95a5a6; color: #fff; }
    </style>
</head>
<body>
<div class="form-container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <h1><%= (request.getAttribute("taxitrajet") != null) ? "✏️ Modifier" : "➕ Ajouter" %> un Taxi-Trajet</h1>

    <form method="post" action="<%= request.getContextPath() %>/taxitrajet">
        <%
            mg.coop.model.TaxiTrajet tt = (mg.coop.model.TaxiTrajet) request.getAttribute("taxitrajet");
            List<mg.coop.model.Trajet> trajets = (List<mg.coop.model.Trajet>) request.getAttribute("trajets");
            List<mg.coop.model.Personne> chauffeurs = (List<mg.coop.model.Personne>) request.getAttribute("chauffeurs");
        %>
        <input type="hidden" name="id" value="<%= (tt != null) ? tt.getId() : "" %>">

        <!-- Trajet -->
        <label>Trajet :</label>
        <select name="trajetId" required>
            <%
                if (trajets != null) {
                    for (mg.coop.model.Trajet t : trajets) {
            %>
                <option value="<%= t.getId() %>" <%= (tt != null && tt.getTrajetId() == t.getId()) ? "selected" : "" %>>
                    <%= t.getDepart() %> → <%= t.getArrivee() %>
                </option>
            <%
                    }
                }
            %>
        </select>

        <!-- Chauffeur -->
        <label>Chauffeur :</label>
        <select name="chauffeurId" required>
            <%
                if (chauffeurs != null) {
                    for (mg.coop.model.Personne c : chauffeurs) {
            %>
                <option value="<%= c.getId() %>" <%= (tt != null && tt.getChauffeurId() == c.getId()) ? "selected" : "" %>>
                    <%= c.getNom() %> (<%= c.getTelephone() %>)
                </option>
            <%
                    }
                }
            %>
        </select>
        <!-- Taxi -->
<label>TaxiBrousse (voiture) :</label>
<select name="taxiId" required>
    <%
        List<mg.coop.model.TaxiBrousse> taxis = (List<mg.coop.model.TaxiBrousse>) request.getAttribute("taxis");
        if (taxis != null) {
            for (mg.coop.model.TaxiBrousse t : taxis) {
    %>
        <option value="<%= t.getId() %>" <%= (tt != null && tt.getTaxiId() == t.getId()) ? "selected" : "" %>>
            <%= t.getImmatriculation() %> 
        </option>
    <%
            }
        }
    %>
</select>

        <!-- Date départ -->
        <label>Date et heure de départ :</label>
        <input type="datetime-local" name="dateHeureDepart"
               value="<%= (tt != null) ? tt.getDateHeureDepart().toString().replace('T',' ') : "" %>" required>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                <%= (tt != null) ? "✅ Enregistrer" : "✅ Ajouter" %>
            </button>
            <a href="<%= request.getContextPath() %>/taxitrajet?action=liste" class="btn btn-secondary">❌ Annuler</a>
        </div>
        
    </form>
</div>
</body>
</html>
