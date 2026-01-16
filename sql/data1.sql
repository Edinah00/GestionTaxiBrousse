-- Nettoyage des données existantes
TRUNCATE TABLE paiement, reservation_place, reservation, taxi_trajet, 
               trajet, taxi_brousse, type_voiture, personne, cooperative, depense 
RESTART IDENTITY CASCADE;

INSERT INTO cooperative (nom) VALUES 
('Coopérative FIANARANTSOA EXPRESS');

-- =========================
-- 2. PERSONNE (Chauffeurs, Aides, etc.)
-- =========================
INSERT INTO personne (nom, telephone, roles) VALUES 
('Rakoto Jean', '034 11 222 33', 'CHAUFFEUR');

INSERT INTO type_voiture (libelle,nbr_places, nb_places_premium, nb_places_vip) 
VALUES 
-- Type 1: Minibus avec places premium et VIP
('Minibus', 10, 6, 2);

INSERT INTO taxi_brousse (cooperative_id, immatriculation, type_voiture_id) VALUES 
(1, '1234 TAB', 1);

INSERT INTO trajet (depart, arrivee, prix_base,
                     prix_premium, prix_vip, prix_enfant) 
VALUES 
-- Tana - Tamatave (Image 1: remise enfant 50 000 au lieu de 90 000)
('Tana', 'Tamatave', 90000, 140000, 180000, 50000);

INSERT INTO taxi_trajet (taxi_id, trajet_id, chauffeur_id, date_heure_depart) VALUES 
(1, 1, 1, '2024-07-01 08:00:00');

INSERT INTO reservation (taxi_trajet_id, nom_client, telephone, nb_places, nb_enfants, 
                         date_reservation, statut) 
VALUES 
-- Réservations pour Voyage 1: Tana - Tamatave
(1, 'Rakotomalala Sophie', '032 55 666 77', 3, 2, '2026-01-17 09:15:00', 'RESERVATION');

INSERT INTO reservation_place (taxi_trajet_id, reservation_id, numero_place, type_place) 
VALUES 
-- Voyage 1, Réservation 1 (Sophie - 2 adultes + 2 enfants)
(1, 1, 5, 'VIP'),
(1, 1, 6, 'STANDARD'),
(1, 1, 7, 'STANDARD'),
(1, 1, 8, 'STANDARD'),
(1, 1, 9, 'STANDARD');

INSERT INTO paiement (reservation_id, type_paiement, mode_paiement, montant, date_paiement) 
VALUES 
(1, 'TOTAL RESERVATION', 'ESPECE', 460000, '2026-01-17 09:20:00');

INSERT INTO depense (cooperative_id, type, montant, date_depense) 
VALUES 
(1, 'CARBURANT', 450000, '2026-01-15'),
(1, 'REPARATION', 350000, '2026-01-16');