<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste Personnel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>👥 Liste du Personnel</h2>

        <!-- Formulaire de recherche -->
        <form method="get" style="background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
            <label>Nom :</label>
            <input type="text" name="nom" value="${param.nom}" placeholder="Rechercher par nom">

            <label>Rôle :</label>
            <select name="role">
                <option value="">-- Tous --</option>
                <option value="CHAUFFEUR" ${param.role == 'CHAUFFEUR' ? 'selected' : ''}>Chauffeur</option>
                <option value="AIDE CHAUFFEUR" ${param.role == 'AIDE CHAUFFEUR' ? 'selected' : ''}>Aide Chauffeur</option>
                <option value="GUICHETIER" ${param.role == 'GUICHETIER' ? 'selected' : ''}>Guichetier</option>
                <option value="CAISSIER" ${param.role == 'CAISSIER' ? 'selected' : ''}>Caissier</option>
                <option value="RESP PLANNING" ${param.role == 'RESP PLANNING' ? 'selected' : ''}>Resp Planning</option>
                <option value="MECANICIEN" ${param.role == 'MECANICIEN' ? 'selected' : ''}>Mécanicien</option>
                <option value="COMPTABLE" ${param.role == 'COMPTABLE' ? 'selected' : ''}>Comptable</option>
                <option value="DIRECTEUR" ${param.role == 'DIRECTEUR' ? 'selected' : ''}>Directeur</option>
                <option value="AGENT COMMERCIAL" ${param.role == 'AGENT COMMERCIAL' ? 'selected' : ''}>Agent Commercial</option>
            </select>

            <button type="submit">🔍 Rechercher</button>
        </form>

        <a href="?action=form" class="btn" style="display: inline-block; margin-bottom: 15px; background: #1abc9c; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
            ➕ Ajouter une personne
        </a>

        <table class="table-list">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Téléphone</th>
                    <th>Rôle</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${personnes}" var="p">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.nom}</td>
                        <td>${p.telephone}</td>
                        <td><span style="background: #3498db; color: white; padding: 3px 8px; border-radius: 3px; font-size: 12px;">${p.role}</span></td>
                        <td>
                            <a href="?action=edit&id=${p.id}" style="color: #f39c12; text-decoration: none;">✏️ Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty personnes}">
            <p style="text-align: center; color: #7f8c8d; padding: 40px;">Aucune personne trouvée.</p>
        </c:if>
    </div>
</div>

</body>
</html>