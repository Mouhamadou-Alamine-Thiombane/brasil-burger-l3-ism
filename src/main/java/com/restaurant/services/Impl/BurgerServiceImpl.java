package com.restaurant.services.Impl;

import com.restaurant.entity.Burger;
import com.restaurant.repository.BurgerRepository;
import com.restaurant.repository.Impl.BurgerRepositoryImpl;
import com.restaurant.services.BurgerService;
import java.util.List;

public class BurgerServiceImpl implements BurgerService {
    
    private BurgerRepository burgerRepository = new BurgerRepositoryImpl();
    
    @Override
    public Burger ajouterBurger(Burger burger) {
        return burgerRepository.save(burger);
    }
    
    @Override
    public Burger modifierBurger(Burger burger) {
        return burgerRepository.update(burger);
    }
    
    @Override
    public boolean archiverBurger(int id) {
        return burgerRepository.archive(id);
    }
    
    @Override
    public Burger getBurgerById(int id) {
        return burgerRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Burger> getAllBurgers() {
        return burgerRepository.findAll();
    }
    
    @Override
    public List<Burger> getBurgersActifs() {
        return burgerRepository.findAllActive();
    }
    
    @Override
    public List<Burger> rechercherBurgers(String nom) {
        return burgerRepository.findByNom(nom);
    }
    
    @Override
    public List<Burger> filtrerBurgersParPrix(double min, double max) {
        return burgerRepository.findByPrixBetween(min, max);
    }
}
