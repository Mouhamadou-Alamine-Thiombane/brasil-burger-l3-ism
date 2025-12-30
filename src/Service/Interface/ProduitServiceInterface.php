<?php

namespace App\Service\Interface;

use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;

interface ProduitServiceInterface
{
    public function archiverBurger(Burger $burger): void;
    
    public function desarchiverBurger(Burger $burger): void;
    
    public function archiverMenu(Menu $menu): void;
    
    public function desarchiverMenu(Menu $menu): void;
    
    public function archiverComplement(Complement $complement): void;
    
    public function desarchiverComplement(Complement $complement): void;
    
    public function calculerPrixMenu(Menu $menu): float;
    
    public function verifierDisponibiliteBurger(Burger $burger): bool;
    
    public function verifierDisponibiliteMenu(Menu $menu): bool;
}