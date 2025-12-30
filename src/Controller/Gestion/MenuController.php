<?php

namespace App\Controller\Gestion;

use App\Entity\Menu;
use App\Form\MenuType;
use App\Repository\MenuRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/menus')]
class MenuController extends AbstractController
{
    #[Route('/', name: 'gestion_menu_index')]
    public function index(MenuRepository $menuRepository): Response
    {
        $menus = $menuRepository->findAllWithBurgers();
        
        return $this->render('gestion/menu/index.html.twig', [
            'menus' => $menus,
        ]);
    }
    
    #[Route('/nouveau', name: 'gestion_menu_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $menu = new Menu();
        $form = $this->createForm(MenuType::class, $menu);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            // Calculer le prix total
            $prixTotal = $menu->getBurger()->getPrix();
            
            if ($menu->getFrite()) {
                $prixTotal += $menu->getFrite()->getPrix();
            }
            
            if ($menu->getBoisson()) {
                $prixTotal += $menu->getBoisson()->getPrix();
            }
            
            $menu->setPrix($prixTotal);
            
            $entityManager->persist($menu);
            $entityManager->flush();
            
            $this->addFlash('success', 'Menu créé avec succès.');
            return $this->redirectToRoute('gestion_menu_index');
        }
        
        return $this->render('gestion/menu/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/modifier', name: 'gestion_menu_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Menu $menu, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(MenuType::class, $menu);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            // Recalculer le prix total
            $prixTotal = $menu->getBurger()->getPrix();
            
            if ($menu->getFrite()) {
                $prixTotal += $menu->getFrite()->getPrix();
            }
            
            if ($menu->getBoisson()) {
                $prixTotal += $menu->getBoisson()->getPrix();
            }
            
            $menu->setPrix($prixTotal);
            
            $entityManager->flush();
            
            $this->addFlash('success', 'Menu modifié avec succès.');
            return $this->redirectToRoute('gestion_menu_index');
        }
        
        return $this->render('gestion/menu/edit.html.twig', [
            'menu' => $menu,
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/archiver', name: 'gestion_menu_archive', methods: ['POST'])]
    public function archive(Request $request, Menu $menu, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('archive' . $menu->getId(), $request->request->get('_token'))) {
            $menu->setArchived(!$menu->isArchived());
            $entityManager->flush();
            
            $action = $menu->isArchived() ? 'archivé' : 'désarchivé';
            $this->addFlash('success', "Menu {$action} avec succès.");
        }
        
        return $this->redirectToRoute('gestion_menu_index');
    }
    
    #[Route('/{id}/supprimer', name: 'gestion_menu_delete', methods: ['POST'])]
    public function delete(Request $request, Menu $menu, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $menu->getId(), $request->request->get('_token'))) {
            $entityManager->remove($menu);
            $entityManager->flush();
            
            $this->addFlash('success', 'Menu supprimé avec succès.');
        }
        
        return $this->redirectToRoute('gestion_menu_index');
    }
}