<?php

namespace App\Service\Interface;

use App\DTO\StatistiqueJournaliereDTO;

interface StatistiqueServiceInterface
{
    public function getStatistiquesJournalieres(\DateTimeInterface $date): StatistiqueJournaliereDTO;
    
    public function getCommandesEnCours(\DateTimeInterface $date): array;
    
    public function getCommandesValidees(\DateTimeInterface $date): array;
    
    public function getRecetteJournaliere(\DateTimeInterface $date): float;
    
    public function getBurgersPlusVendus(\DateTimeInterface $date, int $limit = 5): array;
    
    public function getCommandesAnnulees(\DateTimeInterface $date): array;
    
    public function getStatistiquesParPeriode(\DateTimeInterface $debut, \DateTimeInterface $fin): array;
}