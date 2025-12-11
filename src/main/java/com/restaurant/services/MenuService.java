package com.restaurant.services;

import com.restaurant.entity.Menu;
import java.util.List;

public interface MenuService {
    Menu creerMenu(Menu menu);
    Menu modifierMenu(Menu menu);
    boolean archiverMenu(int id);
    Menu getMenuById(int id);
    List<Menu> getAllMenus();
    List<Menu> getMenusActifs();
    List<Menu> getMenusParBurger(int burgerId);
    double calculerPrixMenu(int menuId);
    List<Menu> rechercherMenus(String keyword);
}
