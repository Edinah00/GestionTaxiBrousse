INSERT INTO cooperative (nom) VALUES
('Cotisse Transport');

INSERT INTO personne (nom, telephone, roles) VALUES
('Rakoto Jean', '0341234567', 'CHAUFFEUR'),
('Rabe Paul', '0339876543', 'AIDE CHAUFFEUR'),
('Andry Michel', '0324567890', 'CHAUFFEUR'),
('Solo Hery', '0345554443', 'GUICHETIER'),
('Rasoanaivo Marie', '0332221110', 'CAISSIER');

INSERT INTO type_voiture (libelle, nbr_places) VALUES
('Sprinter 15 places', 15),
('Coaster 29 places', 29);

INSERT INTO taxi_brousse (cooperative_id, immatriculation, type_voiture_id) VALUES
(1, '1234 TBA', 2),
(1, '5678 TBB', 2),
(1, '9012 TBC', 1);

INSERT INTO trajet (depart, arrivee, distance_km, prix_base, nombre_jour) VALUES
('Antananarivo', 'Toamasina', 350, 50000, 1),
('Antananarivo', 'Mahajanga', 560, 70000, 1.5),
('Antananarivo', 'Fianarantsoa', 410, 60000, 1);

INSERT INTO taxi_trajet (
    taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart
) VALUES
(1, 1, 1, 2, '2026-01-20 06:00'),
(2, 2, 3, 2, '2026-01-21 05:30'),
(3, 3, 1, 2, '2026-01-22 07:00');

-- INSERT INTO reservation (
--     taxi_trajet_id, nom_client, telephone, nb_places, statut, etat_paye
-- ) VALUES
-- (1, 'Client A', '0341110001', 20, 'RESERVATION', FALSE);

INSERT INTO categorie_place (libelle, prix) VALUES 
('STANDARD',90000),
('PREMIUM',140000),
('VIP',180000);

INSERT INTO config_place (id_categ_place, id_taxi_brousse, nbr) VALUES 
(1, 1, 10),
(2, 1, 6),
(3, 1, 2);

INSERT INTO type_passager (libelle) VALUES
('ENFANT'),
('ADULTE'),
('SENIOR');

INSERT INTO place_passager (id_categ_place, id_type_passager, prix) VALUES 
(1,1,40000),
(1,2,55000),
(1,3,44000),

(2,1,50000),
(2,2,60000),
(2,3,48000),

(3,1,65000),
(3,2,70000),
(3,3,56000);

-- INSERT INTO reservation_place (reservation_id, numero_place, id_place_passager) VALUES
-- -- enfant eco 2
-- (1, 1, 1),
-- (1, 2, 1),
-- -- adulte eco 4
-- (1, 3, 2),
-- (1, 4, 2),
-- (1, 5, 2),
-- (1, 6, 2),
-- -- senior eco 2
-- (1, 7, 3),
-- (1, 8, 3),

-- -- enfant premium 1
-- (1, 9, 4),
-- -- adulte premium 2
-- (1, 10, 5),
-- (1, 11, 5),
-- -- senior premium 1
-- (1, 12, 6),

-- -- enfant vip 2
-- (1, 13, 7),
-- (1, 14, 7),
-- -- adulte vip 4
-- (1, 15, 8),
-- (1, 16, 8),
-- (1, 17, 8),
-- (1, 18, 8),
-- -- senior vip 2
-- (1, 19, 9),
-- (1, 20, 9);

INSERT INTO taxi_trajet (
    taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart
) VALUES
(1, 1, 1, 2, '2025-12-01 06:00');

INSERT INTO depense (cooperative_id, type, montant, date_depense) VALUES
(1, 'CARBURANT', 300000, '2026-01-19'),
(1, 'SALAIRE', 500000, '2026-01-18'),
(1, 'REPARATION', 250000, '2026-01-17');

insert into societe (nom) values ('Vaniala'), ('Lewis');
insert into pub (idSociete, descri_pub, cout) values 
(1, 'Publicité pour Vaniala sur les taxis brousse', 100000),
(2, 'Publicité pour Lewis sur les taxis brousse', 100000);
insert into diffusion (idPub, idTaxiTrajet ,nb_diffusions) values 
(1, 4,20),
(2, 4,10);

insert into payment_diffusion (idSociete, montant, date_payment) values
(1, 1000000, '2025-12-15');

insert into payment_diffusion (idSociete, montant, date_payment) values
(1, 200000, '2025-12-15');


