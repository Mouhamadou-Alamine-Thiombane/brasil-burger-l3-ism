<?php

namespace App\Service\Interface;

use App\Entity\Client;

interface ClientServiceInterface
{
    public function creerClient(array $data): Client;
    
    public function archiverClient(Client $client): void;
    
    public function desarchiverClient(Client $client): void;
    
    public function getHistoriqueCommandes(Client $client): array;
    
    public function getMontantTotalDepense(Client $client): float;
    
    public function verifierEmailExiste(string $email): bool;
    
    public function verifierTelephoneExiste(string $telephone): bool;
}