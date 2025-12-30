<?php

namespace App\Controller\Gestion;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/livreurs')]
class LivreurController extends AbstractController
{
    #[Route('/', name: 'gestion_livreur_index')]
    public function index(): Response
    {
        return $this->render('gestion/livreur/index.html.twig', [
            'message' => 'Gestion des livreurs',
        ]);
    }
}