<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/list-resa.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 
            <h2>Liste des réservations</h2>

            <form method="get">
                Date de départ :
                <input type="date" name="dateDepart" value="${param.dateDepart}" />

                Voyage :
                <select name="taxiTrajetId">
                    <option value="">-- Tous --</option>
                    <c:forEach items="${taxisTrajets}" var="tt">
                        <option value="${tt.id}"
                            <c:if test="${param.taxiTrajetId == tt.id}">selected</c:if>>
                            ${tt.trajet.depart} → ${tt.trajet.arrivee}
                            (${tt.dateHeureDepart})
                        </option>
                    </c:forEach>
                </select>

                <button type="submit">Rechercher</button>
            </form>

            <c:if test="${not empty chiffreAffaires}">
                <h3 style="margin-top:20px;">
                    💰 Chiffre d'affaires :
                    <strong>${chiffreAffaires} Ar</strong>
                </h3>
            </c:if> ${param.nom}

            <table>
                <tr>
                    <th>ID</th>
                    <th>Client</th>
                    <th>Taxi</th>
                    <th>Voyage</th>
                    <th>Nb places</th>
                    <%-- <th>Etat de paiement</th> --%>
                </tr>
                <c:forEach items="${reservations}" var="r">
                    <tr>
                        <td>${r.id}</td>
                        <td>${r.nomClient}</td>
                        <td>${r.taxiTrajet.taxi.immatriculation}</td>
                        <td>${r.taxiTrajet.trajet.depart} - ${r.taxiTrajet.trajet.arrivee} ${r.taxiTrajet.dateHeureDepart}</td>
                        <td>${r.nbPlaces}</td>
                        <%-- <td>${r.etat}</td> --%>
                    </tr>
                </c:forEach>
            </table>          
    </div>
</div>