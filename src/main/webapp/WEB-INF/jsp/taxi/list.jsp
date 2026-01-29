<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste Taxis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🚐 Liste des Taxis</h2>

        <form method="get" style="background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
            <label>Immatriculation :</label>
            <input type="text" name="immatriculation" value="${param.immatriculation}" placeholder="Ex: 1234 TBA">

            <label>Type de voiture :</label>
            <select name="typeVoitureId">
                <option value="">-- Tous --</option>
                <c:forEach items="${typeVoitures}" var="tv">
                    <option value="${tv.id}" ${param.typeVoitureId == tv.id ? 'selected' : ''}>
                        ${tv.libelle}
                    </option>
                </c:forEach>
            </select>

            <button type="submit">🔍 Rechercher</button>
        </form>

        <a href="?action=form" class="btn" style="display: inline-block; margin-bottom: 15px; background: #1abc9c; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
            ➕ Ajouter un taxi
        </a>

        <table class="table-list">
            <tr>
                <th>ID</th>
                <th>Immatriculation</th>
                <th>Type de voiture</th>
                <th>Nb places</th>
                <th>Coopérative</th>
                <th>Actions</th>
            </tr>
            <c:forEach items="${taxis}" var="t">
                <tr>
                    <td>${t.id}</td>
                    <td><strong>${t.immatriculation}</strong></td>
                    <td>${t.typeVoiture.libelle}</td>
                    <td>${t.typeVoiture.nbrPlaces}</td>
                    <td>${t.cooperative.nom}</td>
                    <td>
                        <a href="?action=edit&id=${t.id}" style="color: #f39c12;">✏️ Modifier</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${empty taxis}">
            <p style="text-align: center; color: #7f8c8d; padding: 40px;">Aucun taxi trouvé.</p>
        </c:if>
    </div>
</div>

</body>
</html>