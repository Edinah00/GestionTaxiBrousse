
CREATE DATABASE taxi_brousse;
\c taxi_brousse;

 
-- =========================
-- COOPERATIVE
-- =========================
CREATE TABLE cooperative (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

-- =========================
-- PERSONNE
-- =========================
CREATE TABLE personne (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    roles VARCHAR(30)
        CHECK (roles IN ('CHAUFFEUR','AIDE CHAUFFEUR','GUICHETIER','CAISSIER','RESP PLANNING','MECANICIEN','COMPTABLE','DIRECTEUR','AGENT COMMERCIAL'))
);

-- =========================
-- TYPE VOITURE
-- =========================
CREATE TABLE type_voiture (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL,
    pourcentage_auUNIQUEgmentation NUMERIC(5,2) DEFAULT 0,
    nbr_places INT NOT NULL CHECK (nbr_places > 0),
    poids_max_bagage NUMERIC(5,2) DEFAULT 20, -- kg par personne
    conso_carburant NUMERIC(5,2) DEFAULT 8, -- L/100km
    tarif_bagage NUMERIC(10,2) DEFAULT 5000,-- Ariary / kg refa le mihotra ny entana
    nb_places_premium INT,
    nb_places_vip INT
);
-- =========================
-- TAXI BROUSSE
-- =========================
CREATE TABLE taxi_brousse ( -- voiture 
    id SERIAL PRIMARY KEY,
    cooperative_id INT NOT NULL REFERENCES cooperative(id),
    immatriculation VARCHAR(20) UNIQUE NOT NULL,
    type_voiture_id INT NOT NULL REFERENCES type_voiture(id)
);


CREATE OR REPLACE VIEW view_voiture AS
SELECT 
    tb.id AS taxi_id,
    tb.immatriculation,
    tv.libelle AS type_voiture,
    tv.nbr_places,
    tv.poids_max_bagage,
    tv.conso_carburant,
    tv.tarif_bagage,
    tb.cooperative_id
FROM 
    taxi_brousse tb
JOIN 
    type_voiture tv ON tb.type_voiture_id = tv.id;


-- =========================
-- TRAJET
-- =========================
CREATE TABLE trajet ( -- lieu depart sy ny arrive , 
    id SERIAL PRIMARY KEY,
    depart VARCHAR(100) NOT NULL,
    arrivee VARCHAR(100) NOT NULL,
    distance_km INT, -- refa fantatra ny distance dia azo kajiana ny conso carburant
    prix_base NUMERIC(10,2),
    pourcentage_augmentation NUMERIC(5,2) ,-- refa miakatra ny vidiny amin'ny fotoana sasany le fety ohatra
    nombre_jour INT,
    prix_premium NUMERIC(10,2),
    prix_vip NUMERIC(10,2)
);
-- =========================
-- TAXI_TRAJET
-- =========================
CREATE TABLE taxi_trajet ( -- voyage tegna izy 
    id SERIAL PRIMARY KEY,
    taxi_id INT NOT NULL REFERENCES taxi_brousse(id),
    trajet_id INT NOT NULL REFERENCES trajet(id), -- trajet atao
    chauffeur_id INT NOT NULL REFERENCES personne(id),
    aide_chauffeur_id INT REFERENCES personne(id),
    date_heure_depart TIMESTAMP NOT NULL
);

-- =========================
-- RESERVATION
-- =========================
CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    taxi_trajet_id INT NOT NULL REFERENCES taxi_trajet(id),
    nom_client VARCHAR(100),
    telephone VARCHAR(20),
    nb_places INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'RESERVATION'
        CHECK (statut IN ('RESERVATION','ATTENTE','FIL'))
);

-- =========================
-- RESERVATION_PLACE
-- =========================
CREATE TABLE reservation_place (
    id SERIAL PRIMARY KEY,
    taxi_trajet_id INT NOT NULL REFERENCES taxi_trajet(id),-- miala
    reservation_id INT NOT NULL REFERENCES reservation(id),
    numero_place INT NOT NULL,
    CONSTRAINT uq_place_trajet UNIQUE (taxi_trajet_id, numero_place)
);

