-- ====================================
-- ÉTAPE 1 : NETTOYAGE COMPLET
-- ====================================
-- Suppression en cascade (ordre inverse des dépendances)
TRUNCATE TABLE paiement CASCADE;
TRUNCATE TABLE reservation_place CASCADE;
TRUNCATE TABLE reservation CASCADE;
TRUNCATE TABLE taxi_trajet CASCADE;
TRUNCATE TABLE trajet CASCADE;
TRUNCATE TABLE taxi_brousse CASCADE;
TRUNCATE TABLE type_voiture CASCADE;
TRUNCATE TABLE personne CASCADE;
TRUNCATE TABLE depense CASCADE;
TRUNCATE TABLE cooperative CASCADE;

-- Réinitialisation des séquences
ALTER SEQUENCE cooperative_id_seq RESTART WITH 1;
ALTER SEQUENCE personne_id_seq RESTART WITH 1;
ALTER SEQUENCE type_voiture_id_seq RESTART WITH 1;
ALTER SEQUENCE taxi_brousse_id_seq RESTART WITH 1;
ALTER SEQUENCE trajet_id_seq RESTART WITH 1;
ALTER SEQUENCE taxi_trajet_id_seq RESTART WITH 1;
ALTER SEQUENCE reservation_id_seq RESTART WITH 1;
ALTER SEQUENCE reservation_place_id_seq RESTART WITH 1;
ALTER SEQUENCE paiement_id_seq RESTART WITH 1;
ALTER SEQUENCE depense_id_seq RESTART WITH 1;

-- ====================================
-- ÉTAPE 2 : RÉINSERTION DES DONNÉES
-- ====================================

-- COOPERATIVE
INSERT INTO cooperative (nom) VALUES
('Cooperative Taxi Express');

-- PERSONNE
INSERT INTO personne (nom, telephone, roles) VALUES
('Rakoto Jean', '0341234567', 'CHAUFFEUR'),
('Rabe Hery', '0329876543', 'CHAUFFEUR'),
('Andrianina Lala', '0331122334', 'AIDE CHAUFFEUR'),
('Rasolondraibe Fanja', '0345566778', 'AIDE CHAUFFEUR'),
('Rasolofo Harizo', '0324455667', 'GUICHETIER'),
('Rakotondramanana Aina', '0337788990', 'CAISSIER'),
('Rabe Mamy', '0321122334', 'RESP PLANNING'),
('Andrianina Fidy', '0349988776', 'MECANICIEN'),
('Rakotomalala Hery', '0333344556', 'COMPTABLE'),
('Raharimalala Lala', '0346677889', 'DIRECTEUR'),
('Rasolofonirina Fetra', '0322233445', 'AGENT COMMERCIAL');

-- TYPE VOITURE (ATTENTION: colonne = pourcentage_auUNIQUEgmentation)
INSERT INTO type_voiture (libelle, nbr_places, poids_max_bagage, conso_carburant, tarif_bagage, nb_places_premium, nb_places_vip, pourcentage_auUNIQUEgmentation) VALUES
('Sprinter', 15, 10, 25, 5000, 6, 2, 0),
('Mazda', 12, 20, 8, 4000, 3, 2, 0);

-- TAXI BROUSSE
INSERT INTO taxi_brousse (cooperative_id, immatriculation, type_voiture_id) VALUES
(1, '1234 TBS', 1),
(1, '5678 TAU', 2),
(1, '9101 TAF', 1);

-- TRAJET
INSERT INTO trajet (depart, arrivee, distance_km, prix_base, pourcentage_augmentation, nombre_jour, prix_premium, prix_vip) VALUES
('Antananarivo', 'Toamasina', 350, 90000, 0, 1, 140000, 180000),
('Antananarivo', 'Antsirabe', 170, 40000, 0, 1, 70000, 90000),
('Antananarivo', 'Fianarantsoa', 410, 90000, 0, 1, 160000, 200000);

-- TAXI_TRAJET
INSERT INTO taxi_trajet (taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart) VALUES
(1, 1, 1, NULL, '2026-01-20 06:00:00'),
(2, 2, 2, 4, '2026-01-10 09:00:00'),
(3, 3, 1, 3, '2026-01-11 07:30:00');

-- RESERVATION
INSERT INTO reservation (taxi_trajet_id, nom_client, telephone, nb_places, date_reservation, statut) VALUES
(1, 'Ando Mamy', '0321122334', 2, '2026-01-09 10:00:00', 'RESERVATION'),
(1, 'Lala Hery', '0349988776', 1, '2026-01-09 11:30:00', 'ATTENTE'),
(2, 'Fetra Jean', '0323344556', 3, '2026-01-09 12:00:00', 'RESERVATION');

-- RESERVATION_PLACE
INSERT INTO reservation_place (taxi_trajet_id, reservation_id, numero_place) VALUES
(1, 1, 1),
(1, 1, 2),
(1, 2, 3),
(2, 3, 1),
(2, 3, 2),
(2, 3, 3);

-- PAIEMENT
INSERT INTO paiement (reservation_id, type_paiement, mode_paiement, montant, date_paiement) VALUES
(1, 'TOTAL RESERVATION', 'ESPECE', 160000, '2026-01-09 10:15:00'),
(3, 'ACOMPTE', 'MOBILE MONEY', 50000, '2026-01-09 12:30:00');

-- DEPENSE
INSERT INTO depense (cooperative_id, type, montant, date_depense) VALUES
(1, 'CARBURANT', 150000, '2026-01-08'),
(1, 'REPARATION', 300000, '2026-01-07'),
(1, 'VISITE TECHNIQUE', 50000, '2026-01-06'),
(1, 'SALAIRE', 800000, '2026-01-05');

-- ====================================
-- ÉTAPE 3 : VÉRIFICATION
-- ====================================
SELECT 'Vérification des insertions' AS titre;

SELECT 'cooperative' AS table_name, COUNT(*) AS count FROM cooperative
UNION ALL SELECT 'personne', COUNT(*) FROM personne
UNION ALL SELECT 'type_voiture', COUNT(*) FROM type_voiture
UNION ALL SELECT 'taxi_brousse', COUNT(*) FROM taxi_brousse
UNION ALL SELECT 'trajet', COUNT(*) FROM trajet
UNION ALL SELECT 'taxi_trajet', COUNT(*) FROM taxi_trajet
UNION ALL SELECT 'reservation', COUNT(*) FROM reservation
UNION ALL SELECT 'reservation_place', COUNT(*) FROM reservation_place
UNION ALL SELECT 'paiement', COUNT(*) FROM paiement
UNION ALL SELECT 'depense', COUNT(*) FROM depense;