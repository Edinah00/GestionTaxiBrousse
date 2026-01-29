<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/general.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/plan.css">

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <h2>Réservation de places</h2>

        <form method="post" action="form" id="reservationForm">
            <input type="hidden" name="taxiTrajetId" value="${taxiTrajet.id}">
            <input type="hidden" name="numeroPlace" id="numeroPlace">

            <p><strong>Taxi :</strong>
                ${taxiTrajet.taxi.immatriculation}
                (${taxiTrajet.taxi.typeVoiture.libelle})
            </p>

            <p><strong>Date / Heure :</strong>
                ${taxiTrajet.dateHeureDepart}
            </p>

            <h3>Client</h3>
            <label>Nom</label>
            <input type="text" name="nomClient" required>

            <label>Téléphone</label>
            <input type="text" name="telephone" required>

            <h3>Plan des places</h3>

            <!-- PLAN DYNAMIQUE -->
            <div class="bus">
                <div class="row">
                    <div class="chauffeur">CH</div>
                </div>

                <c:set var="cols" value="4"/>
                <c:forEach var="i" begin="1" end="${nbrPlaces}">
                    <c:if test="${(i - 1) % cols == 0}">
                        <div class="row">
                    </c:if>

                    <c:set var="isLibre" value="${placesLibres.contains(i)}"/>

                    <div class="seat
                        ${isLibre ? 'libre' : 'occupee'}"
                        data-seat="${i}">
                        ${i}
                    </div>

                    <c:if test="${i % cols == 0 || i == nbrPlaces}">
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <p>
                Places sélectionnées :
                <strong><span id="selectedCount">0</span></strong>
            </p>

            <button type="submit">Réserver</button>
        </form>
    </div>
</div>

<script>
let selectedSeats = [];

document.querySelectorAll(".seat.libre").forEach(seat => {
    seat.addEventListener("click", () => {
        const num = seat.dataset.seat;

        if (seat.classList.contains("selected")) {
            seat.classList.remove("selected");
            selectedSeats = selectedSeats.filter(s => s !== num);
        } else {
            seat.classList.add("selected");
            selectedSeats.push(num);
        }

        document.getElementById("numeroPlace").value = selectedSeats.join(",");
        document.getElementById("selectedCount").innerText = selectedSeats.length;
    });
});
</script>
