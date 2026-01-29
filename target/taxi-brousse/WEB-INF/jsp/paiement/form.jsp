<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 
        <h2>Paiement réservation</h2>

        <form method="post">

            <input type="hidden" name="reservationId"
                value="${reservation.id}" />

            <p>
                <strong>Client :</strong>
                ${reservation.nomClient}
            </p>

            <p>
                <strong>Taxi :</strong>
                ${reservation.taxiTrajet.taxi.immatriculation}
            </p>

            <br/>

            <label>Mode paiement</label>
            <select name="modePaiement" required>
                <option value="ESPECE">Espèce</option>
                <option value="MOBILE MONEY">Mobile Money</option>
            </select>

            <br/>

            <label>Montant</label>
            <input type="number" step="0.01" name="montant"
                value="${montant}" required />

            <br/><br/>

            <button type="submit">Valider paiement</button>

        </form>
    </div>
</div>