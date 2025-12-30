<?php

namespace App\Controller\Api;

use App\Service\Interface\StatistiqueServiceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/api')]
class StatistiqueController extends AbstractController
{
    #[Route('/statistiques/journalieres', name: 'api_statistiques_journalieres', methods: ['GET'])]
    public function getStatistiquesJournalieres(
        Request $request,
        StatistiqueServiceInterface $statistiqueService
    ): JsonResponse {
        $dateStr = $request->query->get('date', date('Y-m-d'));
        $date = new \DateTimeImmutable($dateStr);
        
        $statistiques = $statistiqueService->getStatistiquesJournalieres($date);
        
        return $this->json([
            'success' => true,
            'data' => [
                'date' => $statistiques->date->format('Y-m-d'),
                'commandes_en_cours' => count($statistiques->commandesEnCours),
                'commandes_validees' => $statistiques->commandesValidees[0]['count'] ?? 0,
                'recette_journaliere' => $statistiques->recetteJournaliere,
                'burgers_plus_vendus' => $statistiques->burgersPlusVendus,
                'commandes_annulees' => $statistiques->commandesAnnulees[0]['count'] ?? 0,
            ]
        ]);
    }
    
    #[Route('/statistiques/periodique', name: 'api_statistiques_periodique', methods: ['GET'])]
    public function getStatistiquesPeriodique(
        Request $request,
        StatistiqueServiceInterface $statistiqueService
    ): JsonResponse {
        $debut = new \DateTimeImmutable($request->query->get('debut', date('Y-m-d')));
        $fin = new \DateTimeImmutable($request->query->get('fin', date('Y-m-d')));
        
        $statistiques = $statistiqueService->getStatistiquesParPeriode($debut, $fin);
        
        $data = [];
        foreach ($statistiques as $stat) {
            $data[] = [
                'date' => $stat->date->format('Y-m-d'),
                'commandes_en_cours' => count($stat->commandesEnCours),
                'commandes_validees' => $stat->commandesValidees[0]['count'] ?? 0,
                'recette_journaliere' => $stat->recetteJournaliere,
                'commandes_annulees' => $stat->commandesAnnulees[0]['count'] ?? 0,
            ];
        }
        
        return $this->json([
            'success' => true,
            'data' => $data
        ]);
    }
}