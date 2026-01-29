<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter Taxi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🚐 Ajouter un Taxi</h2>

        <form method="post" style="max-width: 600px; background: #fff; padding: 30px; border-radius: 8px;">
            <label>Coopérative <span style="color: red;">*</span></label>
            <select name="cooperativeId" required>
                <option value="">-- Sélectionner --</option>
                <c:forEach items="${cooperatives}" var="coop">
                    <option value="${coop.id}">${coop.nom}</option>
                </c:forEach>
            </select>

            <label>Type de voiture <span style="color: red;">*</span></label>
            <select name="typeVoitureId" required>
                <option value="">-- Sélectionner --</option>
                <c:forEach items="${typeVoitures}" var="tv">
                    <option value="${tv.id}">${tv.libelle} (${tv.nbrPlaces} places)</option>
                </c:forEach>
            </select>

            <label>Immatriculation <span style="color: red;">*</span></label>
            <input type="text" name="immatriculation" required placeholder="Ex: 1234 TBA">

            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; background: #1abc9c;">✅ Ajouter</button>
                <a href="${pageContext.request.contextPath}/taxis" 
                   style="flex: 1; background: #95a5a6; color: white; padding: 10px; text-align: center; border-radius: 4px; text-decoration: none; display: block; line-height: 1.5;">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>