-- ====================================
-- SUPPRESSION ET RECRÉATION DE LA BASE
-- ====================================
DROP DATABASE IF EXISTS taxi_brousse;
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
    pourcentage_augmentation NUMERIC(5,2) DEFAULT 0,
    nbr_places INT NOT NULL CHECK (nbr_places > 0),
    poids_max_bagage NUMERIC(5,2) DEFAULT 20, -- kg par personne
    conso_carburant NUMERIC(5,2) DEFAULT 8, -- L/100km
    tarif_bagage NUMERIC(10,2) DEFAULT 5000, -- Ariary / kg pour le surplus
    nb_places_premium INT DEFAULT 0,
    nb_places_vip INT DEFAULT 0
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

-- =========================
-- VUE VOITURE
-- =========================
CREATE OR REPLACE VIEW view_voiture AS
SELECT 
    tb.id AS taxi_id,
    tb.immatriculation,
    tv.libelle AS type_voiture,
    tv.nbr_places,
    tv.poids_max_bagage,
    tv.conso_carburant,
    tv.tarif_bagage,
    tv.nb_places_premium,
    tv.nb_places_vip,
    tb.cooperative_id
FROM 
    taxi_brousse tb
JOIN 
    type_voiture tv ON tb.type_voiture_id = tv.id;

-- =========================
-- TRAJET
-- =========================
CREATE TABLE trajet (
    id SERIAL PRIMARY KEY,
    depart VARCHAR(100) NOT NULL,
    arrivee VARCHAR(100) NOT NULL,
    distance_km INT, -- distance en km
    prix_base NUMERIC(10,2), -- prix standard
    prix_premium NUMERIC(10,2), -- prix pour places premium
    prix_vip NUMERIC(10,2), -- prix pour places VIP
    prix_enfant NUMERIC(10,2), -- prix pour enfants (remise)
    pourcentage_augmentation NUMERIC(5,2) DEFAULT 0, -- augmentation saisonnière
    nombre_jour INT
);

-- =========================
-- TAXI_TRAJET (VOYAGE)
-- =========================
CREATE TABLE taxi_trajet (
    id SERIAL PRIMARY KEY,
    taxi_id INT NOT NULL REFERENCES taxi_brousse(id) ON DELETE CASCADE,
    trajet_id INT NOT NULL REFERENCES trajet(id) ON DELETE CASCADE,
    chauffeur_id INT NOT NULL REFERENCES personne(id) ON DELETE CASCADE,
    aide_chauffeur_id INT REFERENCES personne(id) ON DELETE CASCADE,
    date_heure_depart TIMESTAMP NOT NULL
);

-- =========================
-- RESERVATION
-- =========================
CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    taxi_trajet_id INT NOT NULL REFERENCES taxi_trajet(id) ON DELETE CASCADE,
    nom_client VARCHAR(100),
    telephone VARCHAR(20),
    nb_places INT NOT NULL,
    nb_enfants INT DEFAULT 0, -- nombre d'enfants dans la réservation
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'RESERVATION'
        CHECK (statut IN ('RESERVATION','ATTENTE','FIL'))
);

-- =========================
-- RESERVATION_PLACE
-- =========================
CREATE TABLE reservation_place (
    id SERIAL PRIMARY KEY,
    taxi_trajet_id INT NOT NULL REFERENCES taxi_trajet(id) ON DELETE CASCADE,
    reservation_id INT NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
    numero_place INT NOT NULL,
    type_place VARCHAR(20) DEFAULT 'STANDARD'
        CHECK (type_place IN ('STANDARD', 'PREMIUM', 'VIP')),
    CONSTRAINT uq_place_trajet UNIQUE (taxi_trajet_id, numero_place)
);

-- =========================
-- PAIEMENT
-- =========================
CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    reservation_id INT REFERENCES reservation(id) ON DELETE CASCADE,
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
-- INDEX POUR PERFORMANCES
-- =========================
CREATE INDEX idx_taxi_trajet_taxi ON taxi_trajet(taxi_id);
CREATE INDEX idx_taxi_trajet_trajet ON taxi_trajet(trajet_id);
CREATE INDEX idx_taxi_trajet_date ON taxi_trajet(date_heure_depart);
CREATE INDEX idx_reservation_taxi_trajet ON reservation(taxi_trajet_id);
CREATE INDEX idx_reservation_place_taxi_trajet ON reservation_place(taxi_trajet_id);
CREATE INDEX idx_reservation_place_reservation ON reservation_place(reservation_id);
CREATE INDEX idx_paiement_reservation ON paiement(reservation_id);

-- =========================
-- COMMENTAIRES
-- =========================
COMMENT ON TABLE cooperative IS 'Coopératives de taxi-brousse';
COMMENT ON TABLE personne IS 'Personnel (chauffeurs, guichetiers, etc.)';
COMMENT ON TABLE type_voiture IS 'Types de véhicules avec configuration des places';
COMMENT ON TABLE taxi_brousse IS 'Véhicules de la flotte';
COMMENT ON TABLE trajet IS 'Trajets disponibles avec tarification par catégorie';
COMMENT ON TABLE taxi_trajet IS 'Voyages planifiés (affectation taxi + trajet + date)';
COMMENT ON TABLE reservation IS 'Réservations clients';
COMMENT ON TABLE reservation_place IS 'Détail des places réservées avec leur catégorie';
COMMENT ON TABLE paiement IS 'Paiements effectués';
COMMENT ON TABLE depense IS 'Dépenses de la coopérative';

COMMENT ON COLUMN trajet.prix_base IS 'Prix pour une place standard adulte';
COMMENT ON COLUMN trajet.prix_premium IS 'Prix pour une place premium';
COMMENT ON COLUMN trajet.prix_vip IS 'Prix pour une place VIP';
COMMENT ON COLUMN trajet.prix_enfant IS 'Prix réduit pour enfants (applicable uniquement aux places standard)';
COMMENT ON COLUMN reservation.nb_enfants IS 'Nombre d''enfants bénéficiant de la remise';
COMMENT ON COLUMN reservation_place.type_place IS 'Catégorie de la place: STANDARD, PREMIUM ou VIP';
COMMENT ON COLUMN type_voiture.nb_places_premium IS 'Nombre de places premium dans ce type de véhicule';
COMMENT ON COLUMN type_voiture.nb_places_vip IS 'Nombre de places VIP dans ce type de véhicule';