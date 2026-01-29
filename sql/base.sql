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
        CHECK (roles IN (
            'CHAUFFEUR',
            'AIDE CHAUFFEUR',
            'GUICHETIER',
            'CAISSIER',
            'RESP PLANNING',
            'MECANICIEN',
            'COMPTABLE',
            'DIRECTEUR',
            'AGENT COMMERCIAL'
        ))
);

-- =========================
-- TYPE VOITURE
-- =========================
CREATE TABLE type_voiture (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE,
    nbr_places INT NOT NULL CHECK (nbr_places > 0)
);

-- =========================
-- TAXI BROUSSE
-- =========================
CREATE TABLE taxi_brousse (
    id SERIAL PRIMARY KEY,
    cooperative_id INT NOT NULL,
    immatriculation VARCHAR(20) UNIQUE NOT NULL,
    type_voiture_id INT NOT NULL,

    CONSTRAINT fk_taxi_cooperative
        FOREIGN KEY (cooperative_id)
        REFERENCES cooperative(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_type_voiture
        FOREIGN KEY (type_voiture_id)
        REFERENCES type_voiture(id)
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
    nombre_jour INT
);

-- =========================
-- TAXI_TRAJET (VOYAGE)
-- =========================
CREATE TABLE taxi_trajet (
    id SERIAL PRIMARY KEY,
    taxi_id INT NOT NULL,
    trajet_id INT NOT NULL,
    chauffeur_id INT NOT NULL,
    aide_chauffeur_id INT,
    date_heure_depart TIMESTAMP NOT NULL,

    CONSTRAINT fk_taxi_trajet_taxi
        FOREIGN KEY (taxi_id)
        REFERENCES taxi_brousse(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_trajet_trajet
        FOREIGN KEY (trajet_id)
        REFERENCES trajet(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_trajet_chauffeur
        FOREIGN KEY (chauffeur_id)
        REFERENCES personne(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_trajet_aide
        FOREIGN KEY (aide_chauffeur_id)
        REFERENCES personne(id)
        ON DELETE CASCADE
);

-- =========================
-- RESERVATION
-- =========================
CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    taxi_trajet_id INT NOT NULL,
    nom_client VARCHAR(100),
    telephone VARCHAR(20),
    nb_places INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'RESERVATION'
        CHECK (statut IN ('RESERVATION','ATTENTE','FIL')),
    etat_paye BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_reservation_taxi_trajet
        FOREIGN KEY (taxi_trajet_id)
        REFERENCES taxi_trajet(id)
        ON DELETE CASCADE
);

-- =========================
-- CATEGORIE PLACE
-- =========================
CREATE TABLE categorie_place (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(20) UNIQUE NOT NULL
        CHECK (libelle IN ('STANDARD','PREMIUM','VIP')),
    prix NUMERIC(10,2)
);

CREATE TABLE config_place (
    id SERIAL PRIMARY KEY,
    id_taxi_brousse INT NOT NULL,
    id_categ_place INT NOT NULL,
    nbr INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_categ_place
        FOREIGN KEY (id_categ_place)
        REFERENCES categorie_place(id),    

    CONSTRAINT fk_taxi_brousse
        FOREIGN KEY (id_taxi_brousse)
        REFERENCES taxi_brousse(id)
        ON DELETE CASCADE
);

-- =========================
-- TYPE PASSAGER
-- =========================
CREATE TABLE type_passager (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(20) UNIQUE NOT NULL
        CHECK (libelle IN ('ENFANT','ADULTE','SENIOR'))
);

-- =========================
-- PLACE PASSAGER
-- =========================
CREATE TABLE place_passager (
    id SERIAL PRIMARY KEY,
    id_categ_place INT NOT NULL,
    id_type_passager INT NOT NULL,
    prix NUMERIC(10,2),

    CONSTRAINT fk_place_categorie
        FOREIGN KEY (id_categ_place)
        REFERENCES categorie_place(id),

    CONSTRAINT fk_place_type_passager
        FOREIGN KEY (id_type_passager)
        REFERENCES type_passager(id)
);

-- =========================
-- RESERVATION_PLACE
-- =========================
CREATE TABLE reservation_place (
    id SERIAL PRIMARY KEY,
    reservation_id INT NOT NULL,
    numero_place INT NOT NULL,
    id_place_passager INT NOT NULL,

    CONSTRAINT fk_reservation_place_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_reservation_place_place
        FOREIGN KEY (id_place_passager)
        REFERENCES place_passager(id)
);

-- =========================
-- PAIEMENT
-- =========================
CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    reservation_id INT NOT NULL,
    type_paiement VARCHAR(30)
        CHECK (type_paiement IN ('TOTAL RESERVATION','ACOMPTE','TOTAL ARRIVEE')),
    mode_paiement VARCHAR(20)
        CHECK (mode_paiement IN ('ESPECE','MOBILE MONEY')),
    montant NUMERIC(10,2),
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_paiement_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservation(id)
        ON DELETE CASCADE
);

-- =========================
-- DEPENSE
-- =========================
CREATE TABLE societe (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE pub (
    id SERIAL PRIMARY KEY,
    idSociete INT NOT NULL,
    descri_pub TEXT,
    cout NUMERIC(10,2),
    CONSTRAINT fk_pub_societe
        FOREIGN KEY (idSociete)
        REFERENCES societe(id)
        ON DELETE CASCADE
);

CREATE TABLE diffusion (
    id SERIAL PRIMARY KEY,
    idPub INT NOT NULL,
    idTaxiTrajet INT NOT NULL,
    nb_diffusions INT,


    CONSTRAINT fk_diffusion_pub
        FOREIGN KEY (idPub)
        REFERENCES pub(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_diffusion_taxi_trajet
        FOREIGN KEY (idTaxiTrajet)
        REFERENCES taxi_trajet(id)
        ON DELETE CASCADE
);

CREATE TABLE payment_diffusion (
    id SERIAL PRIMARY KEY,
    idSociete INT NOT NULL,
    montant NUMERIC(10,2),
    date_payment TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_diffusion_societe
        FOREIGN KEY (idSociete)
        REFERENCES societe(id)
        ON DELETE CASCADE
);
 
CREATE OR REPLACE FUNCTION maj_tarif_senior()
RETURNS TRIGGER AS $$
DECLARE
    id_senior INT;
BEGIN
    -- Vérifier si la ligne modifiée concerne un ADULTE
    IF EXISTS (
        SELECT 1
        FROM type_passager tp
        WHERE tp.id = NEW.id_type_passager
          AND tp.libelle = 'ADULTE'
    ) THEN

        -- Récupérer l'id du type SENIOR
        SELECT id INTO id_senior
        FROM type_passager
        WHERE libelle = 'SENIOR';

        -- Mettre à jour le tarif SENIOR correspondant
        UPDATE place_passager
        SET prix = ROUND(NEW.prix * 0.8, 2)
        WHERE id_categ_place = NEW.id_categ_place
          AND id_type_passager = id_senior;

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_maj_tarif_senior
AFTER INSERT OR UPDATE OF prix
ON place_passager
FOR EACH ROW
EXECUTE FUNCTION maj_tarif_senior();
