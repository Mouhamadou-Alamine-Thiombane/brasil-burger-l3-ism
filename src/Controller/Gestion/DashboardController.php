<?php

namespace App\Controller\Gestion;

use App\Service\Interface\StatistiqueServiceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion')]
class DashboardController extends AbstractController
{
    #[Route('/', name: 'gestion_dashboard')]
    public function dashboard(StatistiqueServiceInterface $statistiqueService): Response
    {
        $aujourdhui = new \DateTimeImmutable();
        
        try {
            $statistiques = $statistiqueService->getStatistiquesJournalieres($aujourdhui);
        } catch (\Exception $e) {
            // Fallback en cas d'erreur
            $statistiques = (object) [
                'commandesEnCours' => [],
                'commandesValidees' => [['count' => 0]],
                'recetteJournaliere' => 0,
                'burgersPlusVendus' => [],
                'commandesAnnulees' => [['count' => 0]],
            ];
        }
        
        return $this->render('gestion/dashboard/index.html.twig', [
            'statistiques' => $statistiques,
            'aujourdhui' => $aujourdhui,
        ]);
    }
    
    #[Route('/statistiques', name: 'gestion_statistiques')]
    public function statistiques(Request $request, StatistiqueServiceInterface $statistiqueService): Response
    {
        $dateStr = $request->query->get('date', date('Y-m-d'));
        $date = new \DateTimeImmutable($dateStr);
        
        try {
            $statistiques = $statistiqueService->getStatistiquesJournalieres($date);
            
            // Statistiques du mois pour afficher un graphique
            $debutMois = (clone $date)->modify('first day of this month')->setTime(0, 0, 0);
            $finMois = (clone $date)->modify('last day of this month')->setTime(23, 59, 59);
            $statistiquesMensuelles = $statistiqueService->getStatistiquesParPeriode($debutMois, $finMois);
        } catch (\Exception $e) {
            $statistiques = (object) [
                'commandesEnCours' => [],
                'commandesValidees' => [['count' => 0]],
                'recetteJournaliere' => 0,
                'burgersPlusVendus' => [],
                'commandesAnnulees' => [['count' => 0]],
            ];
            $statistiquesMensuelles = [];
        }
        
        return $this->render('gestion/statistiques/index.html.twig', [
            'statistiques' => $statistiques,
            'statistiquesMensuelles' => $statistiquesMensuelles,
            'date' => $date,
        ]);
    }
    
    #[Route('/aide', name: 'gestion_aide')]
    public function aide(): Response
    {
        return $this->render('gestion/aide/index.html.twig');
    }
}