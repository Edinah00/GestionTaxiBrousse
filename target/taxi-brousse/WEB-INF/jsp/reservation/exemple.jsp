<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 
            <h2>Nouvelle réservation</h2>

            <p style="color: red;">${erreur}</p>

            <form action="${pageContext.request.contextPath}/resa" method="post">

                <input type="hidden" name="taxiTrajetId" value="${taxiTrajet.id}" />

                <fieldset>
                    <legend>Client</legend>
                    <label>Nom :</label>
                    <input type="text" name="nomClient" required />
                    <label>Téléphone :</label>
                    <input type="text" name="telephone" />
                </fieldset>

                <fieldset>
                    <legend>Places réservées</legend>
                    <div id="placesContainer">
                        <!-- Ligne template -->
                        <div class="placeRow">
                            <label>Numéro place:</label>
                            <input type="number" name="numeroPlace[]" min="1" required />

                            <label>Catégorie:</label>
                            <select name="categoriePlaceId[]" required>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.id}">${cat.libelle}</option>
                                </c:forEach>
                            </select>

                            <label>Type passager:</label>
                            <select name="typePassagerId[]" required>
                                <c:forEach items="${typesPassager}" var="tp">
                                    <option value="${tp.id}">${tp.libelle}</option>
                                </c:forEach>
                            </select>

                            <button type="button" class="removeRow">Supprimer</button>
                        </div>
                    </div>

                    <button type="button" id="addRow">Ajouter une place</button>
                </fieldset>

                <button type="submit">Enregistrer réservation</button>
            </form>
    </div>
</div>

<script>
    const container = document.getElementById('placesContainer');
    const addBtn = document.getElementById('addRow');

    addBtn.addEventListener('click', () => {
        // Clone la première ligne
        const newRow = container.querySelector('.placeRow').cloneNode(true);

        // Reset les valeurs des champs
        newRow.querySelectorAll('input, select').forEach(el => el.value = '');

        container.appendChild(newRow);
        attachRemove(newRow);
    });

    function attachRemove(row) {
        row.querySelector('.removeRow').addEventListener('click', () => {
            if (container.querySelectorAll('.placeRow').length > 1) {
                row.remove();
            } else {
                alert("Au moins une place doit être renseignée.");
            }
        });
    }

    // Attache le bouton supprimer de la ligne initiale
    attachRemove(container.querySelector('.placeRow'));
</script>