-- =========================
-- PAIEMENT
-- =========================
CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    reservation_id INT REFERENCES reservation(id),
    type_paiement VARCHAR(30)
        CHECK (type_paiement IN ('TOTAL RESERVATION','ACOMPTE','TOTAL ARRIVEE')),
    mode_paiement VARCHAR(20)
        CHECK (mode_paiement IN ('ESPECE','MOBILE MONEY')),
    montant NUMERIC(10,2),
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- DEPENSE
-- =========================
CREATE TABLE depense (
    id SERIAL PRIMARY KEY,
    cooperative_id INT REFERENCES cooperative(id),
    type VARCHAR(50)
        CHECK (type IN ('CARBURANT','REPARATION','VISITE TECHNIQUE','SALAIRE')),
    montant NUMERIC(10,2),
    date_depense DATE
);




-- =========================
-- taxi_trajet dépend de trajet et taxi_brousse
-- =========================
ALTER TABLE taxi_trajet
DROP CONSTRAINT IF EXISTS taxi_trajet_trajet_id_fkey,
ADD CONSTRAINT taxi_trajet_trajet_id_fkey
FOREIGN KEY (trajet_id) REFERENCES trajet(id) ON DELETE CASCADE;

ALTER TABLE taxi_trajet
DROP CONSTRAINT IF EXISTS taxi_trajet_taxi_id_fkey,
ADD CONSTRAINT taxi_trajet_taxi_id_fkey
FOREIGN KEY (taxi_id) REFERENCES taxi_brousse(id) ON DELETE CASCADE;

ALTER TABLE taxi_trajet
DROP CONSTRAINT IF EXISTS taxi_trajet_chauffeur_id_fkey,
ADD CONSTRAINT taxi_trajet_chauffeur_id_fkey
FOREIGN KEY (chauffeur_id) REFERENCES personne(id) ON DELETE CASCADE;

ALTER TABLE taxi_trajet
DROP CONSTRAINT IF EXISTS taxi_trajet_aide_chauffeur_id_fkey,
ADD CONSTRAINT taxi_trajet_aide_chauffeur_id_fkey
FOREIGN KEY (aide_chauffeur_id) REFERENCES personne(id) ON DELETE CASCADE;

-- =========================
-- reservation dépend de taxi_trajet
-- =========================
ALTER TABLE reservation
DROP CONSTRAINT IF EXISTS reservation_taxi_trajet_id_fkey,
ADD CONSTRAINT reservation_taxi_trajet_id_fkey
FOREIGN KEY (taxi_trajet_id) REFERENCES taxi_trajet(id) ON DELETE CASCADE;

-- =========================
-- reservation_place dépend de reservation et taxi_trajet
-- =========================
ALTER TABLE reservation_place
DROP CONSTRAINT IF EXISTS reservation_place_reservation_id_fkey,
ADD CONSTRAINT reservation_place_reservation_id_fkey
FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE;

ALTER TABLE reservation_place
DROP CONSTRAINT IF EXISTS reservation_place_taxi_trajet_id_fkey,
ADD CONSTRAINT reservation_place_taxi_trajet_id_fkey
FOREIGN KEY (taxi_trajet_id) REFERENCES taxi_trajet(id) ON DELETE CASCADE;

-- =========================
-- paiement dépend de reservation
-- =========================
ALTER TABLE paiement
DROP CONSTRAINT IF EXISTS paiement_reservation_id_fkey,
ADD CONSTRAINT paiement_reservation_id_fkey
FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE;









-- 1. Ajouter prix_enfant dans la table trajet
ALTER TABLE trajet 
ADD COLUMN prix_enfant NUMERIC(10,2) DEFAULT 45000;

-- 2. Ajouter type_place dans reservation_place
ALTER TABLE reservation_place 
ADD COLUMN type_place VARCHAR(20) DEFAULT 'STANDARD'
    CHECK (type_place IN ('STANDARD', 'PREMIUM', 'VIP'));

-- 3. Ajouter nb_enfants dans reservation
ALTER TABLE reservation 
ADD COLUMN nb_enfants INT DEFAULT 0;
