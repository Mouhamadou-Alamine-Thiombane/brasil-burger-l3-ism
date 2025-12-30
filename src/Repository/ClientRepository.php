<?php

namespace App\Repository;

use App\Entity\Client;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class ClientRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Client::class);
    }

    public function search(string $query): array
    {
        return $this->createQueryBuilder('c')
            ->where('c.nom LIKE :query')
            ->orWhere('c.prenom LIKE :query')
            ->orWhere('c.telephone LIKE :query')
            ->orWhere('c.email LIKE :query')
            ->andWhere('c.archived = false')
            ->setParameter('query', '%' . $query . '%')
            ->orderBy('c.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }
    
    public function save(Client $client, bool $flush = false): void
    {
        $this->getEntityManager()->persist($client);
        
        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }
}
