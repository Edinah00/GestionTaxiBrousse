<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, mg.coop.model.TaxiTrajet" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Réservation - Places</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <style>
     
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

<div class="reservation-container">
    <h1>🎫 Réservation de Places</h1>
    
    <% if (request.getAttribute("erreur") != null) { %>
        <div class="erreur"><%= request.getAttribute("erreur") %></div>
    <% } %>
    
    <%
        TaxiTrajet trajet = (TaxiTrajet) request.getAttribute("trajet");
        List<Integer> placesOccupees = (List<Integer>) request.getAttribute("placesOccupees");
        
        if (trajet != null) {
    %>
    
    <div class="trajet-info">
        <h3>📍 Informations du Trajet</h3>
        <p><strong>Départ:</strong> <%= trajet.getDepart() %> → <strong>Arrivée:</strong> <%= trajet.getArrivee() %></p>
        <p><strong>Date et heure:</strong> <%= trajet.getDateHeureDepart() %></p>
        <p><strong>Véhicule:</strong> <%= trajet.getTypeVoiture() %> (<%= trajet.getImmatriculation() %>)</p>
        <p><strong>Chauffeur:</strong> <%= trajet.getNomChauffeur() %></p>
        <p><strong>Distance:</strong> <%= trajet.getDistanceKm() %> km</p>
    </div>
    
    <div class="prix-info">
        <h4>💰 Prix par place: <%= String.format("%.0f", trajet.getPrixBase()) %> Ar</h4>
    </div>
    
    <div class="legende">
        <div class="legende-item">
            <div class="legende-color" style="background: white; border: 2px solid #ddd;"></div>
            <span>Disponible</span>
        </div>
        <div class="legende-item">
            <div class="legende-color" style="background: #1abc9c;"></div>
            <span>Sélectionnée</span>
        </div>
        <div class="legende-item">
            <div class="legende-color" style="background: #e74c3c;"></div>
            <span>Occupée</span>
        </div>
    </div>
    
    <form method="post" action="<%= request.getContextPath() %>/reservation" onsubmit="return validerFormulaire()">
        <input type="hidden" name="taxiTrajetId" value="<%= trajet.getId() %>">
        
        <h3>🪑 Sélectionnez vos places</h3>
        <div class="places-grid">
            <% 
                for (int i = 1; i <= trajet.getNbrPlaces(); i++) {
                    boolean occupee = placesOccupees.contains(i);
            %>
                <div class="place-item <%= occupee ? "occupee" : "" %>">
                    <input type="checkbox" 
                           id="place<%= i %>" 
                           name="places" 
                           value="<%= i %>"
                           <%= occupee ? "disabled" : "" %>
                           onchange="calculerTotal()">
                    <label for="place<%= i %>">
                        Place <%= i %>
                        <%= occupee ? "❌" : "" %>
                    </label>
                </div>
            <% } %>
        </div>
        
        <h3>👤 Informations du client</h3>
        
        <div class="form-group">
            <label for="nomClient">Nom complet *</label>
            <input type="text" id="nomClient" name="nomClient" required>
        </div>
        
        <div class="form-group">
            <label for="telephone">Téléphone *</label>
            <input type="tel" id="telephone" name="telephone" required placeholder="034 12 345 67">
        </div>
        
        <h3>💳 Paiement</h3>
        
        <div class="form-group">
            <label for="typePaiement">Type de paiement *</label>
            <select id="typePaiement" name="typePaiement" required onchange="calculerTotal()">
                <option value="TOTAL RESERVATION">Paiement total à la réservation</option>
                <option value="ACOMPTE">Acompte (50%)</option>
                <option value="TOTAL ARRIVEE">Paiement total à l'arrivée</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="modePaiement">Mode de paiement *</label>
            <select id="modePaiement" name="modePaiement" required>
                <option value="ESPECE">Espèces</option>
                <option value="MOBILE MONEY">Mobile Money</option>
            </select>
        </div>
        
        <div class="prix-info" id="montantTotal" style="display: none;">
            <h4>Montant à payer: <span id="montantAPayer">0</span> Ar</h4>
            <p id="infoMontant" style="margin: 5px 0 0 0; font-size: 14px;"></p>
        </div>
        
        <button type="submit" class="btn-submit">✅ Confirmer la réservation</button>
    </form>
    
    <% } else { %>
        <p>Aucune information de trajet disponible.</p>
    <% } %>
</div>

<script>
    const prixParPlace = <%= trajet != null ? trajet.getPrixBase() : 0 %>;
    
    function calculerTotal() {
        const placesSelectionnees = document.querySelectorAll('input[name="places"]:checked').length;
        const typePaiement = document.getElementById('typePaiement').value;
        const montantTotalDiv = document.getElementById('montantTotal');
        const montantAPayer = document.getElementById('montantAPayer');
        const infoMontant = document.getElementById('infoMontant');
        
        if (placesSelectionnees > 0) {
            const total = prixParPlace * placesSelectionnees;
            let montant = total;
            let info = `Total pour ${placesSelectionnees} place(s)`;
            
            if (typePaiement === 'ACOMPTE') {
                montant = total * 0.5;
                info = `Acompte (50%) pour ${placesSelectionnees} place(s). Reste à payer: ${total - montant} Ar`;
            } else if (typePaiement === 'TOTAL ARRIVEE') {
                montant = 0;
                info = `Paiement à l'arrivée pour ${placesSelectionnees} place(s): ${total} Ar`;
            }
            
            montantAPayer.textContent = montant.toFixed(0);
            infoMontant.textContent = info;
            montantTotalDiv.style.display = 'block';
        } else {
            montantTotalDiv.style.display = 'none';
        }
    }
    
    function validerFormulaire() {
        const placesSelectionnees = document.querySelectorAll('input[name="places"]:checked').length;
        
        if (placesSelectionnees === 0) {
            alert('Veuillez sélectionner au moins une place');
            return false;
        }
        
        return confirm(`Confirmer la réservation de ${placesSelectionnees} place(s) ?`);
    }
</script>

</body>
</html>