<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion Taxi Brousse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h1>🚐 Bienvenue dans Taxi Brousse Manager</h1>
        <p style="color: #7f8c8d; font-size: 18px; margin-bottom: 40px;">
            Système complet de gestion pour votre coopérative de transport
        </p>

        <div class="cards">
            <div class="card">
                <h3>👥 Personnel</h3>
                <p>Gérez les chauffeurs, aides, guichetiers et tout le personnel</p>
                <a href="${pageContext.request.contextPath}/personnes">Gérer →</a>
            </div>

            <div class="card">
                <h3>🚐 Flotte de véhicules</h3>
                <p>Suivez vos taxis et leurs caractéristiques</p>
                <a href="${pageContext.request.contextPath}/taxis">Voir les taxis →</a>
            </div>

            <div class="card">
                <h3>🛣️ Trajets</h3>
                <p>Définissez les itinéraires et leurs tarifs</p>
                <a href="${pageContext.request.contextPath}/trajets">Gérer les trajets →</a>
            </div>

            <div class="card">
                <h3>📅 Voyages</h3>
                <p>Planifiez et suivez tous les voyages</p>
                <a href="${pageContext.request.contextPath}/taxi-trajets">Voir les voyages →</a>
            </div>

            <div class="card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                <h3 style="color: white;">🎫 Réservations</h3>
                <p style="color: rgba(255,255,255,0.9);">Recherchez et réservez des places pour vos clients</p>
                <a href="${pageContext.request.contextPath}/reservation/search" 
                   style="color: white; border-color: white;">Réserver maintenant →</a>
            </div>

            <div class="card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white;">
                <h3 style="color: white;">💰 Paiements</h3>
                <p style="color: rgba(255,255,255,0.9);">Consultez l'historique des paiements et le chiffre d'affaires</p>
                <a href="${pageContext.request.contextPath}/paiement" 
                   style="color: white; border-color: white;">Voir les paiements →</a>
            </div>
        </div>

        <div style="margin-top: 60px; padding: 30px; background: #ecf0f1; border-radius: 10px;">
            <h3>📊 Statistiques rapides</h3>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-top: 20px;">
                <div style="background: white; padding: 20px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 32px; color: #3498db; font-weight: bold;">--</div>
                    <div style="color: #7f8c8d; margin-top: 5px;">Voyages ce mois</div>
                </div>
                <div style="background: white; padding: 20px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 32px; color: #1abc9c; font-weight: bold;">--</div>
                    <div style="color: #7f8c8d; margin-top: 5px;">Réservations actives</div>
                </div>
                <div style="background: white; padding: 20px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 32px; color: #e74c3c; font-weight: bold;">--</div>
                    <div style="color: #7f8c8d; margin-top: 5px;">CA du mois (Ar)</div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>