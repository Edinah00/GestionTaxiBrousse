<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/voyage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">

<div class="container">
    <div class="content">
        <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" /> 

        <h2>Chiffre d'affaire par voyage</h2>

        <!-- SECTION 1 : Tableau des voyages -->
        <h3>Détails par voyage</h3>
        <table>
            <tr>
                <th>Gare routier départ</th>
                <th>Gare routier arrivé</th>
                <th>Vehicule</th>
                <th>Date départ</th>
                <th>Heure départ</th>
                <th>CA Billet</th>
                <th>CA Pub Total</th>
                <th>CA Pub Payé</th>
                <th>CA Pub Reste</th>
                <th>CA Total</th>
            </tr>

            <c:forEach items="${trajets}" var="tt">
            <tr>
                <td>${tt.trajet.depart}</td>
                <td>${tt.trajet.arrivee}</td>
                <td>${tt.taxi.immatriculation}</td>
                <td>
                    <fmt:parseDate value="${tt.dateHeureDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                </td>
                <td>
                    <fmt:parseDate value="${tt.dateHeureDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedTime" type="both" />
                    <fmt:formatDate value="${parsedTime}" pattern="HH:mm" />
                </td>
                <td>
                    <fmt:formatNumber value="${tt.caBillet}" pattern="#,##0.00" /> Ar
                </td>
                <td>
                    <fmt:formatNumber value="${tt.caPub}" pattern="#,##0.00" /> Ar
                </td>
                <td style="color: green;">
                    <fmt:formatNumber value="${tt.caPubPaye}" pattern="#,##0.00" /> Ar
                </td>
                <td style="color: red;">
                    <fmt:formatNumber value="${tt.caPubReste}" pattern="#,##0.00" /> Ar
                </td>
                <td style="font-weight: bold;">
                    <fmt:formatNumber value="${tt.caTotal}" pattern="#,##0.00" /> Ar
                </td>
            </tr>
            </c:forEach>
        </table>

        <!-- SECTION 2 : Résumé par société -->
        <h3 style="margin-top: 40px;">Résumé des paiements par société</h3>
        <table>
            <tr>
                <th>Société</th>
                <th>Montant Total Facturé</th>
                <th>Montant Payé</th>
                <th>Reste à Payer</th>
                <th>Nb Factures</th>
                <th>Statut</th>
            </tr>

            <c:forEach items="${societes}" var="societe">
            <tr>
                <td>${societe.societeNom}</td>
                <td>
                    <fmt:formatNumber value="${societe.montantTotal}" pattern="#,##0.00" /> Ar
                </td>
                <td style="color: green;">
                    <fmt:formatNumber value="${societe.montantPaye}" pattern="#,##0.00" /> Ar
                </td>
                <td style="color: red; font-weight: bold;">
                    <fmt:formatNumber value="${societe.montantRestant}" pattern="#,##0.00" /> Ar
                </td>
                <td>${societe.nbFactures}</td>
                <td>
                    <c:choose>
                        <c:when test="${societe.statut == 'PAYEE'}">
                            <span style="color: green;">✓ Payée</span>
                        </c:when>
                        <c:when test="${societe.statut == 'EN_COURS'}">
                            <span style="color: orange;">⏳ En cours</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">✗ Impayé</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            </c:forEach>
        </table>

        <!-- SECTION 3 : Totaux généraux (optionnel) -->
        <div style="margin-top: 30px; padding: 20px; background-color: #f0f0f0; border-radius: 5px;">
            <h3>Totaux généraux</h3>
            <c:set var="totalBillet" value="0" />
            <c:set var="totalPub" value="0" />
            <c:set var="totalPubPaye" value="0" />
            <c:set var="totalPubReste" value="0" />
            
            <c:forEach items="${trajets}" var="tt">
                <c:set var="totalBillet" value="${totalBillet + tt.caBillet}" />
                <c:set var="totalPub" value="${totalPub + tt.caPub}" />
                <c:set var="totalPubPaye" value="${totalPubPaye + tt.caPubPaye}" />
                <c:set var="totalPubReste" value="${totalPubReste + tt.caPubReste}" />
            </c:forEach>
            
            <p><strong>CA Total Billets :</strong> <fmt:formatNumber value="${totalBillet}" pattern="#,##0.00" /> Ar</p>
            <p><strong>CA Total Publicités :</strong> <fmt:formatNumber value="${totalPub}" pattern="#,##0.00" /> Ar</p>
            <p style="color: green;"><strong>Publicités Payées :</strong> <fmt:formatNumber value="${totalPubPaye}" pattern="#,##0.00" /> Ar</p>
            <p style="color: red;"><strong>Publicités à Encaisser :</strong> <fmt:formatNumber value="${totalPubReste}" pattern="#,##0.00" /> Ar</p>
            <p style="font-size: 1.2em; font-weight: bold;"><strong>CA TOTAL GÉNÉRAL :</strong> <fmt:formatNumber value="${totalBillet + totalPub}" pattern="#,##0.00" /> Ar</p>
        </div>
    </div>
</div>