<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">

<div class="container">

    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">

        <h2>Liste des taxis</h2>
        <a href="${pageContext.request.contextPath}/taxi?action=add">Ajouter</a>

        <table border="1">
            <tr>
                <th>ID</th>
                <th>Immatriculation</th>
                <th>Actions</th>
            </tr>

            <c:forEach items="${taxis}" var="t">
                <tr>
                    <td>${t.id}</td>
                    <td>${t.immatriculation}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/taxi?action=edit&id=${t.id}">Modifier</a>
                        <a href="${pageContext.request.contextPath}/taxi?action=delete&id=${t.id}" onclick="return confirm('Supprimer ?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </div>
</div>