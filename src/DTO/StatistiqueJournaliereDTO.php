<?php

namespace App\DTO;

class StatistiqueJournaliereDTO
{
    public ?\DateTimeInterface $date = null;
    public array $commandesEnCours = [];
    public array $commandesValidees = [];
    public float $recetteJournaliere = 0;
    public array $burgersPlusVendus = [];
    public array $commandesAnnulees = [];
}