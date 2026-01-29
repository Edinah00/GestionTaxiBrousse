INSERT INTO cooperative (nom) VALUES
('Cotisse Transport');

INSERT INTO personne (nom, telephone, roles) VALUES
('Rakoto Jean', '0341234567', 'CHAUFFEUR'),
('Rabe Paul', '0339876543', 'AIDE CHAUFFEUR'),
('Andry Michel', '0324567890', 'CHAUFFEUR'),
('Solo Hery', '0345554443', 'GUICHETIER'),
('Rasoanaivo Marie', '0332221110', 'CAISSIER');

INSERT INTO type_voiture (libelle, nbr_places) VALUES
('Sprinter', 50);

INSERT INTO taxi_brousse (cooperative_id, immatriculation, type_voiture_id) VALUES
(1, '1244 TBK', 1);

INSERT INTO trajet (depart, arrivee, distance_km, prix_base, nombre_jour) VALUES
('Antananarivo', 'Toamasina', 350, 50000, 1);

INSERT INTO taxi_trajet (
    taxi_id, trajet_id, chauffeur_id, aide_chauffeur_id, date_heure_depart
) VALUES
(1, 1, 1, 2, '2026-01-20 10:00'),
(1, 1, 1, 2, '2026-01-21 10:00'),
(1, 1, 1, 2, '2026-01-21 15:00');

INSERT INTO categorie_place (libelle, prix) VALUES 
('STANDARD',90000),
('PREMIUM',140000),
('VIP',180000);

-- INSERT INTO config_place (id_categ_place, id_taxi_brousse, nbr) VALUES 
-- (1, 1, 10),
-- (2, 1, 6),
-- (3, 1, 2);

INSERT INTO type_passager (libelle) VALUES
('ENFANT'),
('ADULTE'),
('SENIOR');

INSERT INTO place_passager (id_categ_place, id_type_passager, prix) VALUES 
(1,1,40000),
(1,2,50000),

(2,1,50000),
(2,2,60000),

(3,1,65000),
(3,2,70000);

-- reservation place

insert into societe (nom) values ('Vaniala'), ('Lewis'), ('Socobis'), ('Jejoo');

insert into pub (idSociete, descri_pub, cout) values 
(1, 'Publicité pour Vaniala', 100000),
(2, 'Publicité pour Lewis', 100000),
(3, 'Publicité pour Socobis', 100000),
(4, 'Publicité pour Jejoo', 100000);

insert into diffusion (idPub, idTaxiTrajet ,nb_diffusions) values 
(1, 1, 1),
(2, 1, 1),

(3, 2, 2),
(4, 2, 1);

DO $$
DECLARE
    res_id INT;
    i INT;
    v_place_passager_id INT;
BEGIN
    -- ===============================
    -- Récupérer STANDARD + ADULTE
    -- ===============================
    SELECT id
    INTO v_place_passager_id
    FROM place_passager
    WHERE id_categ_place = (
        SELECT id FROM categorie_place WHERE libelle = 'STANDARD'
    )
    AND id_type_passager = (
        SELECT id FROM type_passager WHERE libelle = 'ADULTE'
    );

    IF v_place_passager_id IS NULL THEN
        RAISE EXCEPTION 'PlacePassager STANDARD + ADULTE introuvable';
    END IF;

    -- ===============================
    -- VOYAGE 1 → 1 réservation / 40 places
    -- ===============================
    INSERT INTO reservation (
        taxi_trajet_id,
        nom_client,
        telephone,
        nb_places,
        etat_paye
    ) VALUES (
        1,
        'Client Voyage 1',
        '0340000001',
        40,
        FALSE
    )
    RETURNING id INTO res_id;

    FOR i IN 1..40 LOOP
        INSERT INTO reservation_place (
            reservation_id,
            numero_place,
            id_place_passager
        ) VALUES (
            res_id,
            i,
            v_place_passager_id
        );
    END LOOP;

    -- ===============================
    -- VOYAGE 2 → 1 réservation / 30 places
    -- ===============================
    INSERT INTO reservation (
        taxi_trajet_id,
        nom_client,
        telephone,
        nb_places,
        etat_paye
    ) VALUES (
        2,
        'Client Voyage 2',
        '0340000002',
        30,
        FALSE
    )
    RETURNING id INTO res_id;

    FOR i IN 1..30 LOOP
        INSERT INTO reservation_place (
            reservation_id,
            numero_place,
            id_place_passager
        ) VALUES (
            res_id,
            i,
            v_place_passager_id
        );
    END LOOP;

    -- ===============================
    -- VOYAGE 3 → 1 réservation / 50 places
    -- ===============================
    INSERT INTO reservation (
        taxi_trajet_id,
        nom_client,
        telephone,
        nb_places,
        etat_paye
    ) VALUES (
        3,
        'Client Voyage 3',
        '0340000003',
        50,
        FALSE
    )
    RETURNING id INTO res_id;

    FOR i IN 1..50 LOOP
        INSERT INTO reservation_place (
            reservation_id,
            numero_place,
            id_place_passager
        ) VALUES (
            res_id,
            i,
            v_place_passager_id
        );
    END LOOP;

END $$;