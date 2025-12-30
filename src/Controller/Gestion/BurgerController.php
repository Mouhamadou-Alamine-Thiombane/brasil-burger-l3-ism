<?php

namespace App\Controller\Gestion;

use App\Entity\Burger;
use App\Form\BurgerType;
use App\Repository\BurgerRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/burgers')]
class BurgerController extends AbstractController
{
    #[Route('/', name: 'gestion_burger_index')]
    public function index(BurgerRepository $burgerRepository): Response
    {
        $burgers = $burgerRepository->findAllOrderByNom();
        
        return $this->render('gestion/burger/index.html.twig', [
            'burgers' => $burgers,
        ]);
    }
    
    #[Route('/nouveau', name: 'gestion_burger_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $burger = new Burger();
        $form = $this->createForm(BurgerType::class, $burger);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($burger);
            $entityManager->flush();
            
            $this->addFlash('success', 'Burger créé avec succès.');
            return $this->redirectToRoute('gestion_burger_index');
        }
        
        return $this->render('gestion/burger/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/modifier', name: 'gestion_burger_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Burger $burger, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(BurgerType::class, $burger);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            
            $this->addFlash('success', 'Burger modifié avec succès.');
            return $this->redirectToRoute('gestion_burger_index');
        }
        
        return $this->render('gestion/burger/edit.html.twig', [
            'burger' => $burger,
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/archiver', name: 'gestion_burger_archive', methods: ['POST'])]
    public function archive(Request $request, Burger $burger, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('archive' . $burger->getId(), $request->request->get('_token'))) {
            $burger->setArchived(!$burger->isArchived());
            $entityManager->flush();
            
            $action = $burger->isArchived() ? 'archivé' : 'désarchivé';
            $this->addFlash('success', "Burger {$action} avec succès.");
        }
        
        return $this->redirectToRoute('gestion_burger_index');
    }
    
    #[Route('/{id}/supprimer', name: 'gestion_burger_delete', methods: ['POST'])]
    public function delete(Request $request, Burger $burger, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $burger->getId(), $request->request->get('_token'))) {
            $entityManager->remove($burger);
            $entityManager->flush();
            
            $this->addFlash('success', 'Burger supprimé avec succès.');
        }
        
        return $this->redirectToRoute('gestion_burger_index');
    }
}