<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${typeVoiture != null ? 'Modifier' : 'Ajouter'} Type Voiture</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>🚗 ${typeVoiture != null ? 'Modifier' : 'Ajouter'} Type de Voiture</h2>

        <form method="post" style="max-width: 600px; background: #fff; padding: 30px; border-radius: 8px;">
            <input type="hidden" name="id" value="${typeVoiture.id}">

            <label>Libellé <span style="color: red;">*</span></label>
            <input type="text" name="libelle" value="${typeVoiture.libelle}" required placeholder="Ex: Sprinter 15 places">

            <label>Nombre de places <span style="color: red;">*</span></label>
            <input type="number" name="nbrPlaces" value="${typeVoiture.nbrPlaces}" required min="1" placeholder="Ex: 15">

            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; background: #1abc9c;">
                    ✅ ${typeVoiture != null ? 'Modifier' : 'Ajouter'}
                </button>
                <a href="${pageContext.request.contextPath}/type-voitures" 
                   style="flex: 1; background: #95a5a6; color: white; padding: 10px; text-align: center; border-radius: 4px; text-decoration: none; display: block; line-height: 1.5;">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>