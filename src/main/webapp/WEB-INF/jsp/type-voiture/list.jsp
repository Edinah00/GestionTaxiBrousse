<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Types de Voiture</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🚗 Types de Voiture</h2>

        <a href="?action=form" class="btn" style="display: inline-block; margin-bottom: 15px; background: #1abc9c; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
            ➕ Ajouter un type
        </a>

        <table class="table-list">
            <tr>
                <th>ID</th>
                <th>Libellé</th>
                <th>Nombre de places</th>
                <th>Actions</th>
            </tr>
            <c:forEach items="${typeVoitures}" var="tv">
                <tr>
                    <td>${tv.id}</td>
                    <td><strong>${tv.libelle}</strong></td>
                    <td>${tv.nbrPlaces}</td>
                    <td>
                        <a href="?action=edit&id=${tv.id}" style="color: #f39c12;">✏️ Modifier</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${empty typeVoitures}">
            <p style="text-align: center; color: #7f8c8d; padding: 40px;">Aucun type de voiture enregistré.</p>
        </c:if>
    </div>
</div>

</body>
</html>