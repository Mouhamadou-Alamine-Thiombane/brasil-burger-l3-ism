<?php

namespace App\Service\Impl;

use App\Service\Interface\StatistiqueServiceInterface;
use App\Service\Interface\CommandeServiceInterface;
use App\DTO\StatistiqueJournaliereDTO;
use App\Entity\EtatCommande;
use Doctrine\ORM\EntityManagerInterface;

class StatistiqueServiceImpl implements StatistiqueServiceInterface
{
    private EntityManagerInterface $entityManager;
    private CommandeServiceInterface $commandeService;

    public function __construct(
        EntityManagerInterface $entityManager,
        CommandeServiceInterface $commandeService
    ) {
        $this->entityManager = $entityManager;
        $this->commandeService = $commandeService;
    }

    public function getStatistiquesJournalieres(\DateTimeInterface $date): StatistiqueJournaliereDTO
    {
        $statistiques = new StatistiqueJournaliereDTO();
        
        $statistiques->date = $date;
        $statistiques->commandesEnCours = $this->getCommandesEnCours($date);
        $statistiques->commandesValidees = $this->getCommandesValidees($date);
        $statistiques->recetteJournaliere = $this->getRecetteJournaliere($date);
        $statistiques->burgersPlusVendus = $this->getBurgersPlusVendus($date);
        $statistiques->commandesAnnulees = $this->getCommandesAnnulees($date);
        
        return $statistiques;
    }

    public function getCommandesEnCours(\DateTimeInterface $date): array
    {
        $debut = (clone $date)->setTime(0, 0, 0);
        $fin = (clone $date)->setTime(23, 59, 59);
        
        $qb = $this->entityManager->createQueryBuilder();
        
        return $qb->select('c')
            ->from('App\Entity\Commande', 'c')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande <= :fin')
            ->andWhere($qb->expr()->in('c.etat', [
                EtatCommande::EN_ATTENTE,
                EtatCommande::VALIDEE,
                EtatCommande::EN_PREPARATION
            ]))
            ->orderBy('c.dateCommande', 'DESC')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->getQuery()
            ->getResult();
    }

    public function getCommandesValidees(\DateTimeInterface $date): array
    {
        $debut = (clone $date)->setTime(0, 0, 0);
        $fin = (clone $date)->setTime(23, 59, 59);
        
        $qb = $this->entityManager->createQueryBuilder();
        
        $result = $qb->select('COUNT(c.id) as count')
            ->from('App\Entity\Commande', 'c')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande <= :fin')
            ->andWhere('c.etat = :etat')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('etat', EtatCommande::VALIDEE)
            ->getQuery()
            ->getSingleResult();
            
        return [['count' => $result['count'] ?? 0]];
    }

    public function getRecetteJournaliere(\DateTimeInterface $date): float
    {
        $debut = (clone $date)->setTime(0, 0, 0);
        $fin = (clone $date)->setTime(23, 59, 59);
        
        $qb = $this->entityManager->createQueryBuilder();
        
        $result = $qb->select('COALESCE(SUM(p.montant), 0) as total')
            ->from('App\Entity\Paiement', 'p')
            ->join('p.commande', 'c')
            ->where('p.datePaiement >= :debut')
            ->andWhere('p.datePaiement <= :fin')
            ->andWhere('c.etat != :annulee')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('annulee', EtatCommande::ANNULEE)
            ->getQuery()
            ->getSingleResult();
            
        return (float) ($result['total'] ?? 0);
    }

    public function getBurgersPlusVendus(\DateTimeInterface $date, int $limit = 5): array
    {
        $debut = (clone $date)->setTime(0, 0, 0);
        $fin = (clone $date)->setTime(23, 59, 59);
        
        $qb = $this->entityManager->createQueryBuilder();
        
        return $qb->select([
                'b.nom',
                'SUM(ci.quantite) as quantite_vendue',
                'SUM(ci.prixUnitaire * ci.quantite) as chiffre_affaires'
            ])
            ->from('App\Entity\CommandeItem', 'ci')
            ->join('ci.burger', 'b')
            ->join('ci.commande', 'c')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande <= :fin')
            ->andWhere('c.etat != :annulee')
            ->groupBy('b.id', 'b.nom')
            ->orderBy('quantite_vendue', 'DESC')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('annulee', EtatCommande::ANNULEE)
            ->setMaxResults($limit)
            ->getQuery()
            ->getResult();
    }

    public function getCommandesAnnulees(\DateTimeInterface $date): array
    {
        $debut = (clone $date)->setTime(0, 0, 0);
        $fin = (clone $date)->setTime(23, 59, 59);
        
        $qb = $this->entityManager->createQueryBuilder();
        
        $result = $qb->select('COUNT(c.id) as count')
            ->from('App\Entity\Commande', 'c')
            ->where('c.dateCommande >= :debut')
            ->andWhere('c.dateCommande <= :fin')
            ->andWhere('c.etat = :etat')
            ->setParameter('debut', $debut)
            ->setParameter('fin', $fin)
            ->setParameter('etat', EtatCommande::ANNULEE)
            ->getQuery()
            ->getSingleResult();
            
        return [['count' => $result['count'] ?? 0]];
    }

    public function getStatistiquesParPeriode(\DateTimeInterface $debut, \DateTimeInterface $fin): array
    {
        $result = [];
        
        $interval = new \DateInterval('P1D');
        $period = new \DatePeriod($debut, $interval, $fin);
        
        foreach ($period as $date) {
            $result[] = $this->getStatistiquesJournalieres($date);
        }
        
        return $result;
    }
}