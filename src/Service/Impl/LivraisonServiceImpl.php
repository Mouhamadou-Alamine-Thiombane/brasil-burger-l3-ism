<?php

namespace App\Service\Impl;

use App\Service\Interface\LivraisonServiceInterface;
use App\Entity\Commande;
use App\Entity\Livreur;
use App\Entity\EtatCommande;
use App\Repository\LivreurRepository;
use App\Repository\CommandeRepository;
use Doctrine\ORM\EntityManagerInterface;

class LivraisonServiceImpl implements LivraisonServiceInterface
{
    private EntityManagerInterface $entityManager;
    private LivreurRepository $livreurRepository;
    private CommandeRepository $commandeRepository;

    public function __construct(
        EntityManagerInterface $entityManager,
        LivreurRepository $livreurRepository,
        CommandeRepository $commandeRepository
    ) {
        $this->entityManager = $entityManager;
        $this->livreurRepository = $livreurRepository;
        $this->commandeRepository = $commandeRepository;
    }

    public function affecterLivreur(Commande $commande, Livreur $livreur): void
    {
        if ($commande->getEtat() === EtatCommande::PRETE 
            && $commande->getTypeLivraison() === 'LIVRAISON'
            && $livreur->isDisponible()) {
            
            $commande->setLivreur($livreur);
            $commande->setEtat(EtatCommande::EN_LIVRAISON);
            $commande->setDateLivraison(new \DateTimeImmutable());
            
            $livreur->setDisponible(false);
            
            $this->entityManager->flush();
        }
    }

    public function libererLivreur(Livreur $livreur): void
    {
        $livreur->setDisponible(true);
        $this->entityManager->flush();
    }

    public function getLivreursDisponibles(): array
    {
        return $this->livreurRepository->findLivreursDisponibles();
    }

    public function getCommandesEnAttenteLivraison(): array
    {
        return $this->commandeRepository->findCommandesPreteALivrer();
    }

    public function calculerFraisLivraison(Commande $commande): float
    {
        if ($commande->getTypeLivraison() !== 'LIVRAISON' || !$commande->getZone()) {
            return 0;
        }
        
        return $commande->getZone()->getPrixLivraison();
    }

    public function marquerCommandeLivree(Commande $commande): void
    {
        if ($commande->getEtat() === EtatCommande::EN_LIVRAISON) {
            $commande->setEtat(EtatCommande::LIVREE);
            
            // Libérer le livreur
            if ($commande->getLivreur()) {
                $this->libererLivreur($commande->getLivreur());
            }
            
            $this->entityManager->flush();
        }
    }
}