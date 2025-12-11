package com.restaurant.services;

import com.restaurant.entity.Burger;
import java.util.List;

public interface BurgerService {
    Burger ajouterBurger(Burger burger);
    Burger modifierBurger(Burger burger);
    boolean archiverBurger(int id);
    Burger getBurgerById(int id);
    List<Burger> getAllBurgers();
    List<Burger> getBurgersActifs();
    List<Burger> rechercherBurgers(String nom);
    List<Burger> filtrerBurgersParPrix(double min, double max);
}
