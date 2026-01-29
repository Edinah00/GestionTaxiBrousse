<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 
        <h2>Rechercher voiture disponible</h2>

        <form method="post" action="search">
            Départ :
            <select name="depart" required>
                <c:forEach items="${departList}" var="d">
                    <option value="${d}">${d}</option>
                </c:forEach>
            </select>

            Arrivée :
            <select name="arrivee" required>
                <c:forEach items="${arriveeList}" var="a">
                    <option value="${a}">${a}</option>
                </c:forEach>
            </select>

            Date :
            <input type="date" name="date" required />

            <button type="submit">Rechercher</button>
        </form>

        <c:if test="${not empty taxis}">
            <h3>Résultats</h3>
            <table class="table-list">
                <tr>
                    <th>Immatriculation</th>
                    <th>Type voiture</th>
                    <th>Places disponibles</th>
                    <th>Numéros libres</th>
                    <th>Heure départ</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${taxis}" var="t">
                    <tr>
                        <td>${t.immatriculation}</td>
                        <td>${t.typeVoiture}</td>
                        <td>${t.placesDisponibles}</td>
                        <td>
                            <c:forEach items="${t.placesLibres}" var="p">
                                ${p} 
                            </c:forEach>
                        </td>
                        <td>${t.heureDepartStr}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/reservation/form?taxiTrajetId=${t.taxiTrajetId}">Réserver</a>
                            /
                            <a href="${pageContext.request.contextPath}/resa?taxiTrajetId=${t.taxiTrajetId}">RESA</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

    </div>
</div>
