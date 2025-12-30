<?php

namespace App\Service\Impl;

use App\Service\Interface\ProduitServiceInterface;
use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;
use Doctrine\ORM\EntityManagerInterface;

class ProduitServiceImpl implements ProduitServiceInterface
{
    private EntityManagerInterface $entityManager;

    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    public function archiverBurger(Burger $burger): void
    {
        $burger->setArchived(true);
        $this->entityManager->flush();
    }

    public function desarchiverBurger(Burger $burger): void
    {
        $burger->setArchived(false);
        $this->entityManager->flush();
    }

    public function archiverMenu(Menu $menu): void
    {
        $menu->setArchived(true);
        $this->entityManager->flush();
    }

    public function desarchiverMenu(Menu $menu): void
    {
        $menu->setArchived(false);
        $this->entityManager->flush();
    }

    public function archiverComplement(Complement $complement): void
    {
        $complement->setArchived(true);
        $this->entityManager->flush();
    }

    public function desarchiverComplement(Complement $complement): void
    {
        $complement->setArchived(false);
        $this->entityManager->flush();
    }

    public function calculerPrixMenu(Menu $menu): float
    {
        $prixTotal = $menu->getBurger()->getPrix();
        
        if ($menu->getFrite()) {
            $prixTotal += $menu->getFrite()->getPrix();
        }
        
        if ($menu->getBoisson()) {
            $prixTotal += $menu->getBoisson()->getPrix();
        }
        
        return $prixTotal;
    }

    public function verifierDisponibiliteBurger(Burger $burger): bool
    {
        return !$burger->isArchived();
    }

    public function verifierDisponibiliteMenu(Menu $menu): bool
    {
        if ($menu->isArchived()) {
            return false;
        }
        
        // Vérifier que tous les composants sont disponibles
        if (!$this->verifierDisponibiliteBurger($menu->getBurger())) {
            return false;
        }
        
        if ($menu->getFrite() && $menu->getFrite()->isArchived()) {
            return false;
        }
        
        if ($menu->getBoisson() && $menu->getBoisson()->isArchived()) {
            return false;
        }
        
        return true;
    }
}