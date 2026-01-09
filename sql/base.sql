
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
    libelle VARCHAR(50) NOT NULL UNIQUE,
    nbr_places INT NOT NULL CHECK (nbr_places > 0),
    poids_max_bagage NUMERIC(5,2) DEFAULT 20, -- kg par personne
    conso_carburant NUMERIC(5,2) DEFAULT 8, -- L/100km
    tarif_bagage NUMERIC(10,2) DEFAULT 5000 -- Ariary / kg
);

-- =========================
-- TAXI BROUSSE
-- =========================
CREATE TABLE taxi_brousse (
    id SERIAL PRIMARY KEY,
    cooperative_id INT NOT NULL REFERENCES cooperative(id),
    immatriculation VARCHAR(20) UNIQUE NOT NULL,
    type_voiture_id INT NOT NULL REFERENCES type_voiture(id)
);

-- =========================
-- TRAJET
-- =========================
CREATE TABLE trajet (
    id SERIAL PRIMARY KEY,
    depart VARCHAR(100) NOT NULL,
    arrivee VARCHAR(100) NOT NULL,
    distance_km INT,
    prix_base NUMERIC(10,2),
    pourcentage_augmentation NUMERIC(5,2)
);

-- =========================
-- TAXI_TRAJET
-- =========================
CREATE TABLE taxi_trajet (
    id SERIAL PRIMARY KEY,
    taxi_id INT NOT NULL REFERENCES taxi_brousse(id),
    trajet_id INT NOT NULL REFERENCES trajet(id),
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
    taxi_trajet_id INT NOT NULL REFERENCES taxi_trajet(id),
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
