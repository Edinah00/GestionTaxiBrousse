
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${trajet != null ? 'Modifier' : 'Ajouter'} un Trajet</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <style>
        .content {
            flex: 1;
            padding: 30px;
        }

        .form-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .form-header {
            margin-bottom: 30px;
        }

        .form-header h1 {
            margin: 0;
            color: #2c3e50;
        }

        .form-header p {
            margin: 10px 0 0 0;
            color: #7f8c8d;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #2c3e50;
        }

        .form-group label .required {
            color: #e74c3c;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            box-sizing: border-box;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #1abc9c;
            box-shadow: 0 0 0 3px rgba(26, 188, 156, 0.1);
        }

        .form-group small {
            display: block;
            margin-top: 5px;
            color: #7f8c8d;
            font-size: 13px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
            padding-top: 30px;
            border-top: 2px solid #ecf0f1;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background: #1abc9c;
            color: white;
            flex: 1;
        }

        .btn-primary:hover {
            background: #16a085;
        }

        .btn-secondary {
            background: #95a5a6;
            color: white;
            flex: 1;
        }

        .btn-secondary:hover {
            background: #7f8c8d;
        }

        .info-box {
            background: #e8f8f5;
            border-left: 4px solid #1abc9c;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 25px;
        }

        .info-box h4 {
            margin: 0 0 10px 0;
            color: #16a085;
        }

        .info-box ul {
            margin: 0;
            padding-left: 20px;
        }

        .info-box li {
            margin: 5px 0;
            color: #2c3e50;
        }
    </style>
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="content">
        <div class="form-container">
            <div class="form-header">
                <h1>
                    ${trajet != null ? '✏️ Modifier' : '➕ Ajouter'} un Trajet
                </h1>
                <p>
                    ${trajet != null ? 'Modifiez les informations du trajet' : 'Remplissez les informations du nouveau trajet'}
                </p>
            </div>

            <div class="info-box">
                <h4>ℹ️ Informations</h4>
                <ul>
                    <li>Les champs marqués d'une <span style="color: #e74c3c;">*</span> sont obligatoires</li>
                    <li>Le prix de base sera appliqué par passager</li>
                    <li>Le pourcentage d'augmentation s'applique sur le prix de base (ex: pour les périodes de fête)</li>
                </ul>
            </div>

            <form method="post" action="${pageContext.request.contextPath}/trajet">
                <input type="hidden" name="id" value="${trajet.id}">

                <!-- Départ et Arrivée -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="depart">
                            📍 Lieu de départ <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="depart" 
                               name="depart" 
                               value="${trajet.depart}"
                               placeholder="Ex: Tananarive"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="arrivee">
                            🎯 Lieu d'arrivée <span class="required">*</span>
                        </label>
                        <input type="text" 
                               id="arrivee" 
                               name="arrivee" 
                               value="${trajet.arrivee}"
                               placeholder="Ex: Toamasina"
                               required>
                    </div>
                </div>

                <!-- Distance -->
                <div class="form-group">
                    <label for="distance_km">
                        📏 Distance (km) <span class="required">*</span>
                    </label>
                    <input type="number" 
                           id="distance_km" 
                           name="distance_km" 
                           value="${trajet.distanceKm}"
                           placeholder="Ex: 350"
                           min="1"
                           required>
                    <small>La distance entre le lieu de départ et d'arrivée en kilomètres</small>
                </div>

                <!-- Prix et Augmentation -->
                <div class="form-row">
                    <div class="form-group">
                        <label for="prix_base">
                            💰 Prix de base (Ar) <span class="required">*</span>
                        </label>
                        <input type="number" 
                               id="prix_base" 
                               name="prix_base" 
                               value="${trajet.prixBase}"
                               placeholder="Ex: 60000"
                               step="0.01"
                               min="0"
                               required>
                        <small>Prix par passager en Ariary</small>
                    </div>

                    <div class="form-group">
                        <label for="pourcentage_augmentation">
                            📈 Augmentation (%)
                        </label>
                        <input type="number" 
                               id="pourcentage_augmentation" 
                               name="pourcentage_augmentation" 
                               value="${trajet.pourcentageAugmentation}"
                               placeholder="Ex: 10"
                               step="0.01"
                               min="0"
                               max="100">
                        <small>Pourcentage d'augmentation sur le prix de base (optionnel)</small>
                    </div>
                </div>

                <!-- Actions -->
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        ✅ ${trajet != null ? 'Enregistrer les modifications' : 'Ajouter le trajet'}
                    </button>
                    <a href="${pageContext.request.contextPath}/trajet?action=liste" 
                       class="btn btn-secondary">
                        ❌ Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
