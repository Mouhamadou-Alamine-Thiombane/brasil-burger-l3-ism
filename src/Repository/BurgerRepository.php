<?php

namespace App\Repository;

use App\Entity\Burger;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Burger>
 */
class BurgerRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Burger::class);
    }

    public function findAllOrderByNom(): array
    {
        return $this->createQueryBuilder('b')
            ->where('b.archived = false')
            ->orderBy('b.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }

    public function findBurgersActifs(): array
    {
        return $this->createQueryBuilder('b')
            ->where('b.archived = false')
            ->orderBy('b.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}