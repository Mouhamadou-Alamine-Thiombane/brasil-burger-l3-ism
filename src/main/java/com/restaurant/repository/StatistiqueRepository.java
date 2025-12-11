package com.restaurant.repository;

import com.restaurant.dto.StatistiqueDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatistiqueRepository {
    int getCommandesEnCoursDuJour();
    int getCommandesValideesDuJour();
    double getRecettesJournalieres(LocalDate date);
    List<Map<String, Object>> getBurgersPlusVendusDuJour();
    int getCommandesAnnuleesDuJour();
    Map<String, Double> getChiffreAffaireParMois(int annee);
    int getNombreClientsInscrits(LocalDate date);
    Map<String, Integer> getVentesParTypeProduit(LocalDate date);
}
