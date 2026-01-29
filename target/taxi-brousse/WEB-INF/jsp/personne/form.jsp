<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${personne != null ? 'Modifier' : 'Ajouter'} Personne</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>👤 ${personne != null ? 'Modifier' : 'Ajouter'} Personne</h2>

        <form method="post" style="max-width: 600px; background: #fff; padding: 30px; border-radius: 8px;">
            <input type="hidden" name="id" value="${personne.id}">

            <label>Nom complet <span style="color: red;">*</span></label>
            <input type="text" name="nom" value="${personne.nom}" required placeholder="Ex: Rakoto Jean">

            <label>Téléphone</label>
            <input type="text" name="telephone" value="${personne.telephone}" placeholder="Ex: 0341234567">

            <label>Rôle <span style="color: red;">*</span></label>
            <select name="role" required>
                <option value="">-- Sélectionner --</option>
                <option value="CHAUFFEUR" ${personne.role == 'CHAUFFEUR' ? 'selected' : ''}>Chauffeur</option>
                <option value="AIDE CHAUFFEUR" ${personne.role == 'AIDE CHAUFFEUR' ? 'selected' : ''}>Aide Chauffeur</option>
                <option value="GUICHETIER" ${personne.role == 'GUICHETIER' ? 'selected' : ''}>Guichetier</option>
                <option value="CAISSIER" ${personne.role == 'CAISSIER' ? 'selected' : ''}>Caissier</option>
                <option value="RESP PLANNING" ${personne.role == 'RESP PLANNING' ? 'selected' : ''}>Resp Planning</option>
                <option value="MECANICIEN" ${personne.role == 'MECANICIEN' ? 'selected' : ''}>Mécanicien</option>
                <option value="COMPTABLE" ${personne.role == 'COMPTABLE' ? 'selected' : ''}>Comptable</option>
                <option value="DIRECTEUR" ${personne.role == 'DIRECTEUR' ? 'selected' : ''}>Directeur</option>
                <option value="AGENT COMMERCIAL" ${personne.role == 'AGENT COMMERCIAL' ? 'selected' : ''}>Agent Commercial</option>
            </select>

            <div style="margin-top: 20px; display: flex; gap: 10px;">
                <button type="submit" style="flex: 1; background: #1abc9c;">
                    ✅ ${personne != null ? 'Modifier' : 'Ajouter'}
                </button>
                <a href="${pageContext.request.contextPath}/personnes" 
                   style="flex: 1; background: #95a5a6; color: white; padding: 10px; text-align: center; border-radius: 4px; text-decoration: none; display: block; line-height: 1.5;">
                    ❌ Annuler
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>