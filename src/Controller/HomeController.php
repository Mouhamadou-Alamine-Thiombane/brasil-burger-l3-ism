<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class HomeController extends AbstractController
{
    #[Route('/', name: 'app_home')]
    public function index(): Response
    {
        // Redirige vers le tableau de bord gestionnaire
        return $this->redirectToRoute('gestion_dashboard');
    }
    
    #[Route('/gestion', name: 'app_gestion_redirect')]
    public function gestionRedirect(): Response
    {
        return $this->redirectToRoute('gestion_dashboard');
    }
}