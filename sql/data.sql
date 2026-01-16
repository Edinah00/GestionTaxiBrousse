-- ============================================
-- SCRIPT D'INSERTION DES DONNÉES DE TEST
-- Taxi Brousse - Base de données
-- ============================================

-- Nettoyage des données existantes
TRUNCATE TABLE paiement, reservation_place, reservation, taxi_trajet, 
               trajet, taxi_brousse, type_voiture, personne, cooperative, depense 
RESTART IDENTITY CASCADE;

-- =========================
-- 1. COOPERATIVE
-- =========================
INSERT INTO cooperative (nom) VALUES 
('Coopérative COTISSE'),
('Coopérative SOATRANS'),
('Coopérative FIANARANTSOA EXPRESS');

-- =========================
-- 2. PERSONNE (Chauffeurs, Aides, etc.)
-- =========================
INSERT INTO personne (nom, telephone, roles) VALUES 
-- Chauffeurs
('Rakoto Jean', '034 11 222 33', 'CHAUFFEUR'),
('Rabe Paul', '033 44 555 66', 'CHAUFFEUR'),
('Rasoa Marie', '032 77 888 99', 'CHAUFFEUR'),
('Andry Luc', '034 12 345 67', 'CHAUFFEUR'),
('Feno Michel', '033 98 765 43', 'CHAUFFEUR'),
-- Aides chauffeurs
('Hery Claude', '032 11 222 44', 'AIDE CHAUFFEUR'),
('Nivo José', '034 55 666 77', 'AIDE CHAUFFEUR'),
('Tsiky Armand', '033 88 999 00', 'AIDE CHAUFFEUR'),
-- Personnel administratif
('Miora Sarah', '034 22 333 44', 'GUICHETIER'),
('Vonjy Pierre', '032 55 666 77', 'CAISSIER'),
('Lalao Sophie', '033 44 555 88', 'RESP PLANNING'),
('Toky David', '034 77 888 99', 'MECANICIEN'),
('Hanta Julie', '032 99 000 11', 'COMPTABLE'),
('Rivo Martin', '033 11 222 55', 'DIRECTEUR');

-- =========================
-- 3. TYPE VOITURE (AVEC pourcentage_auUNIQUEgmentation)
-- =========================
INSERT INTO type_voiture (libelle, pourcentage_auUNIQUEgmentation, nbr_places, poids_max_bagage, 
                          conso_carburant, tarif_bagage, nb_places_premium, nb_places_vip) 
VALUES 
-- Type 1: Minibus avec places premium et VIP
('Minibus 18 places', 0, 10, 20, 10, 5000, 6, 2),
-- Type 2: Sprinter
('Sprinter 12 places', 0, 12, 25, 12, 6000, 2, 1),
-- Type 3: Toyota Hiace
('Toyota Hiace 10 places', 0, 10, 15, 8, 4000, 0, 0),
-- Type 4: Minibus VIP
('Minibus VIP 8 places', 10, 8, 30, 15, 8000, 2, 3);

-- =========================
-- 4. TAXI BROUSSE
-- =========================
INSERT INTO taxi_brousse (cooperative_id, immatriculation, type_voiture_id) VALUES 
(1, '1234 TAB', 1),
(1, '5678 TAC', 2),
(2, '9012 TAD', 3),
(2, '3456 TAE', 1),
(3, '7890 TAF', 4),
(1, '1111 TAG', 2),
(3, '2222 TAH', 3);

-- =========================
-- 5. TRAJET
-- =========================
INSERT INTO trajet (depart, arrivee, distance_km, prix_base, pourcentage_augmentation, 
                    nombre_jour, prix_premium, prix_vip, prix_enfant) 
VALUES 
-- Tana - Tamatave (Image 1: remise enfant 50 000 au lieu de 90 000)
('Tana', 'Tamatave', 350, 90000, 0, 1, 140000, 180000, 50000),
-- Autres trajets
('Tana', 'Antsirabe', 169, 35000, 0, 1, 45000, 60000, 20000),
('Tana', 'Fianarantsoa', 409, 80000, 0, 2, 100000, 130000, 45000),
('Tana', 'Mahajanga', 570, 120000, 10, 2, 150000, 200000, 70000),
('Tana', 'Tuléar', 936, 180000, 15, 3, 220000, 280000, 100000),
('Antsirabe', 'Fianarantsoa', 240, 50000, 0, 1, 65000, 85000, 30000);

