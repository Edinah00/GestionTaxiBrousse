<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">

<div class="container">

       <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

       <div class="content">

              <h2>Taxi Brousse</h2>

              <form method="post" action="taxi">
              <input type="hidden" name="id" value="${taxi.id}"/>

              Cooperative ID :
              <input type="number" name="cooperativeId" value="${taxi.cooperativeId}" required /><br/>

              Type Voiture ID :
              <input type="number" name="typeVoitureId" value="${taxi.typeVoitureId}" required /><br/>

              Immatriculation :
              <input type="text" name="immatriculation" value="${taxi.immatriculation}" required /><br/>

              <button type="submit">Enregistrer</button>
              </form>

       </div>
</div>
