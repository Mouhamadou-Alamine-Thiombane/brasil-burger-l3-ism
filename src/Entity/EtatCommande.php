<?php

namespace App\Entity;

class EtatCommande
{
    public const EN_ATTENTE = 'EN_ATTENTE';
    public const VALIDEE = 'VALIDEE';
    public const EN_PREPARATION = 'EN_PREPARATION';
    public const PRETE = 'PRETE';
    public const EN_LIVRAISON = 'EN_LIVRAISON';
    public const LIVREE = 'LIVREE';
    public const ANNULEE = 'ANNULEE';
    public const TERMINEE = 'TERMINEE';
    
    public static function getValues(): array
    {
        return [
            self::EN_ATTENTE,
            self::VALIDEE,
            self::EN_PREPARATION,
            self::PRETE,
            self::EN_LIVRAISON,
            self::LIVREE,
            self::ANNULEE,
            self::TERMINEE,
        ];
    }
    
    public static function getLabel(string $etat): string
    {
        $labels = [
            self::EN_ATTENTE => 'En attente',
            self::VALIDEE => 'Validée',
            self::EN_PREPARATION => 'En préparation',
            self::PRETE => 'Prête',
            self::EN_LIVRAISON => 'En livraison',
            self::LIVREE => 'Livrée',
            self::ANNULEE => 'Annulée',
            self::TERMINEE => 'Terminée',
        ];
        
        return $labels[$etat] ?? $etat;
    }
}