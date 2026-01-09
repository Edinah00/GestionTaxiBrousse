<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion Taxi Brousse</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/index.css">
</head>

<body>

<div class="container">

    <!-- MENU GAUCHE -->
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <!-- CONTENU -->
    <div class="content">
        <h1>Bienvenue dans le système de gestion Taxi Brousse</h1>

        <p>
            Cette application permet de gérer efficacement les taxis,
            les trajets, les réservations et les dépenses de la coopérative.
        </p>

        <!-- CARTES -->
        <div class="cards">

            <div class="card">
                <h3>🚕 Gestion des Taxis</h3>
                <p>Ajouter, modifier ou supprimer les taxis brousse.</p>
                <a href="${pageContext.request.contextPath}/taxi">Accéder →</a>
            </div>

            <div class="card">
                <h3>🛣️ Gestion des Trajets</h3>
                <p>Définir les trajets entre les villes.</p>
                <a href="${pageContext.request.contextPath}/trajet">Accéder →</a>
            </div>

            <div class="card">
                <h3>📅 Réservations</h3>
                <p>Gérer les réservations des passagers.</p>
                <a href="${pageContext.request.contextPath}/rechercheVoiture">Accéder →</a>
            </div>

            <div class="card">
                <h3>💰 Dépenses</h3>
                <p>Suivre les dépenses liées aux trajets.</p>
                <a href="${pageContext.request.contextPath}/depense">Accéder →</a>
            </div>

        </div>
    </div>

</div>

</body>
</html>
