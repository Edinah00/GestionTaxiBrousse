<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${taxiTrajet != null ? 'Modifier' : 'Ajouter'} Voyage</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🚗 ${taxiTrajet != null ? 'Modifier' : 'Planifier'} un Voyage</h2>

        <form method="post" style="max-width: 600px; background: #fff; padding: 30px; border-radius: 8px;">
            <input type="hidden" name="id" value="${taxiTrajet.id}">

            <label>Taxi <span style="color: red;">*</span></label>
            <select name="taxiId" required>
                <option value="">-- Sélectionner --</option>
                <c:forEach items="${taxis}" var="t">
                    <option value="${t.id}" ${taxiTrajet.taxi.id == t.id ? 'selected' : ''}>
                        ${t.immatriculation} - ${t.typeVoiture.libelle}
                    </option>
                </c:forEach>
            </select>

            <label>Trajet <span style="color: red;">*</span></label>
            <select name="trajetId" required>
                <option value="">-- Sélectionner --</option>
                <c:forEach items="${trajets}" var="tr">
                    <option value="${tr.id}" ${taxiTrajet.trajet.id == tr.id ? 'selected' : ''}>
                        ${tr.depart} → ${tr.arrivee} (${tr.distanceKm} km)
                    </option>
                </c:forEach>
            </select>

            <label>Chauffeur <span style="color: red;">*</span></label>
            <select name="chauffeurId" required>
                <option value="">-- Sélectionner --</option>
                <c:forEach items="${chauffeurs}" var="ch">
                    <option value="${ch.id}" ${taxiTrajet.chauffeur.id == ch.id ? 'selected' : ''}>
                        ${ch.nom} (${ch.telephone})
                    </option>
                </c:forEach>
            </select>

            <label>Aide chauffeur</label>
            <select name="aideChauffeurId">
                <option value="">-- Aucun --</option>
                <c:forEach items="${aides}" var="a">
                    <option value="${a.id}" ${taxiTrajet.aideChauffeur.id == a.id ? 'selected' : ''}>
                        ${a.nom}
                    </option>
                </c:forEach>
            </select>

            <label>Date et heure de départ <span style="color: red;">*</span></label>
            <input type="datetime-local" name="dateHeureDepart" 
                   value="${taxiTrajet.dateHeureDepart}" required>

            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; background: #1abc9c;">
                    ✅ ${taxiTrajet != null ? 'Modifier' : 'Planifier'}
                </button>
                <a href="${pageContext.request.contextPath}/taxi-trajets" 
                   style="flex: 1; background: #95a5a6; color: white; padding: 10px; text-align: center; border-radius: 4px; text-decoration: none; display: block; line-height: 1.5;">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>