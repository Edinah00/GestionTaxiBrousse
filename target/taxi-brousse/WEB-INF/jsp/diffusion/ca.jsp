<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">

<style>
    .cards {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 16px;
        margin-top: 20px;
    }

    .card {
        background: #fff;
        border-radius: 12px;
        padding: 16px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.08);
        transition: transform 0.2s;
    }

    .card:hover {
        transform: translateY(-3px);
    }

    .card-title {
        font-size: 14px;
        color: #666;
        margin-bottom: 8px;
    }

    .card-value {
        font-size: 24px;
        font-weight: bold;
        color: #222;
    }

    .card.money .card-value {
        color: #1a8f3c;
    }

    .card.warning .card-value {
        color: #d97706;
    }

    .card.danger .card-value {
        color: #dc2626;
    }
</style>

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 

        <h2>🎬 Diffusion de vidéos publicitaires</h2>

        <form method="post" action="diff">
            Date :
            <input type="date" name="date" required />

            Société :
            <select name="societe" required>
                <option value="">-- Sélectionnez une société --</option>
                <c:forEach var="s" items="${societes}">
                    <option value="${s.id}">${s.nom}</option>
                </c:forEach>
            </select>

            <button type="submit">Rechercher</button>
        </form>

        <c:if test="${not empty diffusions}">
            <h3 style="margin-top:20px;">
                Résultats — ${date} | ${soc.nom}
            </h3>

            <div class="cards">
                <div class="card">
                    <div class="card-title">Nombre de diffusions</div>
                    <div class="card-value">
                        ${diffusions.totalDiffusions}
                    </div>
                </div>

                <div class="card money">
                    <div class="card-title">Chiffre d'affaires</div>
                    <div class="card-value">
                        ${diffusions.CA} Ar
                    </div>
                </div>

                <div class="card warning">
                    <div class="card-title">Déjà payé</div>
                    <div class="card-value">
                        ${dejaPaye} Ar
                    </div>
                </div>

                <div class="card danger">
                    <div class="card-title">Reste à payer</div>
                    <div class="card-value">
                        ${reste} Ar
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>
