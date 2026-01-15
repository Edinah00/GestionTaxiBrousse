<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>
<%@ page import="mg.coop.model.Trajet" %>
<%@ page import="mg.coop.model.Personne" %>
<%@ page import="mg.coop.model.TaxiBrousse" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <%
        TaxiTrajet tt = (TaxiTrajet) request.getAttribute("taxitrajet");
    %>
    <title><%= (tt != null) ? "Modifier" : "Ajouter" %> un Taxi-Trajet</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
    <style>
        .content { flex: 1; padding: 30px; }
        .form-container { max-width: 800px; margin: 0 auto; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .form-header { margin-bottom: 30px; }
        .form-header h1 { margin: 0; color: #2c3e50; }
        .form-header p { margin: 10px 0 0 0; color: #7f8c8d; }
        .form-group { margin-bottom: 25px; }
        .form-group label { display: block; margin-bottom: 8px; font-weight: bold; color: #2c3e50; }
        .form-group label .required { color: #e74c3c; }
        .form-group input, .form-group select { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; box-sizing: border-box; }
        .form-group input:focus, .form-group select:focus { outline: none; border-color: #1abc9c; box-shadow: 0 0 0 3px rgba(26,188,156,0.1); }
        .form-group small { display: block; margin-top: 5px; color: #7f8c8d; font-size: 13px; }
        .form-actions { display: flex; gap: 15px; margin-top: 30px; padding-top: 30px; border-top: 2px solid #ecf0f1; }
        .btn { padding: 12px 30px; border: none; border-radius: 5px; font-size: 16px; font-weight: bold; cursor: pointer; text-decoration: none; display: inline-block; text-align: center; }
        .btn-primary { background: #1abc9c; color: white; flex: 1; }
        .btn-primary:hover { background: #16a085; }
        .btn-secondary { background: #95a5a6; color: white; flex: 1; }
        .btn-secondary:hover { background: #7f8c8d; }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="form-container">
        <div class="form-header">
            <h1><%= (tt != null) ? "✏️ Modifier" : "➕ Ajouter" %> un Taxi-Trajet</h1>
            <p><%= (tt != null) ? "Modifiez les informations du trajet taxi" : "Remplissez les informations du nouveau trajet taxi" %></p>
        </div>

        <form method="post" action="<%= request.getContextPath() %>/taxitrajet">
            <%
                List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
                List<Personne> chauffeurs = (List<Personne>) request.getAttribute("chauffeurs");
                List<TaxiBrousse> taxis = (List<TaxiBrousse>) request.getAttribute("taxis");

                // Formater la date pour datetime-local
                String dateValue = "";
                if (tt != null && tt.getDateHeureDepart() != null) {
                    LocalDateTime ldt = tt.getDateHeureDepart();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    dateValue = ldt.format(formatter);
                }
            %>

            <input type="hidden" name="id" value="<%= (tt != null) ? tt.getId() : "" %>">

            <!-- Trajet -->
            <div class="form-group">
                <label>Trajet <span class="required">*</span></label>
                <select name="trajetId" required>
                    <%
                        if (trajets != null) {
                            for (Trajet t : trajets) {
                    %>
                        <option value="<%= t.getId() %>" <%= (tt != null && tt.getTrajetId() == t.getId()) ? "selected" : "" %>>
                            <%= t.getDepart() %> → <%= t.getArrivee() %>
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Chauffeur -->
            <div class="form-group">
                <label>Chauffeur <span class="required">*</span></label>
                <select name="chauffeurId" required>
                    <%
                        if (chauffeurs != null) {
                            for (Personne c : chauffeurs) {
                    %>
                        <option value="<%= c.getId() %>" <%= (tt != null && tt.getChauffeurId() == c.getId()) ? "selected" : "" %>>
                            <%= c.getNom() %> (<%= c.getTelephone() %>)
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Taxi -->
            <div class="form-group">
                <label>TaxiBrousse (voiture) <span class="required">*</span></label>
                <select name="taxiId" required>
                    <%
                        if (taxis != null) {
                            for (TaxiBrousse t : taxis) {
                    %>
                        <option value="<%= t.getId() %>" <%= (tt != null && tt.getTaxiId() == t.getId()) ? "selected" : "" %>>
                            <%= t.getImmatriculation() %>
                        </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Date départ -->
            <div class="form-group">
                <label>Date et heure de départ <span class="required">*</span></label>
                <input type="datetime-local" name="dateHeureDepart" value="<%= dateValue %>" required>
            </div>

            <!-- Actions -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <%= (tt != null) ? "✅ Enregistrer" : "✅ Ajouter" %>
                </button>
                <a href="<%= request.getContextPath() %>/taxitrajet?action=liste" class="btn btn-secondary">❌ Annuler</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
