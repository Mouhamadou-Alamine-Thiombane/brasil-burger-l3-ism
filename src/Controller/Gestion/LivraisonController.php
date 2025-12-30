<?php

namespace App\Controller\Gestion;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/livraisons')]
class LivraisonController extends AbstractController
{
    #[Route('/', name: 'gestion_livraison_index')]
    public function index(): Response
    {
        return $this->render('gestion/livraison/index.html.twig', [
            'message' => 'Gestion des livraisons',
        ]);
    }
}