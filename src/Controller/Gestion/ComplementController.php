<?php

namespace App\Controller\Gestion;  // Ajouter ce namespace

use App\Entity\Complement;
use App\Form\ComplementType;
use App\Repository\ComplementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/gestion/complements')]
class ComplementController extends AbstractController
{
    #[Route('/', name: 'gestion_complement_index', methods: ['GET'])]
    public function index(ComplementRepository $complementRepository): Response
    {
        $complements = $complementRepository->findAllOrderByNom();
        
        return $this->render('gestion/complement/index.html.twig', [
            'complements' => $complements,
        ]);
    }
    
    #[Route('/nouveau', name: 'gestion_complement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $complement = new Complement();
        $form = $this->createForm(ComplementType::class, $complement);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($complement);
            $entityManager->flush();
            
            $this->addFlash('success', 'Complément créé avec succès.');
            return $this->redirectToRoute('gestion_complement_index');
        }
        
        return $this->render('gestion/complement/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/modifier', name: 'gestion_complement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Complement $complement, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ComplementType::class, $complement);
        $form->handleRequest($request);
        
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            
            $this->addFlash('success', 'Complément modifié avec succès.');
            return $this->redirectToRoute('gestion_complement_index');
        }
        
        return $this->render('gestion/complement/edit.html.twig', [
            'complement' => $complement,
            'form' => $form->createView(),
        ]);
    }
    
    #[Route('/{id}/archiver', name: 'gestion_complement_archive', methods: ['POST'])]
    public function archive(Request $request, Complement $complement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('archive' . $complement->getId(), $request->request->get('_token'))) {
            $complement->setArchived(!$complement->isArchived());
            $entityManager->flush();
            
            $action = $complement->isArchived() ? 'archivé' : 'désarchivé';
            $this->addFlash('success', "Complément {$action} avec succès.");
        }
        
        return $this->redirectToRoute('gestion_complement_index');
    }
    
    #[Route('/{id}/supprimer', name: 'gestion_complement_delete', methods: ['POST'])]
    public function delete(Request $request, Complement $complement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $complement->getId(), $request->request->get('_token'))) {
            $entityManager->remove($complement);
            $entityManager->flush();
            
            $this->addFlash('success', 'Complément supprimé avec succès.');
        }
        
        return $this->redirectToRoute('gestion_complement_index');
    }
}