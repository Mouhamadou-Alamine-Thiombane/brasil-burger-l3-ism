<?php

namespace App\Service\Impl;

use App\Service\Interface\CommandeServiceInterface;
use App\Entity\Commande;
use App\Entity\Client;
use App\Entity\CommandeItem;
use App\Entity\EtatCommande;
use App\Repository\CommandeRepository;
use App\Repository\ZoneRepository;
use Doctrine\ORM\EntityManagerInterface;

class CommandeServiceImpl implements CommandeServiceInterface
{
    private EntityManagerInterface $entityManager;
    private CommandeRepository $commandeRepository;
    private ZoneRepository $zoneRepository;

    public function __construct(
        EntityManagerInterface $entityManager,
        CommandeRepository $commandeRepository,
        ZoneRepository $zoneRepository
    ) {
        $this->entityManager = $entityManager;
        $this->commandeRepository = $commandeRepository;
        $this->zoneRepository = $zoneRepository;
    }

    public function creerCommande(Client $client, array $items, string $typeLivraison, ?string $adresse = null): Commande
    {
        $commande = new Commande();
        $commande->setClient($client);
        $commande->setTypeLivraison($typeLivraison);
        $commande->setAdresseLivraison($adresse);
        $commande->setEtat(EtatCommande::EN_ATTENTE);
        
        // Ajouter les items
        foreach ($items as $item) {
            $commande->addCommandeItem($item);
        }
        
        // Calculer le total
        $total = $this->calculerTotalCommande($commande);
        $commande->setTotal($total);
        
        $this->entityManager->persist($commande);
        $this->entityManager->flush();
        
        return $commande;
    }

    public function validerCommande(Commande $commande): void
    {
        if ($commande->getEtat() === EtatCommande::EN_ATTENTE) {
            $commande->setEtat(EtatCommande::VALIDEE);
            $this->entityManager->flush();
        }
    }

    public function annulerCommande(Commande $commande): void
    {
        if (in_array($commande->getEtat(), [EtatCommande::EN_ATTENTE, EtatCommande::VALIDEE])) {
            $commande->setEtat(EtatCommande::ANNULEE);
            $this->entityManager->flush();
        }
    }

    public function preparerCommande(Commande $commande): void
    {
        if ($commande->getEtat() === EtatCommande::VALIDEE) {
            $commande->setEtat(EtatCommande::EN_PREPARATION);
            $this->entityManager->flush();
        }
    }

    public function terminerCommande(Commande $commande): void
    {
        if ($commande->getEtat() === EtatCommande::EN_PREPARATION) {
            $commande->setEtat(EtatCommande::PRETE);
            $this->entityManager->flush();
        }
    }

    public function calculerTotalCommande(Commande $commande): float
    {
        $total = 0;
        
        foreach ($commande->getCommandeItems() as $item) {
            $total += $item->getTotal();
        }
        
        // Ajouter frais de livraison si nécessaire
        if ($commande->getTypeLivraison() === 'LIVRAISON' && $commande->getZone()) {
            $total += $commande->getZone()->getPrixLivraison();
        }
        
        return $total;
    }

    public function ajouterItemCommande(Commande $commande, CommandeItem $item): void
    {
        $commande->addCommandeItem($item);
        
        // Recalculer le total
        $total = $this->calculerTotalCommande($commande);
        $commande->setTotal($total);
        
        $this->entityManager->flush();
    }

    public function getCommandesParEtat(string $etat): array
    {
        return $this->commandeRepository->findBy(['etat' => $etat], ['dateCommande' => 'DESC']);
    }

    public function getCommandesDuJour(): array
    {
        $debut = (new \DateTimeImmutable())->setTime(0, 0, 0);
        $fin = (new \DateTimeImmutable())->setTime(23, 59, 59);
        
        return $this->commandeRepository->createQueryBuilder('c')
            ->where('c.dateCommande BETWEEN :debut AND :fin')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->orderBy('c.dateCommande', 'DESC')
            ->getQuery()
            ->getResult();
    }
}