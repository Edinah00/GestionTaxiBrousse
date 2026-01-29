<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste Trajets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🛣️ Liste des Trajets</h2>

        <form method="get" style="background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
            <label>Départ :</label>
            <input type="text" name="depart" value="${param.depart}" placeholder="Ex: Antananarivo">

            <label>Arrivée :</label>
            <input type="text" name="arrivee" value="${param.arrivee}" placeholder="Ex: Toamasina">

            <button type="submit">🔍 Rechercher</button>
        </form>

        <a href="?action=form" class="btn" style="display: inline-block; margin-bottom: 15px; background: #1abc9c; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
            ➕ Ajouter un trajet
        </a>

        <table class="table-list">
            <tr>
                <th>ID</th>
                <th>Trajet</th>
                <th>Distance (km)</th>
                <th>Prix base (Ar)</th>
                <th>Durée (jours)</th>
                <th>Actions</th>
            </tr>
            <c:forEach items="${trajets}" var="t">
                <tr>
                    <td>${t.id}</td>
                    <td><strong>${t.depart}</strong> → <strong>${t.arrivee}</strong></td>
                    <td>${t.distanceKm}</td>
                    <td>${t.prixBase}</td>
                    <td>${t.nbrJour}</td>
                    <td>
                        <a href="?action=edit&id=${t.id}" style="color: #f39c12;">✏️ Modifier</a> |
                        <a href="?action=delete&id=${t.id}" style="color: #e74c3c;" onclick="return confirm('Supprimer ce trajet ?')">🗑️ Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${empty trajets}">
            <p style="text-align: center; color: #7f8c8d; padding: 40px;">Aucun trajet trouvé.</p>
        </c:if>
    </div>
</div>

</body>
</html>