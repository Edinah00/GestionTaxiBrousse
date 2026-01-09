<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">

<html>
<body>
<h2>Liste des personnes</h2>
<table border="1">
<tr><th>ID</th><th>Nom</th><th>Téléphone</th><th>Rôle</th></tr>
<c:forEach var="p" items="${personnes}">
    <tr>
        <td>${p.id}</td>
        <td>${p.nom}</td>
        <td>${p.telephone}</td>
        <td>${p.role}</td>
    </tr>
</c:forEach>
</table>

<h3>Ajouter une personne</h3>
<form method="post" action="personnes">
    Nom: <input type="text" name="nom"><br>
    Téléphone: <input type="text" name="telephone"><br>
    Rôle: <input type="text" name="role"><br>
    <button type="submit">Ajouter</button>
</form>
</body>
</html>
