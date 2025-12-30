<?php

namespace App\Service\Impl;

use App\Service\Interface\ClientServiceInterface;
use App\Entity\Client;
use App\Repository\ClientRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class ClientServiceImpl implements ClientServiceInterface
{
    private EntityManagerInterface $entityManager;
    private ClientRepository $clientRepository;
    private UserPasswordHasherInterface $passwordHasher;

    public function __construct(
        EntityManagerInterface $entityManager,
        ClientRepository $clientRepository,
        UserPasswordHasherInterface $passwordHasher
    ) {
        $this->entityManager = $entityManager;
        $this->clientRepository = $clientRepository;
        $this->passwordHasher = $passwordHasher;
    }

    public function creerClient(array $data): Client
    {
        $client = new Client();
        $client->setNom($data['nom']);
        $client->setPrenom($data['prenom']);
        $client->setTelephone($data['telephone']);
        $client->setEmail($data['email']);
        
        // Hasher le mot de passe
        $hashedPassword = $this->passwordHasher->hashPassword($client, $data['mot_de_passe']);
        $client->setMotDePasse($hashedPassword);
        
        if (isset($data['adresse'])) {
            $client->setAdresse($data['adresse']);
        }
        
        $this->entityManager->persist($client);
        $this->entityManager->flush();
        
        return $client;
    }

    public function archiverClient(Client $client): void
    {
        $client->setArchived(true);
        $this->entityManager->flush();
    }

    public function desarchiverClient(Client $client): void
    {
        $client->setArchived(false);
        $this->entityManager->flush();
    }

    public function getHistoriqueCommandes(Client $client): array
    {
        return $client->getCommandes()->toArray();
    }

    public function getMontantTotalDepense(Client $client): float
    {
        $total = 0;
        
        foreach ($client->getCommandes() as $commande) {
            if ($commande->isPayee()) {
                $total += $commande->getTotal();
            }
        }
        
        return $total;
    }

    public function verifierEmailExiste(string $email): bool
    {
        return $this->clientRepository->findOneBy(['email' => $email]) !== null;
    }

    public function verifierTelephoneExiste(string $telephone): bool
    {
        return $this->clientRepository->findOneBy(['telephone' => $telephone]) !== null;
    }
}