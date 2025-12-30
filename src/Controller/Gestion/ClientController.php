<?php

namespace App\Controller\Gestion;

use App\Entity\Client;
use App\Repository\ClientRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/clients')]
class ClientController extends AbstractController
{
    #[Route('/', name: 'gestion_client_index')]
    public function index(ClientRepository $clientRepository, Request $request): Response
    {
        // Récupérer le terme de recherche
        $search = $request->query->get('search', '');
        
        if ($search) {
            $clients = $clientRepository->search($search);
        } else {
            $clients = $clientRepository->findBy([], ['nom' => 'ASC']);
        }
        
        return $this->render('gestion/client/index.html.twig', [
            'clients' => $clients,
            'search' => $search,
        ]);
    }
    
    #[Route('/{id}', name: 'gestion_client_show', methods: ['GET'])]
    public function show(Client $client): Response
    {
        // Récupérer les commandes du client
        $commandes = $client->getCommandes();
        
        return $this->render('gestion/client/show.html.twig', [
            'client' => $client,
            'commandes' => $commandes,
        ]);
    }
    
    #[Route('/{id}/supprimer', name: 'gestion_client_delete', methods: ['POST'])]
    public function delete(Request $request, Client $client, ClientRepository $clientRepository): Response
    {
        if ($this->isCsrfTokenValid('delete' . $client->getId(), $request->request->get('_token'))) {
            // Marquer comme archivé plutôt que supprimer
            $client->setArchived(true);
            $clientRepository->save($client, true);
            
            $this->addFlash('success', 'Client archivé avec succès.');
        }
        
        return $this->redirectToRoute('gestion_client_index');
    }
}