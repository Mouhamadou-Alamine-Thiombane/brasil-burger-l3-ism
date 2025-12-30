<?php

namespace App\Repository;

use App\Entity\Commande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Commande>
 */
class CommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande::class);
    }

    public function findRecentCommandes(int $limit = 100): array
    {
        return $this->createQueryBuilder('c')
            ->orderBy('c.dateCommande', 'DESC')
            ->setMaxResults($limit)
            ->getQuery()
            ->getResult();
    }

    public function findCommandesPreteALivrer(): array
    {
        return $this->createQueryBuilder('c')
            ->where('c.etat = :etat')
            ->andWhere('c.typeLivraison = :type')
            ->setParameter('etat', 'PRETE')
            ->setParameter('type', 'LIVRAISON')
            ->orderBy('c.dateCommande', 'ASC')
            ->getQuery()
            ->getResult();
    }

    public function findByFilters(array $filters): array
    {
        $qb = $this->createQueryBuilder('c');
        
        if (!empty($filters['etat'])) {
            $qb->andWhere('c.etat = :etat')
               ->setParameter('etat', $filters['etat']);
        }
        
        if (!empty($filters['date'])) {
            $qb->andWhere('c.dateCommande >= :date')
               ->setParameter('date', $filters['date']);
        }
        
        return $qb->orderBy('c.dateCommande', 'DESC')
                  ->getQuery()
                  ->getResult();
    }
}