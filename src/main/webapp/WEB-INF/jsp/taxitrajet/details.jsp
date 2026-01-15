<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.coop.model.TaxiTrajet" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des Taxi-Trajets</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; margin: 0; padding: 20px; }
        h1 { color: #2c3e50; }
        a.btn { padding: 8px 12px; border-radius: 4px; text-decoration: none; margin: 5px; }
        .btn-primary { background: #3498db; color: #fff;max-height: 50px;}
        .btn-warning { background: #f39c12; color: #fff; }
        .btn-danger { background: #e74c3c; color: #fff; }
        table { width: 100%; border-collapse: collapse; margin-top: 15px; background: #fff; }
        th, td { padding: 10px; border-bottom: 1px solid #ddd; }
        th { background: #3498db; color: #fff; }
        tr:hover { background: #f1f1f1; }
    </style>
</head>
<body>
    <div class="container">

    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />
     <div class="content">
        <% double[] prix = (double[]) request.getAttribute("prix");
           int[] nbPlace = (int[]) request.getAttribute("nbPlace");
           %>
    <h1>Details Valeurs Max</h1>
   
    <table>
        <tr>
            <th>Nombres place premium </th><th>Prix premium </th><th>Nombres place Standart </th><th>Prix Standart</th>
        </tr>
      
        <tr>
            <td><%= nbPlace[1] %></td>
            <td><%= prix[1] %></td>
            <td><%= nbPlace[0] %></td>
            <td><%= prix[0] %></td>
                 </tr>

        
    </table>
    <h1> Valeur Max = <% (nbPlace[1] * prix[1]) + (nbPlace[0] * prix[0]) %></h1>
         </div>
        </div>

</body>
</html>
