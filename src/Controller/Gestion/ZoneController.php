<?php

namespace App\Controller\Gestion;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/zones')]
class ZoneController extends AbstractController
{
    #[Route('/', name: 'gestion_zone_index')]
    public function index(): Response
    {
        return $this->render('gestion/zone/index.html.twig', [
            'message' => 'Gestion des zones de livraison',
        ]);
    }
}