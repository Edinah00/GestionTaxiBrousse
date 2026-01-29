<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/voyage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 

        <h2>Chiffre d'affaire par voyage</h2>

        <form method="get" action="${pageContext.request.contextPath}/ca" class="filter-form">
            <label for="mois">Mois :</label>
            <select name="mois" id="mois">
                <c:forEach begin="1" end="12" var="m">
                    <option value="${m}" <c:if test="${param.mois == m}">selected</c:if>>
                        ${m}
                    </option>
                </c:forEach>
            </select>

            <label for="annee">Année :</label>
            <select name="annee" id="annee">
                <c:forEach begin="2020" end="2030" var="a">
                    <option value="${a}" <c:if test="${param.annee == a}">selected</c:if>>
                        ${a}
                    </option>
                </c:forEach>
            </select>

            <button type="submit">Filtrer</button>
        </form>

        <table>
            <tr>
                <th>Gare routier départ</th>
                <th>Gare routier arrivé</th>
                <th>Vehicule</th>
                <th>Date départ</th>
                <th>Heure départ</th>
                <th>Montant généré par ticket vendu</th>
                <th>Montant généré par diffusion de publicité</th>
                <th>Montant CA total</th>
            </tr>

            <c:forEach items="${trajets}" var="trajet">
            <tr>
                <td>${trajet.gareDepart}</td>
                <td>${trajet.gareArrivee}</td>
                <td>${trajet.vehiculeImmatriculation}</td>
                <td>
                    <fmt:formatDate value="${trajet.dateHeureDepart}" pattern="dd/MM/yyyy" />
                </td>
                <td>
                    <fmt:formatDate value="${trajet.dateHeureDepart}" pattern="HH:mm" />
                </td>
                <td>
                    <fmt:formatNumber value="${trajet.caBillet}" pattern="#,##0.00" /> Ar
                </td>
                <td>
                    <fmt:formatNumber value="${trajet.caPub}" pattern="#,##0.00" /> Ar
                </td>
                <td>
                    <fmt:formatNumber value="${trajet.caTotal}" pattern="#,##0.00" /> Ar
                </td>
            </tr>
            </c:forEach>
        </table>
    </div>
</div>