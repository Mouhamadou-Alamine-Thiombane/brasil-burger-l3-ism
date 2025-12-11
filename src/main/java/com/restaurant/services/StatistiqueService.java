package com.restaurant.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatistiqueService {
    int getNombreCommandesEnCoursDuJour();
    int getNombreCommandesValideesDuJour();
    double getRecettesJournalieres(LocalDate date);
    List<Map<String, Object>> getBurgersPlusVendusDuJour();
    int getNombreCommandesAnnuleesDuJour();
    Map<String, Double> getChiffreAffaireParMois(int annee);
    int getNombreNouveauxClients(LocalDate date);
    Map<String, Integer> getVentesParTypeProduit(LocalDate date);
    Map<String, Object> getTableauDeBord(LocalDate date);
}
