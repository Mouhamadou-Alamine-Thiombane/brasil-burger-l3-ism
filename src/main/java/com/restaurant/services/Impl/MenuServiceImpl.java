package com.restaurant.services.Impl;

import com.restaurant.entity.Menu;
import com.restaurant.repository.MenuRepository;
import com.restaurant.repository.Impl.MenuRepositoryImpl;
import com.restaurant.services.MenuService;
import java.util.List;

public class MenuServiceImpl implements MenuService {
    
    private MenuRepository menuRepository = new MenuRepositoryImpl();
    
    @Override
    public Menu creerMenu(Menu menu) {
        return menuRepository.save(menu);
    }
    
    @Override
    public Menu modifierMenu(Menu menu) {
        return menuRepository.update(menu);
    }
    
    @Override
    public boolean archiverMenu(int id) {
        return menuRepository.archive(id);
    }
    
    @Override
    public Menu getMenuById(int id) {
        return menuRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    
    @Override
    public List<Menu> getMenusActifs() {
        return menuRepository.findAllActive();
    }
    
    @Override
    public List<Menu> getMenusParBurger(int burgerId) {
        return menuRepository.findByBurgerId(burgerId);
    }
    
    @Override
    public double calculerPrixMenu(int menuId) {
        return menuRepository.calculatePrixMenu(menuId);
    }
    
    @Override
    public List<Menu> rechercherMenus(String keyword) {
        return menuRepository.findByNomContaining(keyword);
    }
}
