<?php

namespace App\Repository;

use App\Entity\Menu;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Menu>
 */
class MenuRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Menu::class);
    }

    public function findAllWithBurgers(): array
    {
        return $this->createQueryBuilder('m')
            ->leftJoin('m.burger', 'b')
            ->addSelect('b')
            ->leftJoin('m.frite', 'f')
            ->addSelect('f')
            ->leftJoin('m.boisson', 'bo')
            ->addSelect('bo')
            ->where('m.archived = false')
            ->orderBy('m.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }

    public function findMenusActifs(): array
    {
        return $this->createQueryBuilder('m')
            ->where('m.archived = false')
            ->orderBy('m.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
}