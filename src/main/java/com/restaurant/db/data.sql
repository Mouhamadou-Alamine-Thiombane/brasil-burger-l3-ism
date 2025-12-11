-- Données initiales pour les burgers
INSERT INTO burgers (nom, prix, description, image) VALUES
('Classic Burger', 2500, 'Steak haché, salade, tomate, oignon, sauce maison', 'classic.jpg'),
('Cheese Burger', 3000, 'Steak haché, cheddar, salade, tomate, sauce maison', 'cheese.jpg'),
('Double Cheese', 4000, 'Double steak haché, double cheddar, salade, tomate', 'double_cheese.jpg'),
('Chicken Burger', 3500, 'Filet de poulet pané, salade, tomate, sauce blanche', 'chicken.jpg'),
('Fish Burger', 3500, 'Filet de poisson pané, salade, tomate, sauce tartare', 'fish.jpg'),
('Vegetarien', 2800, 'Galette de légumes, salade, tomate, avocat, sauce yogourt', 'veggie.jpg');

-- Données initiales pour les compléments
INSERT INTO complements (nom, prix, type, image) VALUES
('Frites Normales', 1500, 'FRITE', 'frites_normales.jpg'),
('Frites Maison', 2000, 'FRITE', 'frites_maison.jpg'),
('Coca-Cola', 1000, 'BOISSON', 'coca.jpg'),
('Fanta', 1000, 'BOISSON', 'fanta.jpg'),
('Sprite', 1000, 'BOISSON', 'sprite.jpg'),
('Eau Minérale', 500, 'BOISSON', 'eau.jpg');

-- Données initiales pour les menus (après avoir inséré les burgers et compléments)
INSERT INTO menus (nom, burger_id, frite_id, boisson_id, prix) VALUES
('Menu Classic', 1, 1, 3, 5000),
('Menu Cheese', 2, 1, 3, 5500),
('Menu Double Cheese', 3, 1, 3, 6500),
('Menu Chicken', 4, 1, 3, 6000);

-- Données initiales pour les zones
INSERT INTO zones (nom, prix_livraison, quartiers) VALUES
('Zone 1 - Centre Ville', 1000, ARRAY['Plateau', 'Medina', 'Fann']),
('Zone 2 - Almadies', 1500, ARRAY['Almadies', 'Mermoz', 'Ouakam']),
('Zone 3 - Grand Dakar', 1200, ARRAY['Grand Dakar', 'Fass', 'Gueule Tapée']),
('Zone 4 - Pikine', 2000, ARRAY['Pikine', 'Thiaroye', 'Diamaguene']);

-- Données initiales pour les livreurs
INSERT INTO livreurs (nom, prenom, telephone, vehicule, zone_id) VALUES
('Diallo', 'Moussa', '771234567', 'Moto', 1),
('Ndiaye', 'Aminata', '781234567', 'Voiture', 2),
('Sow', 'Ibrahima', '761234567', 'Moto', 3),
('Fall', 'Fatou', '701234567', 'Vélo', 4);

-- Création d'un compte administrateur (client spécial)
INSERT INTO clients (nom, prenom, telephone, email, mot_de_passe, adresse) VALUES
('Admin', 'System', '770000000', 'admin@brasilburger.sn', 'admin123', 'Plateau, Dakar');

-- Création de quelques clients de test
INSERT INTO clients (nom, prenom, telephone, email, mot_de_passe, adresse) VALUES
('Ndiaye', 'Abdoulaye', '771111111', 'abdoulaye@email.com', 'pass123', 'Fann, Dakar'),
('Diop', 'Aissatou', '772222222', 'aissatou@email.com', 'pass123', 'Almadies, Dakar'),
('Gaye', 'Mamadou', '773333333', 'mamadou@email.com', 'pass123', 'Grand Dakar');