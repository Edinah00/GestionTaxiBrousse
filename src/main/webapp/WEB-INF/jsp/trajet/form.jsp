<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${trajet != null ? 'Modifier' : 'Ajouter'} Trajet</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🛣️ ${trajet != null ? 'Modifier' : 'Ajouter'} Trajet</h2>

        <form method="post" style="max-width: 600px; background: #fff; padding: 30px; border-radius: 8px;">
            <input type="hidden" name="id" value="${trajet.id}">

            <label>Lieu de départ <span style="color: red;">*</span></label>
            <input type="text" name="depart" value="${trajet.depart}" required placeholder="Ex: Antananarivo">

            <label>Lieu d'arrivée <span style="color: red;">*</span></label>
            <input type="text" name="arrivee" value="${trajet.arrivee}" required placeholder="Ex: Toamasina">

            <label>Distance (km) <span style="color: red;">*</span></label>
            <input type="number" name="distanceKm" value="${trajet.distanceKm}" required min="1" placeholder="Ex: 350">

            <label>Prix de base (Ar) <span style="color: red;">*</span></label>
            <input type="number" step="0.01" name="prixBase" value="${trajet.prixBase}" required min="0" placeholder="Ex: 50000">

            <label>Durée estimée (jours) <span style="color: red;">*</span></label>
            <input type="number" step="0.1" name="nbrJour" value="${trajet.nbrJour}" required min="0.1" placeholder="Ex: 1 ou 1.5">

            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; background: #1abc9c;">
                    ✅ ${trajet != null ? 'Modifier' : 'Ajouter'}
                </button>
                <a href="${pageContext.request.contextPath}/trajets" 
                   style="flex: 1; background: #95a5a6; color: white; padding: 10px; text-align: center; border-radius: 4px; text-decoration: none; display: block; line-height: 1.5;">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>