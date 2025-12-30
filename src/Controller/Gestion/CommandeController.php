<?php

namespace App\Controller\Gestion;

use App\Entity\Commande;
use App\Form\CommandeFilterType;
use App\Repository\CommandeRepository;
use App\Repository\LivreurRepository;
use App\Service\Interface\CommandeServiceInterface;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/commandes')]
class CommandeController extends AbstractController
{
    #[Route('/', name: 'gestion_commande_index')]
    public function index(Request $request, CommandeRepository $commandeRepository): Response
    {
        $filterForm = $this->createForm(CommandeFilterType::class);
        $filterForm->handleRequest($request);
        
        $commandes = [];
        
        if ($filterForm->isSubmitted() && $filterForm->isValid()) {
            $filters = $filterForm->getData();
            $commandes = $commandeRepository->findByFilters($filters);
        } else {
            $commandes = $commandeRepository->findRecentCommandes(100);
        }
        
        return $this->render('gestion/commande/index.html.twig', [
            'commandes' => $commandes,
            'filterForm' => $filterForm->createView(),
        ]);
    }
    
    #[Route('/{id}', name: 'gestion_commande_show', methods: ['GET'])]
    public function show(Commande $commande): Response
    {
        return $this->render('gestion/commande/show.html.twig', [
            'commande' => $commande,
        ]);
    }
    
    #[Route('/{id}/valider', name: 'gestion_commande_valider', methods: ['POST'])]
    public function valider(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'EN_ATTENTE') {
            $commande->setEtat(\App\Entity\EtatCommande::VALIDEE);
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande validée avec succès.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être validée.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/{id}/annuler', name: 'gestion_commande_annuler', methods: ['POST'])]
    public function annuler(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'EN_ATTENTE' || $commande->getEtat()->value === 'VALIDEE') {
            $commande->setEtat(\App\Entity\EtatCommande::ANNULEE);
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande annulée avec succès.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être annulée.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/{id}/preparer', name: 'gestion_commande_preparer', methods: ['POST'])]
    public function preparer(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'VALIDEE') {
            $commande->setEtat(\App\Entity\EtatCommande::EN_PREPARATION);
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande mise en préparation.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être mise en préparation.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/{id}/terminer', name: 'gestion_commande_terminer', methods: ['POST'])]
    public function terminer(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'EN_PREPARATION') {
            $commande->setEtat(\App\Entity\EtatCommande::PRETE);
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande marquée comme prête.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être marquée comme prête.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/{id}/livrer', name: 'gestion_commande_livrer', methods: ['POST'])]
    public function livrer(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'PRETE' && $commande->getTypeLivraison() === 'LIVRAISON') {
            $commande->setEtat(\App\Entity\EtatCommande::EN_LIVRAISON);
            $commande->setDateLivraison(new \DateTimeImmutable());
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande mise en livraison.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être mise en livraison.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/{id}/livree', name: 'gestion_commande_livree', methods: ['POST'])]
    public function livree(Commande $commande, EntityManagerInterface $entityManager): Response
    {
        if ($commande->getEtat()->value === 'EN_LIVRAISON') {
            $commande->setEtat(\App\Entity\EtatCommande::LIVREE);
            $entityManager->flush();
            
            $this->addFlash('success', 'Commande marquée comme livrée.');
        } else {
            $this->addFlash('error', 'Cette commande ne peut pas être marquée comme livrée.');
        }
        
        return $this->redirectToRoute('gestion_commande_show', ['id' => $commande->getId()]);
    }
    
    #[Route('/affectation-livraison', name: 'gestion_commande_affectation_livraison')]
    public function affectationLivraison(
        Request $request,
        CommandeRepository $commandeRepository,
        LivreurRepository $livreurRepository,
        EntityManagerInterface $entityManager
    ): Response {
        // Commandes prêtes à livrer
        $commandesPreteALivrer = $commandeRepository->findCommandesPreteALivrer();
        
        // Livreurs disponibles
        $livreursDisponibles = $livreurRepository->findLivreursDisponibles();
        
        if ($request->isMethod('POST')) {
            $commandeId = $request->request->get('commande_id');
            $livreurId = $request->request->get('livreur_id');
            
            $commande = $commandeRepository->find($commandeId);
            $livreur = $livreurRepository->find($livreurId);
            
            if ($commande && $livreur) {
                $commande->setLivreur($livreur);
                $commande->setEtat(\App\Entity\EtatCommande::EN_LIVRAISON);
                $commande->setDateLivraison(new \DateTimeImmutable());
                $livreur->setDisponible(false);
                
                $entityManager->flush();
                
                $this->addFlash('success', 'Livreur affecté avec succès à la commande #' . $commande->getId());
            }
        }
        
        return $this->render('gestion/commande/affectation_livraison.html.twig', [
            'commandes' => $commandesPreteALivrer,
            'livreurs' => $livreursDisponibles,
        ]);
    }
}