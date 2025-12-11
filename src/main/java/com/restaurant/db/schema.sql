-- Création des types énumérés
CREATE TYPE etat_commande AS ENUM (
    'EN_ATTENTE',
    'VALIDEE',
    'EN_PREPARATION',
    'PRETE',
    'EN_LIVRAISON',
    'LIVREE',
    'ANNULEE',
    'TERMINEE'
);

-- Table des burgers
CREATE TABLE burgers (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix DECIMAL(10,2) NOT NULL CHECK (prix > 0),
    description TEXT,
    image VARCHAR(255),
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des compléments
CREATE TABLE complements (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix DECIMAL(10,2) NOT NULL CHECK (prix > 0),
    type VARCHAR(20) NOT NULL CHECK (type IN ('FRITE', 'BOISSON')),
    image VARCHAR(255),
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des menus
CREATE TABLE menus (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    burger_id INTEGER NOT NULL REFERENCES burgers(id),
    frite_id INTEGER REFERENCES complements(id),
    boisson_id INTEGER REFERENCES complements(id),
    image VARCHAR(255),
    prix DECIMAL(10,2) NOT NULL CHECK (prix > 0),
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (frite_id IS NULL OR (SELECT type FROM complements WHERE id = frite_id) = 'FRITE'),
    CHECK (boisson_id IS NULL OR (SELECT type FROM complements WHERE id = boisson_id) = 'BOISSON')
);

-- Table des clients
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(100) NOT NULL,
    adresse TEXT,
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des zones
CREATE TABLE zones (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    prix_livraison DECIMAL(10,2) NOT NULL CHECK (prix_livraison >= 0),
    quartiers TEXT[],
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des livreurs
CREATE TABLE livreurs (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    vehicule VARCHAR(50),
    disponible BOOLEAN DEFAULT TRUE,
    zone_id INTEGER REFERENCES zones(id),
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des commandes
CREATE TABLE commandes (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL REFERENCES clients(id),
    etat etat_commande DEFAULT 'EN_ATTENTE',
    type_livraison VARCHAR(20) NOT NULL CHECK (type_livraison IN ('SUR_PLACE', 'A_EMPORTER', 'LIVRAISON')),
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_livraison TIMESTAMP,
    zone_id INTEGER REFERENCES zones(id),
    livreur_id INTEGER REFERENCES livreurs(id),
    adresse_livraison TEXT,
    total DECIMAL(10,2) NOT NULL CHECK (total >= 0),
    payee BOOLEAN DEFAULT FALSE,
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des items de commande
CREATE TABLE commande_items (
    id SERIAL PRIMARY KEY,
    commande_id INTEGER NOT NULL REFERENCES commandes(id) ON DELETE CASCADE,
    burger_id INTEGER REFERENCES burgers(id),
    menu_id INTEGER REFERENCES menus(id),
    complement_id INTEGER REFERENCES complements(id),
    quantite INTEGER NOT NULL CHECK (quantite > 0),
    prix_unitaire DECIMAL(10,2) NOT NULL CHECK (prix_unitaire >= 0),
    CHECK (
        (burger_id IS NOT NULL AND menu_id IS NULL AND complement_id IS NULL) OR
        (burger_id IS NULL AND menu_id IS NOT NULL AND complement_id IS NULL) OR
        (burger_id IS NULL AND menu_id IS NULL AND complement_id IS NOT NULL)
    )
);

-- Table des paiements
CREATE TABLE paiements (
    id SERIAL PRIMARY KEY,
    commande_id INTEGER UNIQUE NOT NULL REFERENCES commandes(id),
    montant DECIMAL(10,2) NOT NULL CHECK (montant > 0),
    methode VARCHAR(20) NOT NULL CHECK (methode IN ('WAVE', 'OM', 'CARTE', 'ESPECES')),
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference VARCHAR(100),
    archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour les performances
CREATE INDEX idx_commandes_client ON commandes(client_id);
CREATE INDEX idx_commandes_date ON commandes(date_commande);
CREATE INDEX idx_commandes_etat ON commandes(etat);
CREATE INDEX idx_commande_items_commande ON commande_items(commande_id);
CREATE INDEX idx_paiements_commande ON paiements(commande_id);
CREATE INDEX idx_paiements_date ON paiements(date_paiement);

-- Triggers pour updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Application des triggers à toutes les tables
CREATE TRIGGER update_burgers_updated_at BEFORE UPDATE ON burgers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_complements_updated_at BEFORE UPDATE ON complements
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_menus_updated_at BEFORE UPDATE ON menus
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_clients_updated_at BEFORE UPDATE ON clients
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_zones_updated_at BEFORE UPDATE ON zones
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_livreurs_updated_at BEFORE UPDATE ON livreurs
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_commandes_updated_at BEFORE UPDATE ON commandes
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_paiements_updated_at BEFORE UPDATE ON paiements
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();