<?php

namespace App\Repository;

use App\Entity\Livreur;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class LivreurRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livreur::class);
    }

    public function findLivreursDisponibles(): array
    {
        return $this->createQueryBuilder('l')
            ->where('l.disponible = true')
            ->andWhere('l.archived = false')
            ->orderBy('l.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}