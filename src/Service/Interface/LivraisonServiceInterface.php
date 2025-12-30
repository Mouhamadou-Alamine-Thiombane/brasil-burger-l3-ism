<?php

namespace App\Service\Interface;

use App\Entity\Commande;
use App\Entity\Livreur;

interface LivraisonServiceInterface
{
    public function affecterLivreur(Commande $commande, Livreur $livreur): void;
    
    public function libererLivreur(Livreur $livreur): void;
    
    public function getLivreursDisponibles(): array;
    
    public function getCommandesEnAttenteLivraison(): array;
    
    public function calculerFraisLivraison(Commande $commande): float;
    
    public function marquerCommandeLivree(Commande $commande): void;
}