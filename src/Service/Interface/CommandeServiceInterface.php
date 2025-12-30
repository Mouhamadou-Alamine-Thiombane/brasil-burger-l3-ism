<?php

namespace App\Service\Interface;

use App\Entity\Commande;
use App\Entity\Client;
use App\Entity\CommandeItem;

interface CommandeServiceInterface
{
    public function creerCommande(Client $client, array $items, string $typeLivraison, ?string $adresse = null): Commande;
    
    public function validerCommande(Commande $commande): void;
    
    public function annulerCommande(Commande $commande): void;
    
    public function preparerCommande(Commande $commande): void;
    
    public function terminerCommande(Commande $commande): void;
    
    public function calculerTotalCommande(Commande $commande): float;
    
    public function ajouterItemCommande(Commande $commande, CommandeItem $item): void;
    
    public function getCommandesParEtat(string $etat): array;
    
    public function getCommandesDuJour(): array;
}