-- =========================
-- 6. TAXI_TRAJET (Voyages programmés)
-- =========================
INSERT INTO taxi_trajet (taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart) 
VALUES 
-- Voyage 1: Tana - Tamatave (Image 1)
(1, 1, 1, 6, '2026-01-20 06:00:00'),
-- Autres voyages
(2, 2, 2, NULL, '2026-01-18 08:00:00'),
(3, 3, 4, 8, '2026-01-19 05:30:00'),
(4, 4, 5, 6, '2026-01-22 06:00:00'),
(6, 5, 1, 7, '2026-01-23 04:00:00'),
(7, 6, 2, NULL, '2026-01-24 09:00:00');

-- =========================
-- 7. RESERVATION
-- =========================
INSERT INTO reservation (taxi_trajet_id, nom_client, telephone, nb_places, nb_enfants, 
                         date_reservation, statut) 
VALUES 
-- Réservations pour Voyage 1: Tana - Tamatave
(1, 'Rajaona Marc', '034 12 345 67', 2, 0, '2026-01-15 10:30:00', 'RESERVATION'),
(1, 'Andriamihaja Luc', '033 98 765 43', 1, 1, '2026-01-16 14:20:00', 'RESERVATION'),
(1, 'Rakotomalala Sophie', '032 55 666 77', 3, 2, '2026-01-17 09:15:00', 'RESERVATION'),
-- Autres réservations
(2, 'Rabearivelo Jean', '033 77 888 99', 2, 0, '2026-01-16 08:00:00', 'ATTENTE'),
(3, 'Randrianarison Paul', '034 22 333 44', 4, 1, '2026-01-17 16:45:00', 'FIL'),
(4, 'Rasolofo Hanta', '032 99 000 11', 1, 0, '2026-01-20 12:00:00', 'RESERVATION');

-- =========================
-- 8. RESERVATION_PLACE
-- =========================
INSERT INTO reservation_place (taxi_trajet_id, reservation_id, numero_place, type_place) 
VALUES 
-- Voyage 1, Réservation 1 (Marc - 2 adultes)
(1, 1, 1, 'STANDARD'),
(1, 1, 2, 'STANDARD'),
-- Voyage 1, Réservation 2 (Luc - 1 adulte + 1 enfant)
(1, 2, 3, 'STANDARD'),
(1, 2, 4, 'STANDARD'),
-- Voyage 1, Réservation 3 (Sophie - 3 adultes + 2 enfants)
(1, 3, 5, 'STANDARD'),
(1, 3, 6, 'STANDARD'),
(1, 3, 7, 'STANDARD'),
(1, 3, 8, 'STANDARD'),
(1, 3, 9, 'STANDARD'),
-- Voyage 2
(2, 4, 1, 'STANDARD'),
(2, 4, 2, 'STANDARD'),
-- Voyage 3
(3, 5, 1, 'STANDARD'),
(3, 5, 2, 'STANDARD'),
(3, 5, 3, 'STANDARD'),
(3, 5, 4, 'STANDARD'),
(3, 5, 5, 'STANDARD'),
-- Voyage 4
(4, 6, 1, 'VIP');

-- =========================
-- 9. PAIEMENT
-- =========================
INSERT INTO paiement (reservation_id, type_paiement, mode_paiement, montant, date_paiement) 
VALUES 
-- Réservation 1: Marc (2 adultes × 90 000 = 180 000)
(1, 'TOTAL RESERVATION', 'ESPECE', 180000, '2026-01-15 10:35:00'),
-- Réservation 2: Luc (1 adulte 90 000 + 1 enfant 50 000 = 140 000, acompte 70 000)
(2, 'ACOMPTE', 'MOBILE MONEY', 70000, '2026-01-16 14:25:00'),
-- Réservation 3: Sophie (3 adultes 270 000 + 2 enfants 100 000 = 370 000)
(3, 'TOTAL RESERVATION', 'ESPECE', 370000, '2026-01-17 09:20:00'),
-- Réservation 4
(4, 'ACOMPTE', 'ESPECE', 35000, '2026-01-16 08:05:00'),
-- Réservation 5
(5, 'TOTAL RESERVATION', 'MOBILE MONEY', 365000, '2026-01-17 16:50:00'),
-- Réservation 6
(6, 'TOTAL ARRIVEE', 'ESPECE', 200000, '2026-01-22 18:00:00');

-- =========================
-- 10. DEPENSE
-- =========================
INSERT INTO depense (cooperative_id, type, montant, date_depense) 
VALUES 
(1, 'CARBURANT', 450000, '2026-01-15'),
(1, 'REPARATION', 350000, '2026-01-16'),
(2, 'CARBURANT', 380000, '2026-01-15'),
(2, 'VISITE TECHNIQUE', 180000, '2026-01-10'),
(3, 'SALAIRE', 2500000, '2026-01-01'),
(1, 'SALAIRE', 3200000, '2026-01-01'),
(2, 'CARBURANT', 420000, '2026-01-17'),
(3, 'REPARATION', 580000, '2026-01-12');
