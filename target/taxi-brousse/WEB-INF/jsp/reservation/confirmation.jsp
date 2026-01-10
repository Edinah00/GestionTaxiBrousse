<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Confirmation de Réservation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <style>
      
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

<div class="confirmation-container">
    <div class="success-icon">✅</div>
    
    <h1>Réservation Confirmée !</h1>
    
    <p>Votre réservation a été enregistrée avec succès.</p>
    
    <div class="info-box">
        <p><strong>Numéro de réservation:</strong> #<%= request.getAttribute("reservationId") %></p>
        <p><strong>Date:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) %></p>
    </div>
    
    <%
        Double montantPaye = (Double) request.getAttribute("montantPaye");
        Double montantTotal = (Double) request.getAttribute("montantTotal");
        
        if (montantPaye != null && montantTotal != null) {
    %>
        <div class="montant-box">
            <h3>Montant payé: <%= String.format("%.0f", montantPaye) %> Ar</h3>
            <% if (montantPaye < montantTotal) { %>
                <p style="margin: 5px 0 0 0; font-size: 14px;">
                    Reste à payer: <%= String.format("%.0f", montantTotal - montantPaye) %> Ar
                </p>
            <% } %>
        </div>
    <% } %>
    
    <div class="info-box">
        <p><strong>⚠️ Important:</strong></p>
        <ul style="text-align: left; margin: 10px 0;">
            <li>Veuillez vous présenter 15 minutes avant le départ</li>
            <li>Présentez votre numéro de réservation au guichet</li>
            <li>N'oubliez pas une pièce d'identité</li>
        </ul>
    </div>
    
    <div class="btn-actions">
        <a href="${pageContext.request.contextPath}/rechercheVoiture" class="btn btn-secondary">
            🔍 Nouvelle recherche
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
            🏠 Retour à l'accueil
        </a>
    </div>
</div>

</body>
</html>