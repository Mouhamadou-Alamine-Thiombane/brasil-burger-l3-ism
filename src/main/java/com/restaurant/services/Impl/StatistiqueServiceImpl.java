package com.restaurant.services.Impl;

import com.restaurant.repository.StatistiqueRepository;
import com.restaurant.repository.Impl.StatistiqueRepositoryImpl;
import com.restaurant.services.StatistiqueService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiqueServiceImpl implements StatistiqueService {
    
    private StatistiqueRepository statistiqueRepository = new StatistiqueRepositoryImpl();
    
    @Override
    public int getNombreCommandesEnCoursDuJour() {
        return statistiqueRepository.getCommandesEnCoursDuJour();
    }
    
    @Override
    public int getNombreCommandesValideesDuJour() {
        return statistiqueRepository.getCommandesValideesDuJour();
    }
    
    @Override
    public double getRecettesJournalieres(LocalDate date) {
        return statistiqueRepository.getRecettesJournalieres(date);
    }
    
    @Override
    public List<Map<String, Object>> getBurgersPlusVendusDuJour() {
        return statistiqueRepository.getBurgersPlusVendusDuJour();
    }
    
    @Override
    public int getNombreCommandesAnnuleesDuJour() {
        return statistiqueRepository.getCommandesAnnuleesDuJour();
    }
    
    @Override
    public Map<String, Double> getChiffreAffaireParMois(int annee) {
        return statistiqueRepository.getChiffreAffaireParMois(annee);
    }
    
    @Override
    public int getNombreNouveauxClients(LocalDate date) {
        return statistiqueRepository.getNombreClientsInscrits(date);
    }
    
    @Override
    public Map<String, Integer> getVentesParTypeProduit(LocalDate date) {
        return statistiqueRepository.getVentesParTypeProduit(date);
    }
    
    @Override
    public Map<String, Object> getTableauDeBord(LocalDate date) {
        Map<String, Object> tableauDeBord = new HashMap<>();
        
        tableauDeBord.put("commandes_en_cours", getNombreCommandesEnCoursDuJour());
        tableauDeBord.put("commandes_validees", getNombreCommandesValideesDuJour());
        tableauDeBord.put("recettes_journalieres", getRecettesJournalieres(date));
        tableauDeBord.put("burgers_plus_vendus", getBurgersPlusVendusDuJour());
        tableauDeBord.put("commandes_annulees", getNombreCommandesAnnuleesDuJour());
        tableauDeBord.put("nouveaux_clients", getNombreNouveauxClients(date));
        tableauDeBord.put("ventes_par_type", getVentesParTypeProduit(date));
        
        return tableauDeBord;
    }
}
