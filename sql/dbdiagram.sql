Table cooperative {
  id int [pk]
  nom varchar
}

Table personne {
  id int [pk]
  nom varchar
  telephone varchar
  role varchar
}

Table type_voiture {
  id int [pk]
  libelle varchar
  nbr_places int
  poids_max_bagage decimal
  conso_carburant decimal
  tarif_bagage decimal
}

Table taxi_brousse {
  id int [pk]
  cooperative_id int [ref: > cooperative.id]
  immatriculation varchar
  type_voiture_id int [ref: > type_voiture.id]
  conso_carburant decimal
}

Table trajet {
  id int [pk]
  depart varchar
  arrivee varchar
  distance_km int
  prix_base decimal
  pourcentage_augmentation decimal
}

Table taxi_trajet {
  id int [pk]
  taxi_id int [ref: > taxi_brousse.id]
  trajet_id int [ref: > trajet.id]
  chauffeur_id int [ref: > personne.id]
  aide_chauffeur_id int [ref: > personne.id]
  date_heure_depart timestamp
}

Table reservation {
  id int [pk]
  taxi_trajet_id int [ref: > taxi_trajet.id]
  nom_client varchar
  telephone varchar
  nb_places int
  date_reservation timestamp
  statut varchar
}

Table reservation_place {
  id int [pk]
  taxi_trajet_id int [ref: > taxi_trajet.id]
  reservation_id int [ref: > reservation.id]
  numero_place int
}

Table paiement {
  id int [pk]
  reservation_id int [ref: > reservation.id]
  type_paiement varchar
  mode_paiement varchar
  montant decimal
  date_paiement timestamp
}

Table depense {
  id int [pk]
  cooperative_id int [ref: > cooperative.id]
  type varchar
  montant decimal
  date_depense date
}