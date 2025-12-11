package com.restaurant.services.Impl;

import com.restaurant.entity.Paiement;
import com.restaurant.repository.PaiementRepository;
import com.restaurant.repository.Impl.PaiementRepositoryImpl;
import com.restaurant.services.PaiementService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaiementServiceImpl implements PaiementService {
    
    private PaiementRepository paiementRepository = new PaiementRepositoryImpl();
    
    @Override
    public Paiement effectuerPaiement(Paiement paiement) {
        // Validation
        if (paiement.getCommande() == null) {
            throw new IllegalArgumentException("Un paiement doit être associé à une commande");
        }
        
        if (paiement.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être positif");
        }
        
        // Vérifier si la commande est déjà payée
        if (commandeEstPayee(paiement.getCommande().getId())) {
            throw new IllegalStateException("Cette commande a déjà été payée");
        }
        
        return paiementRepository.save(paiement);
    }
    
    @Override
    public Optional<Paiement> getPaiementById(int id) {
        return paiementRepository.findById(id);
    }
    
    @Override
    public Optional<Paiement> getPaiementParCommande(int commandeId) {
        return paiementRepository.findByCommandeId(commandeId);
    }
    
    @Override
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }
    
    @Override
    public List<Paiement> getPaiementsParDate(LocalDate date) {
        return paiementRepository.findByDate(date);
    }
    
    @Override
    public List<Paiement> getPaiementsParMethode(String methode) {
        return paiementRepository.findByMethode(methode);
    }
    
    @Override
    public double getTotalRecettes(LocalDate date) {
        return paiementRepository.getTotalRecettesByDate(date);
    }
    
    @Override
    public boolean commandeEstPayee(int commandeId) {
        return paiementRepository.commandeEstPayee(commandeId);
    }
}
